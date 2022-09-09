package com.mx.path.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

import com.mx.accessors.Accessor;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayBaseClass;
import com.mx.common.lang.Strings;
import com.mx.path.api.reporting.ClassGenerationException;

public class GatewayClassElement {
  private final List<ApiMethod> methods = new ArrayList<>();
  private final List<Field> fields = new ArrayList<>();
  private final String simpleName;
  private final String basePackage;
  private final Class<?> target;
  private final TypeElement baseClass;
  private final GatewayBaseClass annotation;
  private final String targetBasePackage;
  private final String accessorFollow;
  private boolean rootGateway = false;

  public GatewayClassElement(GatewayClassElement parent, Class<?> target, String accessorFollow) {
    this.target = target;
    this.basePackage = parent.basePackage;
    this.baseClass = parent.baseClass;
    this.simpleName = target.getSimpleName().replace("BaseAccessor", "Gateway");
    this.annotation = parent.annotation;
    this.targetBasePackage = parent.targetBasePackage;
    if (Strings.isNotBlank(accessorFollow)) {
      this.accessorFollow = parent.accessorFollow + "." + accessorFollow;
    } else {
      this.accessorFollow = parent.accessorFollow;
    }

    setFields(target);
    setMethods(target);
    validateClassStructure();
  }

  public GatewayClassElement(TypeElement baseClassElement) throws IllegalArgumentException {
    this.baseClass = baseClassElement;
    this.annotation = baseClassElement.getAnnotation(GatewayBaseClass.class);
    this.target = calculateTargetClass(annotation);
    this.targetBasePackage = target.getPackage().getName();
    this.accessorFollow = "";

    validateAnnotation(annotation);
    setFields(target);
    setMethods(target);

    this.simpleName = annotation.className();
    this.basePackage = calculatePackageName(annotation.namespace() + "." + annotation.className(), simpleName);
    validateClassStructure();
  }

  public final GatewayBaseClass getAnnotation() {
    return annotation;
  }

  public final List<ApiMethod> getMethods() {
    return methods;
  }

  public final TypeElement getBaseClass() {
    return baseClass;
  }

  public final Class<?> getTarget() {
    return target;
  }

  public final String getBasePackage() {
    return basePackage;
  }

  public final String getAccessorFollow() {
    return accessorFollow;
  }

  public final String getPackage() {
    return chomp(this.basePackage + target.getPackage().getName().replace(targetBasePackage, ""), '.');
  }

  public final String getQualifiedName() {
    return getPackage() + "." + simpleName;
  }

  public final String getSimpleName() {
    return simpleName;
  }

  public final List<Field> getFields() {
    return fields;
  }

  public final boolean isRootGateway() {
    return this.rootGateway;
  }

  public final void setRootGateway(boolean rootGateway) {
    this.rootGateway = rootGateway;
  }

  private String calculatePackageName(String qualifiedClassName, String simpleClassName) {
    String pakage = qualifiedClassName.replace(simpleClassName, "");
    pakage = chomp(pakage, '.');

    return pakage;
  }

  private String chomp(String pakage, char toChomp) {
    while (pakage.endsWith(String.valueOf(toChomp))) {
      pakage = pakage.substring(0, pakage.length() - 1);
    }

    return pakage;
  }

  private void setFields(Class<?> targetClass) {
    Field[] declaredFields = targetClass.getDeclaredFields();
    List<Field> annotatedFields = Arrays.stream(declaredFields).filter(f -> {
      Annotation[] annotations = f.getAnnotationsByType(GatewayAPI.class);
      return annotations.length > 0;
    }).collect(Collectors.toList());

    fields.addAll(annotatedFields);
  }

  private void validateAnnotation(GatewayBaseClass gatewayBaseClassAnnotation) {
    // This is a nice-to-have validation on the GatewayBaseClass annotation.
    // todo: Figure out how to check this. Getting a TypeMirror error. Has something to do with rearranging models.
    // if (!Accessor.class.isAssignableFrom(gatewayBaseClassAnnotation.target())) {
    //   throw new IllegalArgumentException("GatewayBaseClass accessorClass must extend " + Accessor.class.getCanonicalName());
    // }
    if (Strings.isBlank(gatewayBaseClassAnnotation.namespace()) || Strings.isBlank(gatewayBaseClassAnnotation.className())) {
      throw new IllegalArgumentException("GatewayBaseClass namespace and className are required");
    }
  }

  private void setMethods(Class<?> targetClass) {
    Method[] declaredMethods = targetClass.getDeclaredMethods();

    List<ApiMethod> filteredMethods = Arrays.stream(declaredMethods)
        .filter(method -> {
          GatewayAPI gatewayAPIAnnotation = method.getDeclaredAnnotation(GatewayAPI.class);
          return gatewayAPIAnnotation != null;
        })
        .map(ApiMethod::new)
        .collect(Collectors.toList());

    methods.addAll(filteredMethods);
  }

  private Class<?> calculateTargetClass(GatewayBaseClass gatewayBaseClassAnnotation) {
    Class<?> targetClass;

    try {
      targetClass = gatewayBaseClassAnnotation.target();
    } catch (MirroredTypeException mte) {
      DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
      TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
      try {
        targetClass = Class.forName(classTypeElement.getQualifiedName().toString());
      } catch (ClassNotFoundException e) {
        throw new IllegalArgumentException("target class must be compiled. Move it to a compiled dependency and include in this project. (" + classTypeElement.getQualifiedName().toString() + ")", e);
      }
    }

    return targetClass;
  }

  /**
   * Scans the fields and methods of the class to ensure the Gateway generator will be able to successful generate
   * a Gateway from this class. If an error is found an actionable exception is thrown.
   */
  private void validateClassStructure() {
    AnsiWrapper ansi = new AnsiWrapper();

    // Verify that all sub-accessor fields have a @GatewayAPI annotation.
    List<Method> accessorProviderMethods = Arrays.stream(target.getDeclaredMethods())
        .filter(method -> !method.isAnnotationPresent(GatewayAPI.class) && !method.getName().startsWith("get"))
        .filter(method -> Accessor.class.isAssignableFrom(method.getReturnType()))
        .collect(Collectors.toList());

    for (Method method : accessorProviderMethods) {
      if (fields.stream().noneMatch(field -> method.getReturnType().equals(field.getType()))) {
        throw new ClassGenerationException()
            .withClassName(target.getName())
            .withHumanReadableError("No " + ansi.yellow("@GatewayAPI") + " annotation was found for field " + ansi.yellow(method.getName()) + ", but a getter method (" + ansi.yellow(method.getName()) + "()) was found.")
            .withFixInstructions("Annotate the field " + ansi.yellow(method.getName()) + " with " + ansi.yellow("@GatewayAPI") + ".");
      }
    }

    // Verify that all sub-accessor fields have setters.
    List<Method> accessorSetterMethods = Arrays.stream(target.getDeclaredMethods())
        .filter(method -> !method.isAnnotationPresent(GatewayAPI.class))
        .filter(method -> method.getParameters().length == 1 && Accessor.class.isAssignableFrom(method.getParameters()[0].getType()))
        .collect(Collectors.toList());

    for (Field field : fields) {
      if (accessorSetterMethods.stream().noneMatch(method -> method.getParameters()[0].getType().equals(field.getType()))) {
        throw new ClassGenerationException()
            .withClassName(target.getName())
            .withHumanReadableError("No setter method was found for field " + ansi.yellow(field.getName()) + " of type " + ansi.yellow(field.getType().getName()) + ".")
            .withFixInstructions("Add a setter method (annotated with the " + ansi.yellow("@API") + " annotation) named " + ansi.yellow("set") + ansi.yellow(field.getName().substring(0, 1).toUpperCase(Locale.ROOT)) + ansi.yellow(field.getName().substring(1)) + "(...) that sets the " + ansi.yellow(field.getName()) + " field.");
      }
    }
  }

}
