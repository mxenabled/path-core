package com.mx.testing;

import java.util.List;

import com.mx.accessors.AccessorResponse;
import com.mx.models.MdxList;
import com.mx.models.account.Account;

public class MethodTest {

  public AccessorResponse<Account> get() {
    return null;
  }

  public AccessorResponse<MdxList<Account>> list() {
    return null;
  }

  public List<Account> invalidReturnType() {
    return null;
  }

  public AccessorResponse<List<Account>> invalidReturnListType() {
    return null;
  }

}
