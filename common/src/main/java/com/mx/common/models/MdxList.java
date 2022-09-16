package com.mx.common.models;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.google.gson.reflect.TypeToken;

/**
 * List of MdxBase
 *
 * todo: Need to decide what to do with this since it still references MDX.
 * @param <T>
 */
public class MdxList<T extends MdxBase<?>> implements List<T>, MdxWrappable<MdxList<T>> {
  private static final Map<Class<?>, Class<?>> CACHED_TYPE_TO_LIST_TYPE = new LinkedHashMap<>();
  private static final Map<Class<?>, Type> CACHED_TYPE_TO_LIST_TYPETOKEN = new LinkedHashMap<>();

  @SuppressFBWarnings("DM_NEW_FOR_GETCLASS")
  public static <T extends MdxBase<?>> Class<?> ofClass(Class<T> klass) {
    if (!CACHED_TYPE_TO_LIST_TYPE.containsKey(klass)) {
      CACHED_TYPE_TO_LIST_TYPE.put(klass, new MdxList<T>().getClass());
    }
    return CACHED_TYPE_TO_LIST_TYPE.get(klass);
  }

  public static <T extends MdxBase<?>> Type ofTypeToken(Class<T> klass) {
    if (!CACHED_TYPE_TO_LIST_TYPETOKEN.containsKey(klass)) {
      CACHED_TYPE_TO_LIST_TYPETOKEN.put(klass, new TypeToken<T>() {
      }.getType());
    }
    return CACHED_TYPE_TO_LIST_TYPETOKEN.get(klass);
  }

  private transient boolean wrapped = false;
  private final List<T> container = new ArrayList<T>();

  public MdxList() {
    super();
  }

  public MdxList(List<? extends T> copyList) {
    super();
    copyList.forEach(e -> container.add(e));
  }

  @Override
  public final int size() {
    return container.size();
  }

  @Override
  public final boolean isEmpty() {
    return container.isEmpty();
  }

  @Override
  public final boolean contains(Object o) {
    return container.contains(o);
  }

  @Override
  public final Iterator<T> iterator() {
    return container.iterator();
  }

  @Override
  public final Object[] toArray() {
    return container.toArray();
  }

  @Override
  public final <T> T[] toArray(T[] a) {
    return container.toArray(a);
  }

  @Override
  public final boolean add(T e) {
    return container.add(e);
  }

  @Override
  public final boolean remove(Object o) {
    return container.remove(o);
  }

  @Override
  public final boolean containsAll(Collection<?> c) {
    return container.containsAll(c);
  }

  @Override
  public final boolean addAll(Collection<? extends T> c) {
    return container.addAll(c);
  }

  @Override
  public final boolean addAll(int index, Collection<? extends T> c) {
    return container.addAll(index, c);
  }

  @Override
  public final boolean removeAll(Collection<?> c) {
    return container.removeAll(c);
  }

  @Override
  public final boolean retainAll(Collection<?> c) {
    return container.retainAll(c);
  }

  @Override
  public final void clear() {
    container.clear();
  }

  @Override
  public final T get(int index) {
    return container.get(index);
  }

  @Override
  public final T set(int index, T element) {
    return container.set(index, element);
  }

  @Override
  public final void add(int index, T element) {
    container.add(index, element);
  }

  @Override
  public final T remove(int index) {
    return container.remove(index);
  }

  @Override
  public final int indexOf(Object o) {
    return container.indexOf(o);
  }

  @Override
  public final int lastIndexOf(Object o) {
    return container.lastIndexOf(o);
  }

  @Override
  public final ListIterator<T> listIterator() {
    return container.listIterator();
  }

  @Override
  public final ListIterator<T> listIterator(int index) {
    return container.listIterator();
  }

  @Override
  public final List<T> subList(int fromIndex, int toIndex) {
    return container.subList(fromIndex, toIndex);
  }

  /**
   * Marks list as wrapped
   * Override to modify wrapping behavior
   * @return wrapped T
   */
  @Override
  public MdxList<T> wrapped() {
    setWrapped(true);
    return this;
  }

  @Override
  public final boolean getWrapped() {
    return wrapped;
  }

  @Override
  public final void setWrapped(boolean newWrapped) {
    this.wrapped = newWrapped;
  }
}
