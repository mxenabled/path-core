package com.mx.path.api;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.mx.path.api.remote.RemoteAccessorGenerator;
import com.mx.path.api.remote.RemoteGatewayGenerator;
import com.mx.path.api.reporting.ClassGenerationException;

@SupportedAnnotationTypes({ "com.mx.common.gateway.GatewayBaseClass" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public final class GatewayProcessor extends AbstractProcessor {

  private Map<String, GatewayClassElement> classes = new LinkedHashMap<>();

  /**
   * Capture processingEnv utilities
   * @param processingEnv
   */
  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    generateOneOffs();
  }

  /**
   * Annotation processor
   * <p>
   * @param annotations
   * @param roundEnv
   * @return
   * @see <a href="http://hannesdorfmann.com/annotation-processing/annotationprocessing101">http://hannesdorfmann.com/annotation-processing/annotationprocessing101</a>
   * @see <a href="https://stackoverflow.com/questions/31255098/find-method-arguments-of-annotated-method-using-java-annotation-processor">https://stackoverflow.com/questions/31255098/find-method-arguments-of-annotated-method-using-java-annotation-processor</a>
   */
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (!annotations.isEmpty()) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Gateway SDK Generator...");

      for (TypeElement annotation : annotations) {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

        for (Element e : annotatedElements) {
          if (e.getKind() == ElementKind.CLASS) {
            try {
              TypeElement clazz = (TypeElement) e;
              GatewayClassElement elem = new GatewayClassElement(clazz);
              classes.put(elem.getQualifiedName(), elem);
              compileClasses(classes, new GatewayClassElement(clazz));
            } catch (Exception ex) {
              reportError(ex);
            }
          }
        }
      }
    }

    GatewayGenerator generator = new GatewayGenerator(processingEnv);
    RemoteGatewayGenerator remoteGenerator = new RemoteGatewayGenerator(processingEnv);
    Set<String> processed = new HashSet<>();

    /**
     * Brute-force the classes into existence. Kind of crappy.
     * todo: do this another ways
     */
    do {
      processed.forEach(processedClass -> classes.remove(processedClass));
      processed.clear();

      classes.forEach((name, gateway) -> {
        try {
          generator.generate(gateway);
          remoteGenerator.generate(gateway);
          processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generated: " + gateway.getQualifiedName());
          processed.add(name);
        } catch (ContinueProcessing e) {
          processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Skipping " + gateway.getQualifiedName() + ". Waiting for " + e.getMessage());
        } catch (Exception e) {
          processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "An error occurred generating gateway: " + e.getMessage(), gateway.getBaseClass());
          reportError(e);
        }
      });

    } while (!processed.isEmpty());

    return true;
  }

  private void compileClasses(Map<String, GatewayClassElement> classCollection, GatewayClassElement gatewayClassElement) {
    gatewayClassElement.getFields().forEach(field -> {
      GatewayClassElement gateway = new GatewayClassElement(gatewayClassElement, field.getType(), field.getName() + "()");
      classCollection.put(gateway.getQualifiedName(), gateway);
      compileClasses(classCollection, gateway);
    });
  }

  /**
   * If we want to generate some code that doesn't necessarily depend on an annotation, we can put the generator code
   * here to ensure that it only gets run once.
   */
  private void generateOneOffs() {
    try {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generating AccessorProxy...");
      new AccessorProxyGenerator(processingEnv).generateAll();
    } catch (Exception e) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "An error occurred generating AccessorProxy: " + e.getMessage());
      reportError(e);
    }

    try {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generating RemoteAccessor...");
      new RemoteAccessorGenerator(processingEnv).generate();
    } catch (Exception e) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "An error occurred generating RemoteAccessor: " + e.getMessage());
      reportError(e);
    }
  }

  public void reportError(Exception e) {
    AnsiWrapper ansi = new AnsiWrapper();
    StringBuilder sb = new StringBuilder();

    if (e instanceof ClassGenerationException) {
      ClassGenerationException classGenerationException = (ClassGenerationException) e;
      sb.append(ansi.cyan("An error occurred in class: ")).append(ansi.yellow(classGenerationException.getClassName())).append("\n\n");
      sb.append(" -> ").append(ansi.red("Problem: ")).append(classGenerationException.getHumanReadableError()).append("\n\n");
      sb.append(" -> ").append(ansi.green("Fix: ")).append(classGenerationException.getFixInstructions());
    } else {
      sb.append("An unknown error occurred: ").append(e.getMessage());
    }

    throw new RuntimeException("\n\n\n" + sb + "\n\n\n");
  }

}
