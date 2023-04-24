package com.mx.path.gateway.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Supplier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.common.collections.ObjectArray;
import com.mx.common.collections.ObjectMap;
import com.mx.common.configuration.ConfigurationField;
import com.mx.common.lang.Strings;
import com.mx.common.reflection.Annotations;
import com.mx.common.reflection.Constructors;
import com.mx.common.reflection.Fields;
import com.mx.common.serialization.ConfigurationTypeAdapter;
import com.mx.path.gateway.configuration.annotations.ClientID;
import com.mx.path.utilities.reflection.ClassHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Build and bind configuration POJO from ObjectMap
 *
 * <p>The POJO must have a no-argument constructor. Fields to be populated must
 * be annotated with {@link ConfigurationField}
 */
public class ConfigurationBinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(GatewayObjectConfigurator.class);
  private static final Gson GSON = new GsonBuilder()
      .setPrettyPrinting()
      .registerTypeAdapterFactory(new ConfigurationTypeAdapter.Factory())
      .create();

  private final ConfigurationState state;
  private final String clientId;

  public ConfigurationBinder(String clientId, ConfigurationState state) {
    this.clientId = clientId;
    this.state = state;
  }

  /**
   * Create an instance of klass and populate its fields from configurationMap
   *
   * @param klass
   * @param configurationMap
   * @return
   */
  public <T> T build(Class<T> klass, ObjectMap configurationMap) {
    T configuration = Constructors.instantiateWithNoArgumentConstructor(klass);
    configure(configuration, configurationMap);

    return configuration;
  }

  /**
   * Populate fields of given configuration POJO with given {@link ObjectMap}
   *
   * @param configuration Configuration POJO instance
   * @param configurationMap Configuration ObjectMap
   */
  public final void configure(Object configuration, ObjectMap configurationMap) {
    populateFields(configuration, configurationMap);

    if (Configurable.class.isAssignableFrom(configuration.getClass())) {
      ((Configurable) configuration).initialize();
      ((Configurable) configuration).validate(state);
    }

    try {
      LOGGER.debug("Configuration binding: " + configuration.getClass().getName() + " -> " + GSON.toJson(configuration));
    } catch (Exception e) {
      LOGGER.warn("Unable to serialize configuration: " + configuration.getClass().getName(), e);
    }
  }

  /**
   * Constructs array for given configuration map
   *
   * @param configurationValue
   * @param annotatedField
   * @param inArray
   * @return
   */
  private List<Object> buildArray(ObjectArray configurationValue, Annotations.AnnotatedField<ConfigurationField> annotatedField, boolean inArray) {
    List<Object> array = new ArrayList<>();

    annotatedField.setElementType(resolveArrayElementClass(annotatedField));
    if (annotatedField.getElementType() == null) {
      throw new ConfigurationError("Must provide elementType if configuration element is a list", state);
    }

    return buildInArray(inArray, annotatedField.getField().getName(), () -> {
      int objectIndex = 0;
      for (Object item : configurationValue) {
        state.pushLevel(String.valueOf(objectIndex++));
        try {
          array.add(buildValue(item, annotatedField, true));
        } finally {
          state.popLevel();
        }
      }

      return array;
    });
  }

  /**
   * Pushes and pops given level around executing supplier when populating an array
   *
   * @param inArray
   * @param level
   * @param supplier
   * @param <T>
   * @return
   */
  private <T> T buildInArray(boolean inArray, String level, Supplier<T> supplier) {
    if (!inArray) {
      state.pushLevel(level);
    }

    try {
      return supplier.get();
    } finally {
      if (!inArray) {
        state.popLevel();
      }
    }
  }

  /**
   * Construct Object for given configuration map
   *
   * @param configurationMap
   * @param annotatedField
   * @param inArray
   * @return
   */
  private Object buildObject(ObjectMap configurationMap, Annotations.AnnotatedField<ConfigurationField> annotatedField, boolean inArray) {
    return buildInArray(inArray, annotatedField.getField().getName(), () -> {
      Class<?> klass = annotatedField.getElementType() != null ? annotatedField.getElementType() : annotatedField.getAnnotation().elementType();
      if (klass == Void.class) {
        klass = annotatedField.getField().getType();
      }

      return build(klass, configurationMap);
    });
  }

  /**
   * Coerces given, raw value from configuration map to expected type
   *
   * @param configurationValue
   * @param annotatedField
   * @return
   */
  private Object buildValue(Object configurationValue, Annotations.AnnotatedField<ConfigurationField> annotatedField) {
    return buildValue(configurationValue, annotatedField, false);
  }

  /**
   * Coerces given, raw value from configuration map to expected type.
   *
   * @param configurationValue
   * @param annotatedField
   * @param inArray
   * @return
   */
  private Object buildValue(Object configurationValue, Annotations.AnnotatedField<ConfigurationField> annotatedField, boolean inArray) {
    if (configurationValue instanceof ObjectArray) {
      return buildArray((ObjectArray) configurationValue, annotatedField, inArray);
    } else if (configurationValue instanceof ObjectMap) {

      return buildObject((ObjectMap) configurationValue, annotatedField, inArray);
    } else {
      return configurationValue;
    }
  }

  /**
   * Set all {@link ConfigurationField} field values from configurationMap
   *
   * @param obj
   * @param configurationMap
   */
  private void populateFields(Object obj, ObjectMap configurationMap) {
    Map<String, Annotations.AnnotatedField<ConfigurationField>> configurationFieldMap = prepareConfigurationObjectFields(obj);

    configurationMap.forEach((fieldName, fieldValue) -> {
      Annotations.AnnotatedField<ConfigurationField> configurationAnnotatedField = configurationFieldMap.get(fieldName);

      state.withField(fieldName, () -> {
        Object value;
        if (configurationAnnotatedField == null) {
          throw new ConfigurationError("Unknown field", state);
        }
        Field field = configurationAnnotatedField.getField();
        if (field.getType() == HashMap.class) {
          value = fieldValue;
        } else {
          value = this.buildValue(fieldValue, configurationAnnotatedField);
        }
        if (value != null) {
          Fields.setFieldValue(field, obj, value);
        }
      });
    });

    validate(obj);

    // Populate clientId fields
    List<Annotations.AnnotatedField<ClientID>> clientIdFields = Annotations.fieldsWithAnnotation(ClientID.class, obj.getClass());

    clientIdFields.forEach(annotatedField -> {
      String fieldName = annotatedField.getField().getName();
      Field field = annotatedField.getField();

      state.withField(fieldName, () -> {
        Fields.setFieldValue(field, obj, clientId);
      });
    });
  }

  /**
   * @param obj
   * @return Map of fields. Key = expected name of field, Value = AnnotatedField object
   */
  private Map<String, Annotations.AnnotatedField<ConfigurationField>> prepareConfigurationObjectFields(Object obj) {
    List<Annotations.FieldWithAnnotations> annotatedFields = Annotations.fieldsWithAnnotations(obj.getClass());

    return annotatedFields.stream()
        .map((field) -> field.asAnnotatedField(ConfigurationField.class))
        .collect(Collectors.toMap(field -> {
          if (field.getAnnotation() != null && Strings.isNotBlank(field.getAnnotation().value())) {
            return field.getAnnotation().value();
          } else {
            return field.getField().getName();
          }
        }, field -> field));
  }

  /**
   * @param annotatedField
   * @return the element class of given array field
   */
  private Class<?> resolveArrayElementClass(Annotations.AnnotatedField<ConfigurationField> annotatedField) {
    if (annotatedField.getAnnotation() != null && annotatedField.getAnnotation().elementType() != Void.class) {
      return annotatedField.getAnnotation().elementType();
    } else {
      List<Type> klasses = new ClassHelper().resolveParameterizedFieldTypes(annotatedField.getField());
      if (klasses.size() == 1) {
        return (Class<?>) klasses.get(0);
      }
    }

    return null;
  }

  /**
   * Apply annotation validations to given object
   *
   * @param obj
   */
  private void validate(Object obj) {
    List<Annotations.AnnotatedField<ConfigurationField>> annotatedFields = Annotations.fieldsWithAnnotation(ConfigurationField.class, obj.getClass());
    annotatedFields.forEach(annotatedField -> {
      Object value = Fields.getFieldValue(annotatedField.getField(), obj);
      String field = Strings.isBlank(annotatedField.getAnnotation().value()) ? annotatedField.getField().getName() : annotatedField.getAnnotation().value();
      state.withField(field, () -> validateField(annotatedField, value));
    });
  }

  /**
   * Apply annotation validations to given field value
   *
   * @param annotatedField
   * @param value
   */
  private void validateField(Annotations.AnnotatedField<ConfigurationField> annotatedField, Object value) {
    if (annotatedField.getAnnotation() == null) {
      return;
    }

    if (annotatedField.getAnnotation().required()) {
      if (value == null) {
        throw new ConfigurationError("Value required", state);
      }
      if (value instanceof String && Strings.isBlank((String) value)) {
        throw new ConfigurationError("Value required", state);
      }
    }
  }
}
