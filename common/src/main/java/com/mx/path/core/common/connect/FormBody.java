package com.mx.path.core.common.connect;

import com.mx.path.core.common.collection.MultiValueMap;

public interface FormBody {
  MultiValueMap<String, String> toForm();
}
