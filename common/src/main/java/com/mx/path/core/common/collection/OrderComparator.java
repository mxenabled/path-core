package com.mx.path.core.common.collection;

import java.io.Serializable;
import java.util.Comparator;

public class OrderComparator implements Comparator<Object>, Serializable {
  /**
   * Compare 2 arbitrary objects using {@link Order} annotation
   *
   * <p>Lower values will appear first in the list and same values will be unchanged, relative to each other.
   *
   * <p>Nulls and missing annotations will be treated as indifferent and their order will be unaffected, relative to
   * each other.
   *
   * <p>See {@link Ordered} and {@link Order}
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return -1 if o1 &lt; o2, 1 if o1 &gt; o2, 0 if o1 == o2
   */
  @Override
  public int compare(Object o1, Object o2) {
    return Integer.compare(getObjectOrder(o1), getObjectOrder(o2));
  }

  private int getObjectOrder(Object obj) {
    if (obj == null) {
      return Ordered.INDIFFERENT;
    }

    Order annotation = obj.getClass().getDeclaredAnnotation(Order.class);

    if (annotation == null) {
      return Ordered.INDIFFERENT;
    }

    return annotation.value();
  }
}
