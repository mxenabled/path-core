package com.mx.testing;

import com.mx.path.model.context.ScopeKeyGenerator;

public enum TestScope implements ScopeKeyGenerator {
  Key {
    @Override
    public String generate() {
      return "scope";
    }
  }
}
