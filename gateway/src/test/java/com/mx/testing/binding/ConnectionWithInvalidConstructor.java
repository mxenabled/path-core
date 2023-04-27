package com.mx.testing.binding;

import com.mx.path.core.common.connect.AccessorConnectionSettings;

public class ConnectionWithInvalidConstructor extends AccessorConnectionSettings {
  private int garbage;

  public ConnectionWithInvalidConstructor(int garbage) {
    this.garbage = garbage;
  }
}
