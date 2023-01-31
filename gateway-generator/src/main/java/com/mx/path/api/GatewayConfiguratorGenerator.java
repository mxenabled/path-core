package com.mx.path.api;

import com.mx.common.lang.Strings;
import com.mx.path.gateway.configuration.Configurator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.io.IOException;

public class GatewayConfiguratorGenerator {
  private final Filer filer;

  public GatewayConfiguratorGenerator(ProcessingEnvironment processingEnv) {
    this.filer = processingEnv.getFiler();
  }

  public final void generate(GatewayClassElement target) throws IOException {
    TypeSpec.Builder configuratorClass = TypeSpec.classBuilder(target.getSimpleName() + "Configurator")
        .addModifiers(Modifier.PUBLIC)
        .superclass(ParameterizedTypeName.get(
            ClassName.get(Configurator.class),
            ClassName.get(target.getBasePackage(), target.getSimpleName())));

    if (Strings.isNotBlank(target.getAnnotation().initializer())) {
      configuratorClass.addMethod(
          MethodSpec.constructorBuilder()
              .addStatement("this.addInitializer(new $T())", ClassName.bestGuess(target.getAnnotation().initializer()))
              .build()
      );
    }

    JavaFile javaFile = JavaFile.builder(target.getBasePackage(), configuratorClass.build())
        .addFileComment("---------------------------------------------------------------------------------------------------------------------\n"
            + "  GENERATED FILE - ** Do not edit **\n"
            + "---------------------------------------------------------------------------------------------------------------------")
        .build();

    javaFile.writeTo(filer);
  }
}
