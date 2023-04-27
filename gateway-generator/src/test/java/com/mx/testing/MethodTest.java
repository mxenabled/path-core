package com.mx.testing;

import java.util.List;

import com.mx.path.gateway.accessor.AccessorResponse;

public class MethodTest {

  public AccessorResponse<Account> get() {
    return null;
  }

  public AccessorResponse<TestMdxList<Account>> list() {
    return null;
  }

  public List<Account> invalidReturnType() {
    return null;
  }

  public AccessorResponse<List<Account>> invalidReturnListType() {
    return null;
  }

}
