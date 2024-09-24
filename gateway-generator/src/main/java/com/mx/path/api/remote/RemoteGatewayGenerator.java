package com.mx.path.api.remote;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.api.AnsiWrapper;
import com.mx.path.api.GatewayClassElement;
import com.mx.path.api.reporting.ClassGenerationException;
import com.mx.path.connect.messaging.MessageRequest;
import com.mx.path.connect.messaging.MessageResponse;
import com.mx.path.connect.messaging.remote.RemoteService;
import com.mx.path.connect.messaging.remote.Responder;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.messaging.MessageError;
import com.mx.path.core.common.messaging.MessageStatus;
import com.mx.path.core.common.remote.RemoteOperation;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.lang3.StringUtils;

/**
 * Generate remote gateway classes.
 */
public class RemoteGatewayGenerator {
  private final Filer filer;

  /**
   * Build {@link RemoteGatewayGenerator} with the provided processing environment.
   *
   * @param processingEnvironment environment used during annotation processing.
   */
  public RemoteGatewayGenerator(ProcessingEnvironment processingEnvironment) {
    this.filer = processingEnvironment.getFiler();
  }

  /**
   * Generates a remote class that extends RemoteService for the specified gateway class element.
   *
   * @param gatewayClassElement gateway class element to wrap and generate a remote class for
   * @throws IOException to be thrown
   */

  public final void generate(GatewayClassElement gatewayClassElement) throws IOException {
    String className = "Remote" + gatewayClassElement.getSimpleName();
    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
        .addModifiers(Modifier.PUBLIC)
        .superclass(ParameterizedTypeName.get(RemoteService.class, gatewayClassElement.getTarget()));

    String gatewayName = StringUtils.uncapitalize(gatewayClassElement.getSimpleName());
    ClassName gatewayClass = ClassName.bestGuess(gatewayClassElement.getPackage() + "." + gatewayClassElement.getSimpleName());
    classBuilder.addField(
        FieldSpec.builder(gatewayClass, gatewayName)
            .addModifiers(Modifier.PRIVATE)
            .addAnnotation(Getter.class)
            .addAnnotation(Setter.class)
            .build());

    classBuilder.addField(
        FieldSpec.builder(ObjectMap.class, "configurations")
            .addModifiers(Modifier.PRIVATE)
            .addAnnotation(Getter.class)
            .addAnnotation(Setter.class)
            .build());

    classBuilder.addMethod(
        MethodSpec.constructorBuilder()
            .addParameter(String.class, "clientId")
            .addParameter(gatewayClass, gatewayName)
            .addParameter(ObjectMap.class, "configurations")
            .addModifiers(Modifier.PUBLIC)
            .addStatement("super(clientId)")
            .addStatement("this." + gatewayName + " = " + gatewayName)
            .addStatement("this.configurations = configurations")
            .build());

    AnsiWrapper ansi = new AnsiWrapper();
    Set<String> apiMethodSet = new HashSet<>();
    gatewayClassElement.getMethods().forEach(apiMethod -> {
      String apiMethodName = apiMethod.getName();

      if (apiMethod.getMethod().isAnnotationPresent(RemoteOperation.class)) {
        apiMethodName = apiMethod.getMethod().getAnnotation(RemoteOperation.class).value();
      }

      if (apiMethodSet.contains(apiMethodName)) {
        throw new ClassGenerationException()
            .withClassName(gatewayClass.canonicalName())
            .withHumanReadableError("Duplicate method '" + ansi.yellow(apiMethodName) + "' found for " + gatewayClassElement.getSimpleName() + ".")
            .withFixInstructions("Overloaded methods must be annotated with " + ansi.yellow("@RemoteOperation(\"" + ansi.green("someOperationName") + "\"" + ansi.yellow(")") + ". Please remember to choose a descriptive name."));
      }

      apiMethodSet.add(apiMethodName);

      MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(apiMethodName)
          .addAnnotation(Responder.class)
          .addModifiers(Modifier.PUBLIC)
          .addParameter(String.class, "channel")
          .addParameter(MessageRequest.class, "request")
          .returns(MessageResponse.class)
          .beginControlFlow("if (configurations.getMap(\"" + apiMethodName + "\") == null)")
          .addStatement(
              "throw new $T(\"No responder registered for " + apiMethodName + "\", $T.$L, null)",
              MessageError.class,
              MessageStatus.class,
              MessageStatus.NO_RESPONDER)
          .endControlFlow()
          .beginControlFlow("if (configurations.getMap(\"" + apiMethodName + "\").getAsBoolean(\"requireSession\", true))")
          .addStatement("requireSession()")
          .endControlFlow();

      List<Parameter> primitivesOrStrings = apiMethod.getParameters()
          .stream()
          .filter(parameter -> parameter.getType().isPrimitive() || parameter.getType().isAssignableFrom(String.class))
          .collect(Collectors.toList());

      List<Parameter> complex = apiMethod.getParameters()
          .stream()
          .filter(parameter -> !parameter.getType().isPrimitive() && !parameter.getType().isAssignableFrom(String.class))
          .collect(Collectors.toList());

      if (complex.size() > 1) {
        throw new RuntimeException(gatewayClassElement.getSimpleName() + "#" + apiMethodName + " has more than 1 complex type parameter. Only one is allowed.");
      }

      primitivesOrStrings.forEach(parameter -> {
        if (parameter.getType().isAssignableFrom(String.class)) {
          methodBuilder.addStatement("$T " + parameter.getName() + " = request.getMessageParameters().get(\"" + parameter.getName() + "\")", parameter.getType());
        } else if (parameter.getType().isAssignableFrom(Boolean.class) || parameter.getType().getName().equals("boolean")) {
          methodBuilder.addStatement("$T " + parameter.getName() + " = Boolean.valueOf(request.getMessageParameters().get(\"" + parameter.getName() + "\"))", parameter.getType());
        } else if (parameter.getType().isAssignableFrom(Integer.class) || parameter.getType().getName().equals("int")) {
          methodBuilder.addStatement("$T " + parameter.getName() + " = Integer.valueOf(request.getMessageParameters().get(\"" + parameter.getName() + "\"))", parameter.getType());
        } else if (parameter.getType().isAssignableFrom(Double.class) || parameter.getType().getName().equals("double")) {
          methodBuilder.addStatement("$T " + parameter.getName() + " = Double.valueOf(request.getMessageParameters().get(\"" + parameter.getName() + "\"))", parameter.getType());
        } else if (parameter.getType().isAssignableFrom(Float.class) || parameter.getType().getName().equals("float")) {
          methodBuilder.addStatement("$T " + parameter.getName() + " = Float.valueOf(request.getMessageParameters().get(\"" + parameter.getName() + "\"))", parameter.getType());
        }
      });

      complex.forEach(parameter -> {
        methodBuilder.addStatement("$T " + parameter.getName() + " = request.getBodyAs($T.class)", parameter.getType(), parameter.getType());
      });

      methodBuilder.addStatement(
          "$T result = " + gatewayName + "." + apiMethod.getName() + "(" + apiMethod.getParameters().stream().map(Parameter::getName).collect(Collectors.joining(", ")) + ")",
          apiMethod.getGenericReturnType());

      methodBuilder.addStatement("$T response = new MessageResponse()", MessageResponse.class);
      methodBuilder.addStatement("response.setBody(result.getResult())");
      methodBuilder.addStatement("response.setStatus($T.$L)", MessageStatus.class, MessageStatus.SUCCESS);
      methodBuilder.addStatement("return response");

      classBuilder.addMethod(methodBuilder.build());
    });

    TypeSpec remoteGatewaySpec = classBuilder.build();
    JavaFile javaFile = JavaFile.builder(calculatePackageName(gatewayClassElement), remoteGatewaySpec)
        .addFileComment("---------------------------------------------------------------------------------------------------------------------\n"
            + "  GENERATED FILE - ** Do not edit **\n"
            + "  Wrapped Class: $L\n"
            + "---------------------------------------------------------------------------------------------------------------------",
            gatewayClassElement.getPackage() + "." + gatewayClassElement.getSimpleName())
        .build();

    javaFile.writeTo(filer);
  }

  private String calculatePackageName(GatewayClassElement gatewayClassElement) {
    return gatewayClassElement.getPackage().replaceAll(".api.", ".remote.");
  }
}
