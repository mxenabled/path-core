package com.mx.models.authorization;

import com.mx.models.MdxBase;

public class JavaScript extends MdxBase<JavaScript> {

  private NameValuePair[] arguments;
  private String functionToInvoke;

  public final NameValuePair[] getArguments() {
    return arguments;
  }

  public final void setArguments(NameValuePair[] newArguments) {
    this.arguments = newArguments;
  }

  public final String getFunctionToInvoke() {
    return functionToInvoke;
  }

  public final void setFunctionToInvoke(String newFunctionToInvoke) {
    this.functionToInvoke = newFunctionToInvoke;
  }
}
