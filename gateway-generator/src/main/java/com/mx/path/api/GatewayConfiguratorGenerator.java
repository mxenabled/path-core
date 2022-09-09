package com.mx.path.api;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import com.mx.path.gateway.configuration.Configurator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

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

    JavaFile javaFile = JavaFile.builder(target.getBasePackage(), configuratorClass.build())
        .addFileComment("---------------------------------------------------------------------------------------------------------------------\n"
            + "  GENERATED FILE - ** Do not edit **\n"
            + "---------------------------------------------------------------------------------------------------------------------")
        .build();

    javaFile.writeTo(filer);
  }
}
