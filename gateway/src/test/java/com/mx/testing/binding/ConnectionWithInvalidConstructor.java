package com.mx.testing.binding;

import com.mx.accessors.AccessorConnection;

public class ConnectionWithInvalidConstructor extends AccessorConnection {
  private int garbage;

  public ConnectionWithInvalidConstructor(int garbage) {
    this.garbage = garbage;
  }
}
