package com.mx.testing;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.mx.common.models.ModelBase;

@Data
@EqualsAndHashCode(callSuper = false)
public class TestAccount extends ModelBase<TestAccount> {
  private String id;
}
