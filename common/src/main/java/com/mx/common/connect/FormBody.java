package com.mx.common.connect;

import com.mx.common.collections.MultiValueMap;

public interface FormBody {
  MultiValueMap<String, String> toForm();
}
