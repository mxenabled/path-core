package com.mx.path.core.common.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Annotation feature.
 */
public class Annotations {

  /**
   * Field annotation.
   *
   * @param <T> type of annotation
   */
  public static class AnnotatedField<T extends Annotation> {

    private T annotation;
    private Class<?> elementType;
    private Field field;

    /**
     * Set annotation with type T.
     *
     * @param annotation annotation to set
     */
    public final void setAnnotation(T annotation) {
      this.annotation = annotation;
    }

    /**
     * Return annotation.
     *
     * @return annotation
     */
    public final T getAnnotation() {
      return annotation;
    }

    /**
     * Set type of annotated element.
     *
     * @param elementType element type to set
     */
    public final void setElementType(Type elementType) {
      this.elementType = (Class<?>) elementType;
    }

    /**
     * Set type of annotated element.
     *
     * @param elementType element type to set
     */
    public final void setElementType(Class<?> elementType) {
      this.elementType = elementType;
    }

    /**
     * Return type of annotated element.
     *
     * @return element type
     */
    public final Class<?> getElementType() {
      return elementType;
    }

    /**
     * Set field annotated.
     *
     * @param field field to set
     */
    public final void setField(Field field) {
      this.field = field;
    }

    /**
     * Get field annotated.
     *
     * @return field
     */
    public final Field getField() {
      return field;
    }

  }

  /**
   * Field with multiple annotations.
   */
  public static class FieldWithAnnotations {

    private final Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
    private Class<?> elementType;
    private Field field;

    /**
     * Set type of annotated element.
     *
     * @param elementType element type to set
     */
    public final void setElementType(Class<?> elementType) {
      this.elementType = elementType;
    }

    /**
     * Return type of annotated element.
     *
     * @return element type
     */
    public final Class<?> getElementType() {
      return elementType;
    }

    /**
     * Set annotated field.
     *
     * @param field field to set
     */
    public final void setField(Field field) {
      this.field = field;
    }

    /**
     * Return annotated field.
     *
     * @return field
     */
    public final Field getField() {
      return field;
    }

    /**
     * Convert to annotatedField with given annotation type.
     *
     * @param annotationType annotation type to convert
     * @param <T> type of annotation
     * @return annotation
     */
    public final <T extends Annotation> AnnotatedField<T> asAnnotatedField(Class<T> annotationType) {
      AnnotatedField<T> annotatedField = new AnnotatedField<>();
      annotatedField.setAnnotation(getAnnotation(annotationType));
      annotatedField.setField(getField());
      annotatedField.setElementType(getElementType());

      return annotatedField;
    }

    /**
     * Add new annotation.
     *
     * @param annotation annotation to add
     */
    public final void putAnnotation(Annotation annotation) {
      this.annotations.put(resolveAnnotationType(annotation), annotation);
    }

    /**
     * Return annotation with specified type.
     *
     * @param annotationType type of annotation
     * @param <T> type of annotation
     * @return annotation
     */
    @SuppressWarnings("unchecked")
    public final <T extends Annotation> T getAnnotation(Class<T> annotationType) {
      return (T) annotations.get(annotationType);
    }

  }

  /**
   * Parameter annotation.
   *
   * @param <T> type of annotation
   */
  public static class AnnotatedParameter<T extends Annotation> {

    private T annotation;
    private Parameter parameter;
    private int position;

    /**
     * Return annotation.
     *
     * @return annotation
     */
    public final T getAnnotation() {
      return annotation;
    }

    /**
     * Set annotation.
     *
     * @param annotation annotation to set
     */
    public final void setAnnotation(T annotation) {
      this.annotation = annotation;
    }

    /**
     * Return annotated parameter.
     *
     * @return parameter
     */
    public final Parameter getParameter() {
      return parameter;
    }

    /**
     * Set annotated parameter.
     *
     * @param parameter parameter to set
     */
    public final void setParameter(Parameter parameter) {
      this.parameter = parameter;
    }

    /**
     * Return annotation position.
     *
     * @return position
     */
    public final int getPosition() {
      return position;
    }

    /**
     * Set annotation position.
     *
     * @param position position to set
     */
    public final void setPosition(int position) {
      this.position = position;
    }
  }

  /**
   * Get all fields with their annotations. This is deprecated as the name indicates filtering.
   *
   * @param klass klass
   * @return a list of {@link FieldWithAnnotations} objects
   * @deprecated Use {@link Annotations#fieldsAndAnnotations(Class)}
   */
  @Deprecated
  public static List<FieldWithAnnotations> fieldsWithAnnotations(Class<?> klass) {
    return fieldsAndAnnotations(klass);
  }

  /**
   * Get all fields with their annotations.
   *
   * @param klass klass
   * @return List of {@link FieldWithAnnotations}
   */
  public static List<FieldWithAnnotations> fieldsAndAnnotations(Class<?> klass) {
    return getAllFields(klass).stream()
        .map(field -> {
          FieldWithAnnotations fieldWithAnnotations = new FieldWithAnnotations();
          fieldWithAnnotations.setField(field);

          for (Annotation annotation : field.getAnnotations()) {
            fieldWithAnnotations.putAnnotation(annotation);
          }

          return fieldWithAnnotations;
        })
        .filter(annotatedField -> !annotatedField.getField().getName().startsWith("$"))
        .collect(Collectors.toList());
  }

  /**
   * Find all methods in given class with given annotation.
   *
   * @param annotation annotation to search for
   * @param klass klass to search on
   * @param <T> type of annotation
   * @return list of annotation
   */
  public static <T extends Annotation> List<Method> methodsWithAnnotation(Class<T> annotation, Class<?> klass) {
    return Arrays.stream(klass.getMethods()).filter(method -> method.getAnnotation(annotation) != null).collect(Collectors.toList());
  }

  /**
   * Get only fields with given annotation.
   *
   * @param annotationType annotation type
   * @param klass klass to search on
   * @param <T> annotationType
   * @return List of AnnotatedField
   */
  public static <T extends Annotation> List<AnnotatedField<T>> fieldsWithAnnotation(Class<T> annotationType, Class<?> klass) {
    return getAllFields(klass).stream()
        .map(field -> {
          AnnotatedField<T> annotatedField = new AnnotatedField<>();
          annotatedField.setAnnotation(field.getAnnotation(annotationType));
          annotatedField.setField(field);

          return annotatedField;
        })
        .filter(annotatedField -> annotatedField.getAnnotation() != null)
        .collect(Collectors.toList());
  }

  /**
   * Check if klass has annotation.
   *
   * @param klass class to check
   * @param annotation annotation searching for
   * @return true, if the annotation is found on klass
   * @param <T> type of annotation
   */
  public static <T extends Annotation> boolean hasAnnotation(Class<?> klass, Class<T> annotation) {
    return klass.getAnnotation(annotation) != null;
  }

  /**
   * Get only constructor parameters with given annotation.
   *
   * @param annotationType type of annotation to search for
   * @param constructor constructor to search on
   * @param <T> annotationType
   * @return list of constructors
   */
  public static <T extends Annotation> List<AnnotatedParameter<T>> parametersWithAnnotation(Class<T> annotationType, Constructor<?> constructor) {
    AtomicInteger position = new AtomicInteger();

    return Arrays.stream(constructor.getParameters())
        .map(parameter -> {
          AnnotatedParameter<T> annotatedField = new AnnotatedParameter<>();
          annotatedField.setAnnotation(parameter.getAnnotation(annotationType));
          annotatedField.setParameter(parameter);
          annotatedField.setPosition(position.getAndIncrement());

          return annotatedField;
        })
        .filter(annotatedField -> annotatedField.getAnnotation() != null)
        .collect(Collectors.toList());
  }

  /**
   * Resolve annotation type for given Annotation. These are sometimes wrapped in a Proxy. This resolve the true class.
   *
   * @param annotation annotation to get type
   * @return annotation type
   */
  public static Class<? extends Annotation> resolveAnnotationType(Annotation annotation) {
    if (annotation instanceof Proxy) {
      return annotation.annotationType();
    } else {
      return annotation.getClass();
    }
  }

  private static List<Field> getAllFields(Class<?> klass) {
    List<Field> fields = new ArrayList<>(Arrays.asList(klass.getDeclaredFields()));

    if (klass.getSuperclass() != null) {
      fields.addAll(getAllFields(klass.getSuperclass()));
    }

    return fields;
  }
}
