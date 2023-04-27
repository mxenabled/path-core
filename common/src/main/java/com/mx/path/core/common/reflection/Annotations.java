package com.mx.path.core.common.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Annotations {

  public static class AnnotatedField<T extends Annotation> {

    private T annotation;
    private Class<?> elementType;
    private Field field;

    public final void setAnnotation(T annotation) {
      this.annotation = annotation;
    }

    public final T getAnnotation() {
      return annotation;
    }

    public final void setElementType(Type elementType) {
      this.elementType = (Class<?>) elementType;
    }

    public final void setElementType(Class<?> elementType) {
      this.elementType = elementType;
    }

    public final Class<?> getElementType() {
      return elementType;
    }

    public final void setField(Field field) {
      this.field = field;
    }

    public final Field getField() {
      return field;
    }

  }

  public static class FieldWithAnnotations {

    private final Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
    private Class<?> elementType;
    private Field field;

    public final void setElementType(Class<?> elementType) {
      this.elementType = elementType;
    }

    public final Class<?> getElementType() {
      return elementType;
    }

    public final void setField(Field field) {
      this.field = field;
    }

    public final Field getField() {
      return field;
    }

    /**
     * Convert to annotatedField with given annotation type
     * @param annotationType
     * @param <T>
     * @return
     */
    public final <T extends Annotation> AnnotatedField<T> asAnnotatedField(Class<T> annotationType) {
      AnnotatedField<T> annotatedField = new AnnotatedField<>();
      annotatedField.setAnnotation(getAnnotation(annotationType));
      annotatedField.setField(getField());
      annotatedField.setElementType(getElementType());

      return annotatedField;
    }

    public final void putAnnotation(Annotation annotation) {
      this.annotations.put(resolveAnnotationType(annotation), annotation);
    }

    @SuppressWarnings("unchecked")
    public final <T extends Annotation> T getAnnotation(Class<T> annotationType) {
      return (T) annotations.get(annotationType);
    }

  }

  public static class AnnotatedParameter<T extends Annotation> {

    private T annotation;
    private Parameter parameter;
    private int position;

    public final T getAnnotation() {
      return annotation;
    }

    public final void setAnnotation(T annotation) {
      this.annotation = annotation;
    }

    public final Parameter getParameter() {
      return parameter;
    }

    public final void setParameter(Parameter parameter) {
      this.parameter = parameter;
    }

    public final int getPosition() {
      return position;
    }

    public final void setPosition(int position) {
      this.position = position;
    }

  }

  /**
   * Get all fields with their annotations
   * @param klass
   * @return List of FieldWithAnnotation
   */
  public static List<FieldWithAnnotations> fieldsWithAnnotations(Class<?> klass) {
    return Arrays.stream(klass.getDeclaredFields())
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
   */
  public static <T extends Annotation> List<Method> methodsWithAnnotation(Class<T> annotation, Class<?> klass) {
    return Arrays.stream(klass.getMethods()).filter(method -> method.getAnnotation(annotation) != null).collect(Collectors.toList());
  }

  /**
   * Get only fields with given annotation
   * @param annotationType
   * @param klass
   * @param <T> annotationType
   * @return List of AnnotatedField
   */
  public static <T extends Annotation> List<AnnotatedField<T>> fieldsWithAnnotation(Class<T> annotationType, Class<?> klass) {
    return Arrays.stream(klass.getDeclaredFields())
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
   * @param klass class to check
   * @param annotation annotation searching for
   * @return true, if the annotation is found on klass
   * @param <T>
   */
  public static <T extends Annotation> boolean hasAnnotation(Class<?> klass, Class<T> annotation) {
    return klass.getAnnotation(annotation) != null;
  }

  /**
   * Get only constructor parameters with given annotation
   * @param annotationType
   * @param constructor
   * @param <T> annotationType
   * @return
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
   * @param annotation
   * @return
   */
  public static Class<? extends Annotation> resolveAnnotationType(Annotation annotation) {
    if (annotation instanceof Proxy) {
      return annotation.annotationType();
    } else {
      return annotation.getClass();
    }
  }
}
