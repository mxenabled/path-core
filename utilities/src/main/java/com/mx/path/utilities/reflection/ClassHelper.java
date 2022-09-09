package com.mx.path.utilities.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassHelper {
  private final Set<String> whiteListedMethods = new HashSet<>();

  public ClassHelper() {
  }

  /**
   * @param whileListedMethods List of methods for which to ignore Java Access Controls.
   */
  public ClassHelper(String... whileListedMethods) {
    this.whiteListedMethods.addAll(Arrays.asList(whileListedMethods));
  }

  @SuppressWarnings("unchecked")
  public final <T> T buildInstance(Class<T> returnType, Class<?> instanceType, Object... args) {
    T instance = null;

    try {

      if (!returnType.isAssignableFrom(instanceType)) {
        throw new RuntimeException("Invalid class " + instanceType.getCanonicalName() + ". Must extend " + returnType.getCanonicalName());
      }

      Class<?>[] argTypes = getTypes(args);

      Constructor<?> constr = instanceType.getConstructor(argTypes);
      instance = (T) constr.newInstance(args);

    } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
      throw new RuntimeException("Unable to create instance of " + instanceType.getCanonicalName(), e);
    }

    return instance;
  }

  public final Class<?> getClass(String klassName) {
    // todo: replace this with Strings.isEmpty(...) after we move lang.Strings from Commons to this library.
    if (klassName == null || klassName.trim().isEmpty()) {
      throw new RuntimeException("Class name not provided");
    }

    Class<?> klass;

    try {
      klass = Class.forName(klassName);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Class " + klassName + " not found or invalid", e);
    }

    return klass;
  }

  /**
   * Find method using name and assignable argument types.
   *
   * <p>Returns the first method matching given {@code name} with arg types that are assignable from
   * the given argument types.
   *
   * <p>If more than one method matches, the first one will be returned. This does not fond the closest match!
   *
   * todo: Improve this by finding the best match. Could try Class#findMethod first, then do the assignable search.
   *
   * @param klass target class
   * @param name of method
   * @param types of arguments to be passed
   * @return first-found method
   */
  public final Method getMethod(Class<?> klass, String name, Class<?>... types) {
    Stream<Method> allMethods = Stream.concat(Arrays.stream(klass.getMethods()), Arrays.stream(klass.getDeclaredMethods()));
    Optional<Method> method = allMethods.filter(m -> Objects.equals(name, m.getName())).filter(m -> {
      Parameter[] parameters = m.getParameters();
      if (parameters.length != types.length) {
        return false;
      }

      for (int i = 0; i < parameters.length; i++) {
        if (!parameters[i].getType().isAssignableFrom(types[i])) {
          return false;
        }
      }

      return true;
    }).findFirst();

    if (!method.isPresent()) {
      String arg = Arrays.stream(types).map(Class::getCanonicalName).collect(Collectors.joining(", "));
      throw new RuntimeException("No method " + name + " on class " + klass.getCanonicalName() + " with argTypes " + arg);
    }

    return method.get();
  }

  /**
   * Collect and return the types of given Object array.
   * @param objs array
   * @return array of types
   */
  public final Class<?>[] getTypes(Object... objs) {
    List<Class<?>> types = new ArrayList<>();
    for (Object a : objs) {
      types.add(a.getClass());
    }
    Class<?>[] argTypes = new Class<?>[0];

    if (!types.isEmpty()) {
      argTypes = types.toArray(argTypes);
    }
    return argTypes;
  }

  /**
   * Attempts to invoke given method on
   *
   * <p>Will attempt to locate and invoke the method
   *
   * <p>See {@link #getMethod(Class, String, Class[])} for description of method locator strategy.
   *
   * @param target to invoke method on
   * @param name of method
   * @param args of method
   */
  public final void invokeMethod(Object target, String name, Object... args) {
    Class<?>[] argTypes = getTypes(args);
    Method method = getMethod(target.getClass(), name, argTypes);
    try {
      boolean methodIsAccessible = method.isAccessible();
      if (!methodIsAccessible && whiteListedMethods.contains(method.getName())) {
        method.setAccessible(true);
      }

      try {
        method.invoke(target, args);
      } finally {
        if (!methodIsAccessible) {
          method.setAccessible(false);
        }
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("No method " + name + " on class " + target.getClass().getCanonicalName());
    }
  }

  /**
   * Attempts to invoke given static method on {@code target}
   *
   * <p>Will attempt to locate and invoke the method
   *
   * <p>See {@link #getMethod(Class, String, Class[])} for description of method locator strategy.
   *
   * @param target to invoke method on
   * @param name of method
   * @param args of method
   */
  public final void invokeStaticMethod(Class<?> target, String name, Object... args) {
    Class<?>[] argTypes = getTypes(args);
    Method method = getMethod(target, name, argTypes);
    try {
      boolean methodIsAccessible = method.isAccessible();
      if (!methodIsAccessible && whiteListedMethods.contains(method.getName())) {
        method.setAccessible(true);
      }

      try {
        method.invoke(target, args);
      } finally {
        if (!methodIsAccessible) {
          method.setAccessible(false);
        }
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("No method " + name + " on class " + target.getCanonicalName());
    }
  }

  /**
   * Attempts to invoke given method on
   *
   * <p>Will attempt to locate and invoke the method
   *
   * <p>See {@link #getMethod(Class, String, Class[])} for description of method locator strategy.
   *
   * @param resultType type of result
   * @param target to invoke method against
   * @param name of method
   * @param args of method
   * @param <T> type of result
   * @return
   */
  @SuppressWarnings("unchecked")
  public final <T> T invokeMethod(Class<T> resultType, Object target, String name, Object... args) {
    Class<?>[] argTypes = getTypes(args);
    Method method = getMethod(target.getClass(), name, argTypes);
    try {
      boolean methodIsAccessible = method.isAccessible();

      if (!methodIsAccessible && whiteListedMethods.contains(method.getName())) {
        methodIsAccessible = method.isAccessible();
        method.setAccessible(true);
      }

      try {
        return (T) method.invoke(target, args);
      } finally {
        if (!methodIsAccessible) {
          method.setAccessible(false);
        }
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("No method " + name + " on class " + target.getClass().getCanonicalName(), e);
    }
  }

  /**
   * Attempts to invoke given method on
   *
   * <p>Will attempt to locate and invoke the method
   *
   * <p>See {@link #getMethod(Class, String, Class[])} for description of method locator strategy.
   *
   * @param resultType type of result
   * @param target to invoke method against
   * @param name of method
   * @param args of method
   * @param <T> type of result
   * @return
   */
  @SuppressWarnings("unchecked")
  public final <T> T invokeStaticMethod(Class<T> resultType, Class<?> target, String name, Object... args) {
    Class<?>[] argTypes = getTypes(args);
    Method method = getMethod(target, name, argTypes);
    try {
      boolean methodIsAccessible = method.isAccessible();

      if (!methodIsAccessible && whiteListedMethods.contains(method.getName())) {
        methodIsAccessible = method.isAccessible();
        method.setAccessible(true);
      }

      try {
        return (T) method.invoke(target, args);
      } finally {
        if (!methodIsAccessible) {
          method.setAccessible(false);
        }
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("No method " + name + " on class " + target.getCanonicalName(), e);
    }
  }

  /**
   * Takes in a field and returns a list of resolved generic types.
   *
   * @param field
   * @return List<Type> a list of types for each generic field parameter
   */
  public final List<Type> resolveParameterizedFieldTypes(Field field) {
    return resolveParameterizedTypes(field.getGenericType());
  }

  /**
   * Takes in a method and returns a list of the parameterized types in the generic return type.
   * <p> i.e. List<String> would return [String] and Map<String, Integer> would return [String, Integer]
   *
   * @param method
   * @return List<Type> a list of types
   */
  public final List<Type> resolveParameterizedMethodReturnTypes(Method method) {
    return resolveParameterizedTypes(method.getGenericReturnType());
  }

  /**
   * Takes in a parameterized type and resolves the actual type arguments.
   *
   * @param parameterizedType
   * @return List<Type> a list of types
   */
  public final List<Type> resolveParameterizedTypes(Type parameterizedType) {
    if (!(parameterizedType instanceof ParameterizedType)) {
      return Collections.emptyList();
    }
    Type[] actualTypeArguments = ((ParameterizedType) parameterizedType).getActualTypeArguments();
    return Arrays.asList(actualTypeArguments);
  }

}
