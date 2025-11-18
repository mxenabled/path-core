package com.mx.testing.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
  private Integer id;
  private String name;
  private Address address;
}
