package com.mx.path.api.remote;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.BaseAccessor;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.lang.Strings;
import com.mx.common.messaging.MessageError;
import com.mx.common.messaging.MessageStatus;
import com.mx.common.remote.RemoteOperation;
import com.mx.models.MdxList;
import com.mx.path.api.AnsiWrapper;
import com.mx.path.api.connect.messaging.MessageHeaders;
import com.mx.path.api.connect.messaging.MessageParameters;
import com.mx.path.api.connect.messaging.MessageRequest;
import com.mx.path.api.connect.messaging.MessageResponse;
import com.mx.path.api.connect.messaging.remote.MdxListOfJson;
import com.mx.path.api.connect.messaging.remote.RemoteRequester;
import com.mx.path.api.reporting.ClassGenerationException;
import com.mx.path.model.context.RequestContext;
import com.mx.path.utilities.reflection.ClassHelper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

public final class RemoteAccessorGenerator {
  @Data
  private static class SubAccessor {
    private Class<? extends Accessor> klass;
    private ClassName remoteClassName;
    private String getterMethodName;
    private String fieldName;
  }

  private final Filer filer;

  public RemoteAccessorGenerator(ProcessingEnvironment processingEnvironment) {
    this.filer = processingEnvironment.getFiler();
  }

  public void generate() {
    generateRemoteAccessor(BaseAccessor.class);
  }

  private void generateRemoteAccessor(Class<? extends Accessor> accessorClass) {
    // We need to process any sub accessors this accessor contains.
    getDeclaredSubAccessors(accessorClass).forEach(subAccessor -> generateRemoteAccessor(subAccessor.getKlass()));
    buildRemoteAccessor(accessorClass);
  }

  @SuppressWarnings("unchecked")
  private List<SubAccessor> getDeclaredSubAccessors(Class<? extends Accessor> accessorClass) {
    List<SubAccessor> subAccessors = Arrays.stream(accessorClass.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(GatewayAPI.class) && Accessor.class.isAssignableFrom(field.getType()))
        .map(field -> {
          SubAccessor subAccessor = new SubAccessor();
          subAccessor.setKlass((Class<? extends Accessor>) field.getType());
          subAccessor.setFieldName(field.getName());
          subAccessor.setRemoteClassName(ClassName.bestGuess(calculatePackageName(subAccessor.getKlass()) + "." + calculateClassName(subAccessor.getKlass())));
          return subAccessor;
        })
        .collect(Collectors.toList());

    Arrays.stream(accessorClass.getMethods()).forEach(method -> {
      subAccessors.stream()
          .filter(subAccessor -> method.getReturnType().equals(subAccessor.getKlass()))
          .findFirst()
          .ifPresent(associatedSubAccessor -> associatedSubAccessor.setGetterMethodName(method.getName()));
    });
    return subAccessors;
  }

  private List<Method> getDeclaredAccessorMethods(Class<? extends Accessor> accessorClass) {
    return Arrays.stream(accessorClass.getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(GatewayAPI.class) && method.getReturnType().isAssignableFrom(AccessorResponse.class))
        .collect(Collectors.toList());
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  private void buildRemoteAccessor(Class<? extends Accessor> accessorClass) {
    String remoteAccessorName = calculateClassName(accessorClass);
    String packageName = calculatePackageName(accessorClass);

    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(remoteAccessorName)
        .addModifiers(Modifier.PUBLIC)
        .superclass(ParameterizedTypeName.get(RemoteRequester.class, accessorClass))
        .addJavadoc("Utility class that mirrors the {@link " + accessorClass.getCanonicalName() + "} API and handles remote communication.");

    List<SubAccessor> subAccessors = getDeclaredSubAccessors(accessorClass);

    // Add all sub-accessor fields
    for (SubAccessor subAccessor : subAccessors) {
      classBuilder.addField(
          FieldSpec.builder(subAccessor.getRemoteClassName(), subAccessor.getFieldName())
              .addModifiers(Modifier.PRIVATE)
              .addAnnotation(Setter.class)
              .addAnnotation(NonNull.class)
              .initializer("new $T()", subAccessor.getRemoteClassName())
              .build());
    }

    // Add all sub-accessor getters
    for (SubAccessor subAccessor : subAccessors) {
      if (subAccessor.getGetterMethodName() == null) {
        AnsiWrapper ansi = new AnsiWrapper();
        throw new ClassGenerationException()
            .withClassName(accessorClass.getName())
            .withHumanReadableError("No getter method was found for field " + ansi.yellow(subAccessor.getFieldName()) + " of type " + ansi.yellow(subAccessor.getKlass().getName()) + ".")
            .withFixInstructions("Add a getter method (annotated with the " + ansi.yellow("@API") + " annotation) named " + ansi.yellow(subAccessor.getFieldName()) + "() that returns the " + ansi.yellow(subAccessor.getFieldName()) + " field.");
      }

      classBuilder.addMethod(
          MethodSpec.methodBuilder(subAccessor.getGetterMethodName())
              .addModifiers(Modifier.PUBLIC)
              .returns(subAccessor.getRemoteClassName())
              .addStatement("return $L", subAccessor.getFieldName())
              .build());
    }

    // Add accessor methods
    for (Method method : getDeclaredAccessorMethods(accessorClass)) {
      MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(method.getName())
          .addModifiers(Modifier.PUBLIC)
          .returns(method.getGenericReturnType());

      // add method parameters
      for (Parameter parameter : method.getParameters()) {
        methodSpecBuilder.addParameter(ParameterSpec.builder(parameter.getType(), parameter.getName()).build());
      }

      methodSpecBuilder.addStatement("$T messageParameters = new MessageParameters()", MessageParameters.class);

      // Marshal parameters into MessageRequest
      List<Parameter> stringOrPrimitiveParameters = Arrays.stream(method.getParameters())
          .filter(parameter -> parameter.getType().isAssignableFrom(String.class) || parameter.getType().isPrimitive())
          .collect(Collectors.toList());

      List<Parameter> objectParameters = Arrays.stream(method.getParameters())
          .filter(parameter -> !parameter.getType().isAssignableFrom(String.class) && !parameter.getType().isPrimitive())
          .collect(Collectors.toList());

      if (objectParameters.size() > 1) {
        throw new RuntimeException("An accessor method should only receive one payload per REST conventions");
      }

      for (Parameter parameter : stringOrPrimitiveParameters) {
        if (parameter.getType().isAssignableFrom(String.class)) {
          methodSpecBuilder.addStatement("messageParameters.put($S, " + parameter.getName() + ")", parameter.getName());
        } else {
          methodSpecBuilder.addStatement("messageParameters.put($S, String.valueOf(" + parameter.getName() + "))", parameter.getName());
        }
      }

      methodSpecBuilder.addStatement("$T messageRequest = new MessageRequest()", MessageRequest.class);
      methodSpecBuilder.addStatement("messageRequest.setMessageHeaders(new $T())", MessageHeaders.class);
      methodSpecBuilder.addStatement("messageRequest.setMessageParameters(messageParameters)");

      String operationName = method.getName();
      if (method.isAnnotationPresent(RemoteOperation.class)) {
        operationName = method.getAnnotation(RemoteOperation.class).value();
      }
      methodSpecBuilder.addStatement("messageRequest.setOperation($S)", operationName);

      if (objectParameters.size() != 0) {
        methodSpecBuilder.addStatement("messageRequest.setBody(" + objectParameters.get(0).getName() + ")");
      }

      // execute remote request
      methodSpecBuilder.addStatement("$T messageResponse = executeRequest($T.current().getClientId(), messageRequest)", MessageResponse.class, RequestContext.class);

      // handle response
      methodSpecBuilder.beginControlFlow("if (messageResponse.getStatus() != $T.$L)", MessageStatus.class, MessageStatus.SUCCESS);
      methodSpecBuilder.addStatement("throw new $T(\"Unable to call remote " + method.getName() + " with status \" + messageResponse.getStatus(), messageResponse.getStatus(), messageResponse.getException())", MessageError.class);
      methodSpecBuilder.endControlFlow();

      // return response
      List<Type> types = new ClassHelper().resolveParameterizedMethodReturnTypes(method);
      Type accessorResponseType = types.get(0);
      if (accessorResponseType instanceof ParameterizedType) {
        List<Type> innerType = new ClassHelper().resolveParameterizedTypes(accessorResponseType);
        methodSpecBuilder.addStatement("return new $T<$T<$T>>().withResult(messageResponse.getBodyAs(new $T<>($T.class)))", AccessorResponse.class, MdxList.class, innerType.get(0), MdxListOfJson.class, innerType.get(0));
      } else {
        methodSpecBuilder.addStatement("return new $T<$T>().withResult(messageResponse.getBodyAs($T.class))", AccessorResponse.class, accessorResponseType, accessorResponseType);
      }

      classBuilder.addMethod(methodSpecBuilder.build());
    }

    TypeSpec remoteAccessorSpec = classBuilder.build();
    JavaFile javaFile = JavaFile.builder(packageName, remoteAccessorSpec)
        .addFileComment("---------------------------------------------------------------------------------------------------------------------\n"
            + "  GENERATED FILE - ** Do not edit **\n"
            + "  Wrapped Class: $L\n"
            + "---------------------------------------------------------------------------------------------------------------------",
            accessorClass.getCanonicalName())
        .build();
    try {
      javaFile.writeTo(filer);
    } catch (IOException e) {
      throw new RuntimeException("Unable to write generated file for: " + packageName + "." + remoteAccessorName);
    }
  }

  private String calculateClassName(Class<? extends Accessor> accessorClass) {
    return "Remote" + accessorClass.getSimpleName().replace("Base", "");
  }

  private String calculatePackageName(Class<? extends Accessor> accessorClass) {
    String packageClass = accessorClass.getPackage().getName().replaceAll(".*\\.accessors[.]*", "");
    if (Strings.isBlank(packageClass)) {
      return "com.mx.path.gateway.accessor.remote";
    }
    packageClass = "com.mx.path.gateway.accessor.remote." + packageClass;
    return packageClass;
  }
}
