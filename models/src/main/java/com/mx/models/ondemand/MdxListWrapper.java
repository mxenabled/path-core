package com.mx.models.ondemand;

import com.mx.models.MdxList;

/**
 * We need to have a special class to wrap lists because Jackson will wrap all arrays, regardless of configuration
 * options provided. Lame.
 */
public class MdxListWrapper {
  private final MdxList<?> list;
  private final String wrapperName;

  public MdxListWrapper(String wrapperName, MdxList<?> list) {
    this.wrapperName = wrapperName;
    this.list = list;
  }

  public final MdxList<?> getList() {
    return list;
  }

  public final String getWrapperName() {
    return wrapperName;
  }
}
