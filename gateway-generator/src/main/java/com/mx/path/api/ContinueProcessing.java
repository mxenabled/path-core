package com.mx.path.api;

public class ContinueProcessing extends RuntimeException {
  public ContinueProcessing(String message) {
    super(message);
  }
}
