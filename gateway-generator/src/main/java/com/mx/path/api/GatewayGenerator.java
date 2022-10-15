package com.mx.path.api;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

import com.mx.common.collections.ObjectMap;
import com.mx.path.gateway.configuration.RootGateway;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public class GatewayGenerator {

  private final Filer filer;
  private final ProcessingEnvironment roundEnvironment;
  private final CodeBlock accessorProxyMappingCodeBlock;

  public GatewayGenerator(ProcessingEnvironment processingEnvironment, CodeBlock accessorProxyMappingCodeBlock) {
    this.roundEnvironment = processingEnvironment;
    this.accessorProxyMappingCodeBlock = accessorProxyMappingCodeBlock;
    this.filer = processingEnvironment.getFiler();
  }

  @SuppressWarnings("MethodLength")
  public final void generate(GatewayClassElement gatewayClassElement) throws IOException {

    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(gatewayClassElement.getSimpleName())
        .addModifiers(Modifier.PUBLIC)
        .superclass(gatewayClassElement.getBaseClass().asType());

    if (gatewayClassElement.isRootGateway()) {
      classBuilder.addAnnotation(RootGateway.class);

      classBuilder.addStaticBlock(accessorProxyMappingCodeBlock);
    }

    try {
      Class clazz = gatewayClassElement.getAnnotation().annotation();

      ClassName annotationName = ClassName.bestGuess(clazz.toString());
      AnnotationSpec.Builder annotationBuilder = AnnotationSpec.builder(annotationName);
      classBuilder.addAnnotation(annotationBuilder.build());
    } catch (MirroredTypeException mte) {
      DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
      TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
      ClassName annotationName = ClassName.bestGuess(classTypeElement.getQualifiedName().toString());
      AnnotationSpec.Builder annotationBuilder = AnnotationSpec.builder(annotationName);
      classBuilder.addAnnotation(annotationBuilder.build());
    }

    classBuilder.addMethod(
        MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("super()")
            .build());

    classBuilder.addMethod(
        MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String.class, "clientId")
            .addStatement("super(clientId)")
            .build());

    MethodSpec.Builder descriptionBuilder = MethodSpec.methodBuilder("describe")
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addAnnotation(Override.class)
        .addParameter(ObjectMap.class, "description")
        .addStatement("super.describe(description)");

    if (gatewayClassElement.getFields().size() > 0) {
      descriptionBuilder.addStatement("ObjectMap gateways = description.createMap(\"gateways\")");
    }

    gatewayClassElement.getFields().forEach(field -> {

      GatewayClassElement fieldType = new GatewayClassElement(gatewayClassElement, field.getType(), "");

      String referencedGatewayName = fieldType.getQualifiedName();
      TypeElement referencedGateway = roundEnvironment.getElementUtils().getTypeElement(referencedGatewayName);

      if (referencedGateway == null) {
        throw new ContinueProcessing(referencedGatewayName);
      }

      classBuilder.addField(TypeName.get(referencedGateway.asType()), field.getName());

      descriptionBuilder.addCode("\n"
          + "if ($L != null) {\n"
          + "  gateways.put($S, $L.describe());\n"
          + "}\n", field.getName(), field.getName(), field.getName());

      MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(field.getName())
          .addModifiers(Modifier.PUBLIC)
          .addStatement("$L.setParent(this)", field.getName())
          .addStatement("return $L", field.getName())
          .returns(TypeName.get(referencedGateway.asType()));

      classBuilder.addMethod(methodBuilder.build());

    });

    gatewayClassElement.getMethods().forEach(method -> {
      List<ParameterSpec> parameters = method.getParameters().stream().map(p -> {
        return ParameterSpec.builder(p.getType(), p.getName()).build();
      }).collect(Collectors.toList());

      String parameterPasser = method.getParameters().stream().map(p -> {

        return "(" + p.getType().getSimpleName() + ") req.getParams().get(\"" + p.getName() + "\")";
      }).collect(Collectors.joining(",")).trim();

      MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.getName())
          .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
          .addParameters(parameters)
          .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unchecked").build())
          .returns(method.getGenericReturnType())
          .addStatement("$T<$T> result = new AtomicReference<>()", ClassName.get("java.util.concurrent.atomic", "AtomicReference"), method.getGenericReturnType())
          .addStatement("$T originalRequestContext = RequestContext.current()", ClassName.get("com.mx.path.model.context", "RequestContext"))
          .addStatement("$T requestContextCopy = GatewayRequestContext.fromRequestContext(originalRequestContext).toBuilder().build()", ClassName.get("com.mx.path.gateway.context", "GatewayRequestContext"))
          .addStatement("requestContextCopy.register()", ClassName.get("com.mx.path.gateway.context", "GatewayRequestContext"), ClassName.get("com.mx.path.model.context", "RequestContext"))
          .beginControlFlow("try")
          .addStatement("$T.withSelfClearing(getClientId(), (requestContext) -> {", ClassName.get("com.mx.path.gateway.context", "GatewayRequestContext"))
          .addStatement("  $T gatewayRequestContext = (GatewayRequestContext) requestContext", ClassName.get("com.mx.path.gateway.context", "GatewayRequestContext"));

      methodBuilder.addCode("  if (gatewayRequestContext.getClientId() == null) {\n");
      methodBuilder.addStatement("    gatewayRequestContext.setClientId(getClientId())");
      methodBuilder.addCode("  }\n");
      if (method.getModel() != Void.class) {
        methodBuilder.addStatement("  gatewayRequestContext.setModel($T.class)", method.getModel());
      }
      methodBuilder.addStatement("  $T accessor = getAccessor()", ClassName.get(method.getMethod().getDeclaringClass().getPackage().getName(), method.getMethod().getDeclaringClass().getSimpleName()));
      methodBuilder.addStatement("  gatewayRequestContext.setOp(\"$L\")", method.getName());
      methodBuilder.addStatement("  gatewayRequestContext.setListOp($L)", method.isListOp());
      methodBuilder.addStatement("  gatewayRequestContext.setGateway(this.root())");
      methodBuilder.addStatement("  gatewayRequestContext.setCurrentGateway(this)");
      methodBuilder.addStatement("  gatewayRequestContext.setCurrentAccessor(accessor)");

      method.getParameters().forEach(p -> {
        methodBuilder.addStatement("  gatewayRequestContext.getParams().put(\"$L\", $L)", p.getName(), p.getName());
      });

      methodBuilder.addStatement("  $T terminatingBehavior = new BlockBehavior((req) -> {", ClassName.get("com.mx.path.gateway.behavior", "BlockBehavior"))
          .addStatement("    beforeAccessor(root(), accessor, gatewayRequestContext)")
          .addCode("    try {\n")
          .addStatement("      return accessor.$L($L)", method.getName(), parameterPasser)
          .addCode("    } finally {")
          .addStatement("      afterAccessor(root(), accessor, gatewayRequestContext)")
          .addStatement("    }})");

      methodBuilder.addStatement("  result.set(($T) executeBehaviorStack($L, gatewayRequestContext, terminatingBehavior))", method.getGenericReturnType(), method.getParameterizedReturnType());

      methodBuilder.addStatement("})")
          .endControlFlow("finally {\n\tif (originalRequestContext != null) {\n\t\toriginalRequestContext.register();\n\t}\n}")
          .addStatement("return result.get()")
          .returns(method.getGenericReturnType());

      classBuilder.addMethod(methodBuilder.build());
    });

    MethodSpec.Builder accessorMethod = MethodSpec.methodBuilder("getAccessor")
        .addModifiers(Modifier.PUBLIC)
        .returns(ClassName.get(gatewayClassElement.getTarget().getPackage().getName(), gatewayClassElement.getTarget().getSimpleName()))
        .addStatement("return getBaseAccessor()$L", gatewayClassElement.getAccessorFollow());

    classBuilder.addMethod(accessorMethod.build());
    classBuilder.addMethod(descriptionBuilder.build());

    TypeSpec gatewaySpec = classBuilder.build();
    JavaFile javaFile = JavaFile.builder(gatewayClassElement.getPackage(), gatewaySpec)
        .addFileComment("---------------------------------------------------------------------------------------------------------------------\n"
            + "  GENERATED FILE - ** Do not edit **\n"
            + "  Wrapped Class: $L\n"
            + "---------------------------------------------------------------------------------------------------------------------",
            gatewayClassElement.getPackage() + "." + gatewayClassElement.getSimpleName())
        .build();

    javaFile.writeTo(filer);
  }
}
