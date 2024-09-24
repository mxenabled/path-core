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

/**
 * Generator for creating configurator classes for gateways.
 */
public class GatewayConfiguratorGenerator {
  private final Filer filer;

  /**
   * Build new {@link GatewayConfiguratorGenerator} instance.
   *
   * @param processingEnv environment provided by the annotation processing tool.
   */
  public GatewayConfiguratorGenerator(ProcessingEnvironment processingEnv) {
    this.filer = processingEnv.getFiler();
  }

  /**
   * Generates a configurator class for the given {@link GatewayClassElement}.
   *
   * @param target element representing the gateway class for which to generate a configurator.
   * @throws IOException to be thrown
   */
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
