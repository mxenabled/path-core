package com.mx.path.core.common.reflection;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.mx.path.core.common.configuration.ConfigurationException;
import com.mx.path.core.common.lang.Durations;

/**
 * Field access utilities
 */
public class Fields {

  /**
   * Get a field's value
   * @param field
   * @param obj
   * @return
   */
  public static Object getFieldValue(Field field, Object obj) {
    boolean originalAccessibility = field.isAccessible();
    field.setAccessible(true);
    try {
      try {
        return field.get(obj);
      } finally {
        field.setAccessible(originalAccessibility);
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Can't get field value " + field.getName(), e);
    }
  }

  /**
   * Get a field's value by name
   * @param fieldName
   * @param obj
   * @return
   */
  public static Object getFieldValue(String fieldName, Object obj) {
    Field field = Arrays.stream(obj.getClass().getDeclaredFields()).filter(f -> Objects.equals(f.getName(), fieldName)).findFirst().orElse(null);
    if (field != null) {
      return getFieldValue(field, obj);
    } else {
      throw new RuntimeException("Can't find field " + fieldName + " on " + obj.getClass().getCanonicalName());
    }
  }

  /**
   * Set a field's value
   * @param field
   * @param obj
   * @param val
   */
  public static void setFieldValue(Field field, Object obj, Object val) {
    try {
      boolean originalAccessibility = field.isAccessible();
      field.setAccessible(true);
      try {
        field.set(obj, coerceValueType(field.getType(), val));
      } finally {
        field.setAccessible(originalAccessibility);
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Can't set field value " + field.getName(), e);
    }
  }

  /**
   * Set a field's value by name
   * @param fieldName
   * @param obj
   * @param val
   */
  public static void setFieldValue(String fieldName, Object obj, Object val) {
    Field field = Arrays.stream(obj.getClass().getDeclaredFields()).filter(f -> Objects.equals(f.getName(), fieldName)).findFirst().orElse(null);
    if (field != null) {
      setFieldValue(field, obj, val);
    } else {
      throw new RuntimeException("Can't find field " + fieldName + " on " + obj.getClass().getCanonicalName());
    }
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  private static Object coerceValueType(Class<?> targetType, Object value) {
    if (value == null) {
      return null;
    }

    if (targetType == int.class || targetType == Integer.class) {
      return Integer.valueOf(value.toString().trim());
    }

    if (targetType == float.class || targetType == Float.class) {
      return Float.valueOf(value.toString().trim());
    }

    if (targetType == double.class || targetType == Double.class) {
      return Double.valueOf(value.toString().trim());
    }

    if (targetType == long.class || targetType == Long.class) {
      return Long.valueOf(value.toString().trim());
    }

    if (targetType == short.class || targetType == Short.class) {
      return coerceToShort(value);
    }

    if (targetType == byte.class || targetType == Byte.class) {
      return coerceToByte(value);
    }

    if (targetType == String.class) {
      return value.toString();
    }

    if (targetType == Duration.class) {
      return coerceToDuration(value);
    }

    if (targetType == Pattern.class) {
      return coerceToPattern(value);
    }

    if (targetType.isEnum()) {
      return coerceToEnum(targetType, value);
    }

    return value;
  }

  private static Byte coerceToByte(Object value) {
    try {
      return Byte.parseByte(value.toString());
    } catch (NumberFormatException e) {
      throw new ConfigurationException("Invalid Byte value - " + value.toString(), e);
    }
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  private static Duration coerceToDuration(Object value) {
    if (value.getClass() != String.class) {
      throw new ConfigurationException("Duration value must be a string");
    }

    return Durations.fromCompactString(value.toString());
  }

  @SuppressWarnings({ "unchecked" })
  private static Enum<? extends Enum<?>> coerceToEnum(Class<?> targetType, Object value) {
    String valueStr = value.toString().trim();
    EnumSet<? extends Enum<?>> enumValues = EnumSet.allOf((Class<? extends Enum>) targetType);
    return enumValues.stream()
        .filter((enumValue) -> valueStr.equalsIgnoreCase(enumValue.toString()) || valueStr.equalsIgnoreCase(enumValue.name()))
        .findFirst()
        .orElseThrow(() -> new ConfigurationException("Invalid value " + valueStr + " for enumeration " + targetType.getName()));
  }

  /**
   * Parses String into a {@link Pattern}
   *
   * <p>To specify flags, use the Java embedded flag expression in the regular expression string.
   * See https://docs.oracle.com/javase/tutorial/essential/regex/pattern.html for details.
   *
   * @param value regular expression
   * @return Pattern representing provided regular expression
   */
  private static Pattern coerceToPattern(Object value) {
    String valueStr = value.toString().trim();

    try {
      return Pattern.compile(valueStr);
    } catch (PatternSyntaxException e) {
      throw new ConfigurationException("Invalid regular expression - " + valueStr, e);
    }
  }

  private static Short coerceToShort(Object value) {
    try {
      return Short.parseShort(value.toString());
    } catch (NumberFormatException e) {
      throw new ConfigurationException("Invalid Short value - " + value.toString(), e);
    }
  }
}
