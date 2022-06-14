package com.mx.path.api;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import com.google.common.collect.ImmutableMap;
import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.BaseAccessor;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.lang.Strings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import org.apache.commons.lang3.StringUtils;

/**
 * Generates a proxy wrapper for each accessor
 */
public class AccessorProxyGenerator {
  private final Filer filer;
  private CodeBlock.Builder accessorProxyMappings;

  public AccessorProxyGenerator(ProcessingEnvironment processingEnvironment) {
    this.filer = processingEnvironment.getFiler();
  }

  public final void generateAll() throws IOException {
    TypeSpec.Builder accessorProxyMap = TypeSpec.classBuilder("AccessorProxyMap").addModifiers(Modifier.PUBLIC)
        .addField(
            ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class),
                ParameterizedTypeName.get(
                    ClassName.get(Map.class),
                    ParameterizedTypeName.get(
                        ClassName.get(Class.class), WildcardTypeName.subtypeOf(Accessor.class)),
                    ParameterizedTypeName.get(
                        ClassName.get(Class.class), WildcardTypeName.subtypeOf(Accessor.class)))),
            "proxyClassMap", Modifier.STATIC, Modifier.PRIVATE)
        .addMethod(MethodSpec.methodBuilder("getProxy")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(ClassName.get(String.class), "scope")
            .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Accessor.class)), "klass")
            .returns(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Accessor.class)))
            .addCode("if (proxyClassMap.get(scope) == null) {\n")
            .addCode("throw new $T(\"Unsupported scope \" + scope);\n", RuntimeException.class)
            .addCode("}\n")
            .addStatement("return proxyClassMap.get(scope).get(klass)")
            .build());
    accessorProxyMappings = CodeBlock.builder()
        .addStatement("proxyClassMap = new $T<>()", HashMap.class)
        .addStatement("proxyClassMap.put(\"singleton\", new HashMap<>())")
        .addStatement("proxyClassMap.put(\"prototype\", new HashMap<>())");

    recursiveGenerate(BaseAccessor.class);

    accessorProxyMappings.add("\n// Freeze the scope proxy mappings\n")
        .addStatement("proxyClassMap.put(\"singleton\", $T.copyOf(proxyClassMap.get(\"singleton\")))", ImmutableMap.class)
        .addStatement("proxyClassMap.put(\"prototype\", $T.copyOf(proxyClassMap.get(\"prototype\")))", ImmutableMap.class)
        .addStatement("proxyClassMap = $T.copyOf(proxyClassMap)", ImmutableMap.class);
    accessorProxyMap.addStaticBlock(accessorProxyMappings.build());

    JavaFile javaFile = JavaFile.builder("com.mx.path.gateway.accessor.proxy", accessorProxyMap.build())
        .addFileComment("---------------------------------------------------------------------------------------------------------------------\n"
            + "  GENERATED FILE - ** Do not edit **\n"
            + "---------------------------------------------------------------------------------------------------------------------")
        .build();

    javaFile.writeTo(filer);
  }

  @SuppressWarnings("unchecked")
  private void recursiveGenerate(Class<? extends Accessor> klass) throws IOException {

    String proxyName = klass.getSimpleName() + "Proxy";
    String packageName = calculatePackageName(klass);

    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(proxyName)
        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
        .superclass(klass)
        .addSuperinterface(ClassName.get("com.mx.path.gateway.configuration", "AccessorProxy"))

        .addJavadoc(
            "Base class for wrapping " + klass.getName() + ".\n"
                + "<p>\n"
                + "Used to provide scoped construction strategies.")

        .addMethod(
            MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AccessorConfiguration.class, "configuration")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(klass)), "accessorClass")
                .addStatement("super(configuration)")
                .addStatement("this.accessorConstructionContext = new $T(accessorClass, configuration)", ParameterizedTypeName.get(ClassName.get("com.mx.path.gateway.configuration", "AccessorConstructionContext"), TypeName.get(klass)))
                .build())

        .addField(
            FieldSpec.builder(ParameterizedTypeName.get(ClassName.get("com.mx.path.gateway.configuration", "AccessorConstructionContext"), WildcardTypeName.subtypeOf(klass)), "accessorConstructionContext")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .addAnnotation(ClassName.get("lombok", "Getter"))
                .build())

        .addMethod(
            MethodSpec.methodBuilder("getAccessorClass")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(klass)))
                .addStatement("return accessorConstructionContext.getAccessorClass()")
                .build());

    /**
     * Step through the fields with types that are assignable to Accessor, and generate
     * the accessor proxies for that class.
     */
    for (Field field : klass.getDeclaredFields()) {
      Class<?> fieldClass = field.getType();
      if (Accessor.class.isAssignableFrom(fieldClass)) {
        recursiveGenerate((Class<? extends Accessor>) fieldClass);
      }
    }

    /**
     * Find all GatewayAPI methods and add them to the proxy.
     * The body will forward the call on to the result of {@code build()}
     *
     * NOTE: We are skipping the sub-accessor and letting the base class handle
     * those. They can be configured with a scope proxy.
     */
    List<Method> methods = new ArrayList<>();
    for (Method method : klass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(API.class)) {
        methods.add(method);
      }
    }

    methods.sort(Comparator.comparing(Method::getName));

    methods.forEach(method -> {
      MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.getName())
          .addModifiers(Modifier.PUBLIC)
          .addAnnotation(Override.class)
          .addJavadoc("@return " + method.getName() + " accessor")
          .returns(ClassName.get(method.getGenericReturnType()));

      List<String> parameterNames = new ArrayList<>();
      for (Parameter parameter : method.getParameters()) {
        parameterNames.add(parameter.getName());
        methodBuilder.addParameter(parameter.getType(), parameter.getName());
      }
      if (!method.isAnnotationPresent(GatewayAPI.class)) {
        methodBuilder.addCode(CodeBlock.builder()
            .add("if (get$L() != null) {\n", StringUtils.capitalize(method.getName()))
            .add("  return get$L();\n", StringUtils.capitalize(method.getName()))
            .add("}\n")
            .build());
      }
      methodBuilder.addStatement("return build()." + method.getName() + "(" + String.join(",", parameterNames) + ")");
      classBuilder.addMethod(methodBuilder.build());
    });

    classBuilder.addMethod(
        MethodSpec.methodBuilder("buildAccessor")
            .addModifiers(Modifier.PROTECTED)
            .addStatement("return accessorConstructionContext.build()")
            .addJavadoc(
                "Create an instance of Accessor type klass\n\n"
                    + "<p>Override to change how the accessor is constructed.\n"
                    + "@param klass\n"
                    + "@return Accessor")
            .returns(klass)
            .build());

    classBuilder.addMethod(
        MethodSpec.methodBuilder("build")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(klass)
            .build());

    TypeSpec annotationProxySpec = classBuilder.build();
    JavaFile javaFile = JavaFile.builder(packageName, annotationProxySpec)
        .addFileComment("---------------------------------------------------------------------------------------------------------------------\n"
            + "  GENERATED FILE - ** Do not edit **\n"
            + "  Wrapped Class: $L\n"
            + "---------------------------------------------------------------------------------------------------------------------",
            klass.getCanonicalName())
        .build();
    javaFile.writeTo(filer);

    generateSingletonProxy(proxyName, klass);
    generatePrototypeProxy(proxyName, klass);
  }

  private void generateSingletonProxy(String proxyBaseClass, Class<? extends Accessor> accessorClass) throws IOException {
    String packageName = calculatePackageName(accessorClass);
    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(proxyBaseClass + "Singleton")
        .addModifiers(Modifier.PUBLIC)
        .superclass(ClassName.get(packageName, proxyBaseClass))
        .addField(FieldSpec.builder(accessorClass, "instance", Modifier.PRIVATE).build())
        .addMethod(MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(AccessorConfiguration.class, "configuration")
            .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(accessorClass)), "accessorClass")
            .addStatement("super(configuration, accessorClass)")
            .addStatement("this.instance = buildAccessor()")
            .build())
        .addMethod(MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(AccessorConfiguration.class, "configuration")
            .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(accessorClass)), "accessorClass")
            .addParameter(ClassName.get(accessorClass), "instance")
            .addStatement("super(configuration, accessorClass)")
            .addStatement("this.instance = instance")
            .build())
        .addMethod(MethodSpec.methodBuilder("build")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(accessorClass)
            .addStatement("return instance")
            .build())
        .addMethod(MethodSpec.methodBuilder("getScope")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(String.class)
            .addStatement("return $S", "singleton")
            .build());

    TypeSpec annotationProxySpec = classBuilder.build();
    JavaFile javaFile = JavaFile.builder(packageName, annotationProxySpec)
        .addFileComment("---------------------------------------------------------------------------------------------------------------------\n"
            + "  GENERATED FILE - ** Do not edit **\n"
            + "---------------------------------------------------------------------------------------------------------------------")
        .build();
    javaFile.writeTo(filer);

    accessorProxyMappings.addStatement("proxyClassMap.get(\"singleton\").put($T.class, $T.class)", accessorClass, ClassName.get(packageName, proxyBaseClass + "Singleton"));
  }

  private void generatePrototypeProxy(String proxyBaseClass, Class<? extends Accessor> accessorClass) throws IOException {
    String packageName = calculatePackageName(accessorClass);
    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(proxyBaseClass + "Prototype")
        .addModifiers(Modifier.PUBLIC)
        .superclass(ClassName.get(packageName, proxyBaseClass))
        .addMethod(MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(AccessorConfiguration.class, "configuration")
            .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(accessorClass)), "accessorClass")
            .addStatement("super(configuration, accessorClass)")
            .build())
        .addMethod(MethodSpec.methodBuilder("build")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(accessorClass)
            .addStatement("return buildAccessor()")
            .build())
        .addMethod(MethodSpec.methodBuilder("getScope")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(String.class)
            .addStatement("return $S", "prototype")
            .build());

    TypeSpec annotationProxySpec = classBuilder.build();
    JavaFile javaFile = JavaFile.builder(packageName, annotationProxySpec)
        .addFileComment("---------------------------------------------------------------------------------------------------------------------\n"
            + "  GENERATED FILE - ** Do not edit **\n"
            + "---------------------------------------------------------------------------------------------------------------------")
        .build();
    javaFile.writeTo(filer);

    accessorProxyMappings.addStatement("proxyClassMap.get(\"prototype\").put($T.class, $T.class)", accessorClass, ClassName.get(packageName, proxyBaseClass + "Prototype"));
  }

  private String calculatePackageName(Class<? extends Accessor> accessorClass) {
    String packageClass = accessorClass.getPackage().getName().replaceAll(".*\\.accessors[.]*", "");
    if (Strings.isBlank(packageClass)) {
      return "com.mx.path.gateway.accessor.proxy";
    }
    packageClass = "com.mx.path.gateway.accessor.proxy." + packageClass;
    return packageClass;
  }

}
