package com.mx.path.gateway.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import lombok.Builder;
import lombok.Data;

import com.mx.path.core.common.accessor.API;
import com.mx.path.core.common.accessor.Accessor;
import com.mx.path.core.common.accessor.AccessorConfiguration;
import com.mx.path.core.common.accessor.AccessorMethodDefinition;
import com.mx.path.core.common.accessor.AccessorResponse;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.common.reflection.Annotations;
import com.mx.path.core.common.reflection.Fields;

/**
 * Utility class for generating accessor descriptions
 *
 * <p>Uses the {@link API} annotation from base accessors and implementing classes
 * to build a description of the implementation.
 */
public class AccessorDescriber {

  @Data
  static class Description {
    private String description;
    private String notes;
    private String specification;

    public final void describe(ObjectMap desc) {
      if (Strings.isNotBlank(description)) {
        desc.put("description", description);
      }

      if (Strings.isNotBlank(notes)) {
        desc.put("notes", notes);
      }

      if (Strings.isNotBlank(specification)) {
        desc.put("specification", specification);
      }
    }
  }

  @Builder
  static class MethodDefinition {
    private String name;
    private Method method;
    private API annotation;
  }

  public final ObjectMap describe(@Nonnull Accessor accessor) {
    ObjectMap result = new ObjectMap();
    describe(accessor, result);
    return result;
  }

  public final void describe(@Nonnull Accessor accessor, @Nonnull ObjectMap result) {
    Class<? extends Accessor> klass;
    Class<? extends Accessor> proxyClass = null;
    if (AccessorProxy.class.isAssignableFrom(accessor.getClass())) {
      // If this accessor is a proxy, extract the actual accessor class
      klass = ((AccessorProxy) accessor).getAccessorClass();
      proxyClass = accessor.getClass();
    } else {
      klass = accessor.getClass();
    }
    Class<?> parentClass = Accessor.getAccessorBase(klass);

    describeClass(result, klass, parentClass, proxyClass);
    describeOperations(result, klass, parentClass);
    describeConfiguration(result, accessor);
  }

  public final ObjectMap describeDeep(@Nonnull Accessor accessor) {
    ObjectMap description = new ObjectMap();
    describeDeep(accessor, description);

    return description;
  }

  public final void describeDeep(@Nonnull Accessor accessor, @Nonnull ObjectMap description) {
    AccessorDescriber describer = new AccessorDescriber();
    describer.describe(accessor, description);

    List<AccessorMethodDefinition> childAccessors = Accessor.getBaseChildAccessorMethods(accessor.getClass());
    if (!childAccessors.isEmpty()) {
      description.createMap("accessors");
    }

    childAccessors.forEach(childAccessorDef -> {
      Accessor childAccessor = null;
      try {
        childAccessor = (Accessor) childAccessorDef.getMethod().invoke(accessor);
      } catch (IllegalAccessException | InvocationTargetException ignored) {
      }
      if (childAccessor != null) {
        describeDeep(childAccessor, description.getMap("accessors").createMap(childAccessorDef.getMethod().getName()));
      }
    });
  }

  private void describeConfiguration(ObjectMap result, Accessor accessor) {
    if (AccessorProxy.class.isAssignableFrom(accessor.getClass())) {
      Field field;
      try {

        field = accessor.getClass().getSuperclass().getDeclaredField("accessorConstructionContext");
      } catch (NoSuchFieldException e) {
        result.put("configurations", "unable to load construction context");
        return;
      }

      AccessorConstructionContext<?> constructionContext = (AccessorConstructionContext<?>) Fields.getFieldValue(field, accessor);
      constructionContext.describe(result);

      return;
    }

    Class<?> klass = accessor.getClass();
    Method getConfiguration;
    try {
      getConfiguration = klass.getMethod("getConfiguration");
    } catch (NoSuchMethodException e) {
      return;
    }

    AccessorConfiguration configuration;
    if (getConfiguration.getReturnType() == AccessorConfiguration.class) {
      try {
        configuration = (AccessorConfiguration) getConfiguration.invoke(accessor);
      } catch (IllegalAccessException | InvocationTargetException e) {
        return;
      }

      if (configuration != null) {
        configuration.describe(result);
      }
    }
  }

  private void describeOperations(ObjectMap result, Class<?> klass, Class<?> parentClass) {
    ObjectMap operationsNode = new ObjectMap();
    result.put("operations", operationsNode);

    Map<String, MethodDefinition> parentClassMethods = getAnnotatedMethods(parentClass);
    Map<String, MethodDefinition> classMethods = findDeclaredMethods(klass, parentClassMethods.keySet());

    parentClassMethods.forEach((name, parentDefinition) -> {
      MethodDefinition definition = classMethods.get(name);

      if (definition != null && definition.method.getDeclaringClass() == klass) {
        ObjectMap apiDescription = operationsNode.createMap(name);

        Description description = merge(parentDefinition.annotation, definition.annotation);
        description.describe(apiDescription);
      }
    });

  }

  private void describeClass(ObjectMap result, Class<?> klass, Class<?> parentClass, Class<?> proxyClass) {
    result.put("class", klass.getCanonicalName());
    result.put("parentClass", parentClass.getCanonicalName());
    if (proxyClass != null) {
      result.put("proxyClass", proxyClass.getCanonicalName());
    }
    API parentAnnotation = parentClass.getAnnotation(API.class);
    API annotation = klass.getAnnotation(API.class);

    if (parentAnnotation == null) {
      if (annotation == null) {
        throw new RuntimeException("Accessor " + klass.getCanonicalName() + " or parent class" + parentClass.getCanonicalName() + " must be annotated with API");
      } else {
        parentAnnotation = annotation;
      }
    }

    Description classDescription = merge(parentAnnotation, annotation);
    classDescription.describe(result);
  }

  private Map<String, MethodDefinition> findDeclaredMethods(Class<?> accessorClass, Set<String> strings) {
    Map<String, MethodDefinition> result = new LinkedHashMap<>();

    Arrays.stream(accessorClass.getDeclaredMethods())
        .filter(method -> strings.contains(method.getName()))
        .filter(method -> Modifier.isPublic(method.getModifiers()))
        .filter(method -> method.getReturnType() == AccessorResponse.class)
        .map(method -> MethodDefinition.builder()
            .name(method.getName())
            .annotation(method.getAnnotation(API.class))
            .method(method)
            .build())
        .collect(Collectors.toList()).forEach(definition -> {
          result.put(definition.name, definition);
        });

    return result;
  }

  private Map<String, MethodDefinition> getAnnotatedMethods(Class<?> accessorClass) {
    Map<String, MethodDefinition> result = new LinkedHashMap<>();

    Annotations.methodsWithAnnotation(API.class, accessorClass).stream()
        .filter(method -> method.getReturnType() == AccessorResponse.class)
        .filter(method -> Modifier.isPublic(method.getModifiers()))
        .map(method -> {
          API annotation = method.getAnnotation(API.class);
          return MethodDefinition.builder()
              .name(method.getName())
              .annotation(annotation)
              .method(method)
              .build();
        })
        .filter(definition -> Objects.nonNull(definition.annotation))
        .forEach(definition -> {
          result.put(definition.name, definition);
        });

    return result;
  }

  private Description merge(API parent, API child) {
    Description result = new Description();

    if (child == null) {
      result.setDescription(parent.description());
      result.setNotes(parent.notes());
      result.setSpecification(parent.specificationUrl());
    } else {
      result.setDescription(stringWithDefault(child.description(), parent.description()));
      result.setNotes(stringWithDefault(child.notes(), parent.notes()));
      result.setSpecification(stringWithDefault(child.specificationUrl(), parent.specificationUrl()));
    }

    return result;
  }

  private String stringWithDefault(String str, String def) {
    if (Strings.isNotBlank(str)) {
      return str;
    }
    return def;
  }

}
