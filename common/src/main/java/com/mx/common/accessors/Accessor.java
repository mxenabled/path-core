package com.mx.common.accessors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.mx.common.exception.request.accessor.AccessorException;
import com.mx.common.gateway.GatewayAPI;

/**
 * Base for all accessor implementations
 *
 * <p>Provides configuration and some static utility methods
 *
 * todo: Move back to gateway after model extraction
 */
public abstract class Accessor {

  /**
   * Find first ancestor class of {@code klass} that extends {@link Accessor}, including self.
   *
   * @param klass
   * @return finds ancestor class that extends {@link Accessor}.
   */
  public static @Nonnull Class<? extends Accessor> getAccessorBase(Class<? extends Accessor> klass) {
    Class<?> currentClass = klass;
    do {
      if (currentClass.getSuperclass() == Accessor.class) {
        return currentClass.asSubclass(Accessor.class);
      }

      currentClass = currentClass.getSuperclass();
    } while (currentClass.getSuperclass() != null);

    // should never happen since provided klass constrained to extend Accessor
    return currentClass.asSubclass(Accessor.class);
  }

  /**
   * Get list of child accessor methods in given class.
   *
   * <p>The methods must have the  {@link API} annotation and should _not_ have {@link GatewayAPI} annotation.
   *
   * @return list of methods
   */
  public static List<AccessorMethodDefinition> getBaseChildAccessorMethods(Class<? extends Accessor> klass) {
    Class<? extends Accessor> base = getAccessorBase(klass);
    Method[] baseMethods = base.getDeclaredMethods();

    return Arrays.stream(baseMethods).map(method -> {
      API[] apis = method.getDeclaredAnnotationsByType(API.class);
      GatewayAPI[] functionAnnotation = method.getDeclaredAnnotationsByType(GatewayAPI.class);

      if (apis.length > 0 && functionAnnotation.length == 0) {
        return AccessorMethodDefinition.builder()
            .accessorClass(base)
            .annotation(apis[0])
            .method(method)
            .build();
      }

      return null;
    }).filter(Objects::nonNull).collect(Collectors.toList());
  }

  private AccessorConfiguration configuration;

  public Accessor(AccessorConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Get implemented accessors
   * <p>
   *   Finds methods from {@link Accessor} annotated with {@link API} and have been overriden in this instance.
   *
   * @return list of implemented accessors
   */
  public List<AccessorMethodDefinition> getChildAccessors() {
    return getBaseChildAccessorMethods(getClass()).stream().map(baseAccessorMethodDefinition -> {
      try {
        Method declaredMethod = getClass().getMethod(baseAccessorMethodDefinition.getMethod().getName());

        API api = Arrays.stream(baseAccessorMethodDefinition.getMethod().getDeclaredAnnotationsByType(API.class)).findFirst().orElse(null);
        Accessor accessor = (Accessor) declaredMethod.invoke(this);

        return AccessorMethodDefinition.builder()
            .accessorClass(accessor.getClass())
            .annotation(api)
            .method(declaredMethod)
            .base(baseAccessorMethodDefinition)
            .instance(accessor)
            .build();

      } catch (InvocationTargetException e) {
        // todo: We don't seem to be able to load a slf4j logger here. Need to switch this to a warn() once we can do that.
        if (e.getTargetException() instanceof AccessorException && ((AccessorException) e.getTargetException()).getStatus() != PathResponseStatus.NOT_IMPLEMENTED) {
          System.out.println("Accessor implementation for " + baseAccessorMethodDefinition.getMethod().getName() + " raised exception : " + e.getTargetException().getClass());
          e.getTargetException().printStackTrace();
        }
      } catch (IllegalAccessException | NoSuchMethodException ignored) {
      }

      return null;
    }).filter(Objects::nonNull).collect(Collectors.toList());
  }

  /**
   * Get configuration
   * @return
   */
  public AccessorConfiguration getConfiguration() {
    return configuration;
  }

  /**
   * Set configuration
   * @param configuration
   */
  public void setConfiguration(AccessorConfiguration configuration) {
    this.configuration = configuration;
  }
}
