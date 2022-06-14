package com.mx.testing.binding;

import java.util.List;

import lombok.Data;

@Data
public class WithGenericList {

  @Data
  public static class SubObject {
    private String name;
    private int id;
  }

  private List<SubObject> list;
}
