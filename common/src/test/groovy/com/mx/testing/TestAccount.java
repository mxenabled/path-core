package com.mx.testing;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.mx.path.core.common.model.ModelBase;

@Data
@EqualsAndHashCode(callSuper = false)
public class TestAccount extends ModelBase<TestAccount> {
  private String id;
}
