package com.mx.path.gateway.net;

import com.mx.common.collections.MultiValueMap;

public interface FormBody {
  MultiValueMap<String, String> toForm();
}
