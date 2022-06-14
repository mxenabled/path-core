package com.mx.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.mx.common.reflection.Annotations;

/**
 * Provides MdxBase reflection utilities.
 *
 * ID:
 *
 * Extract the ID of an object:
 *
 * <pre>
 *   Account account = new Account();
 *   account.setId("account1");
 *
 *   // Get the ID
 *   String id = new MdxBaseReflector().getId(account);
 *
 * </pre>
 *
 * Relations:
 * <pre>
 *   class Transfer extends MdxBase<Transfer> {
 *     private String fromAccountId;
 *
 *     @MdxRelationId(referredClass = Account.class)
 *     public String getFromAccountId() {
 *       return fromAccountId;
 *     }
 *   }
 * </pre>
 *
 * To discover the relationship
 *
 * <pre>
 *   Transfer transfer = new Transfer();
 *   transfer.setFromAccountId("account-1");
 *
 *   // Get the relationship definitions:
 *   List<MdxRelationDef> relationships = new MdxBaseReflector().getRelations(transfer);
 *
 *   // Get the relationship values:
 *   List<String> ids = new MdxBaseReflector().getRelationIds(transfer);
 * </pre>
 */
public class MdxBaseReflector {

  // Cached map of ID methods. So they don't need to be retrieved every time they need to be invoked. These shouldn't change during runtime.
  private static final Map<Class<?>, Method> METHODS = new HashMap<>();

  // Cached map of relation defs. So they don't need to be rebuilt every time they are needed. These shouldn't change during runtime.
  private static final Map<Class<?>, List<MdxRelationDef>> RELATIONS = new HashMap<>();

  /**
   * Extract the id from obj
   *
   * @param obj instance of MdxBase
   * @return the extracted id
   */
  public final String getId(MdxBase<?> obj) {
    Method getIdMethod;
    String id;
    Class<?> klass = obj.getClass();

    try {
      getIdMethod = getIdMethod(klass);
      id = (String) getIdMethod.invoke(obj);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Unable to extract id from model " + klass.getCanonicalName(), e);
    }

    return id;
  }

  /**
   * Get list of relations annotated on parentClass that refers to referredClass
   * @param parentClass the class with relation annotation
   * @param referredClass the class that the parentClass refers to
   * @return list of relation defs
   */
  public final List<MdxRelationDef> getRelationDefs(Class<MdxBase<?>> parentClass, Class<?> referredClass) {
    return getRelationMethods(parentClass).stream()
        .filter(relation -> relation.getRelation().referredClass() == referredClass)
        .collect(Collectors.toList());
  }

  /**
   * Extract the reference values from obj that refer to referredClass
   * @param obj
   * @param referredClass
   * @return list of reference ids
   */
  @SuppressWarnings("unchecked")
  public final List<String> getRelationIds(MdxBase<?> obj, Class<?> referredClass) {
    Class<MdxBase<?>> parentClass = (Class<MdxBase<?>>) obj.getClass();
    List<MdxRelationDef> relations = getRelationDefs(parentClass, referredClass);

    return relations.stream().map(relation -> {
      try {
        return (String) relation.getMethod().invoke(obj);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException("Unable to extract relation (" + parentClass.getCanonicalName() + " -> " + referredClass.getCanonicalName() + ") id using " + relation.getMethod().getName(), e);
      }
    }).filter(Objects::nonNull).collect(Collectors.toList());
  }

  private Method getIdMethod(Class<?> klass) {
    Method idMethod = METHODS.get(klass);

    if (idMethod == null) {
      try {
        idMethod = klass.getMethod("getId");
      } catch (NoSuchMethodException e) {
        throw new RuntimeException("Unable to reflect getId method from model " + klass.getCanonicalName(), e);
      }
      METHODS.put(klass, idMethod);
    }

    return idMethod;
  }

  private List<MdxRelationDef> getRelationMethods(Class<MdxBase<?>> targetClass) {
    List<MdxRelationDef> relations = RELATIONS.get(targetClass);

    if (relations == null) {
      relations = Annotations.methodsWithAnnotation(MdxRelationId.class, targetClass)
          .stream()
          .map(method -> MdxRelationDef.builder()
              .relation(method.getAnnotation(MdxRelationId.class))
              .method(method)
              .build())
          .collect(Collectors.toList());

      RELATIONS.put(targetClass, relations);
    }

    return relations;
  }

}
