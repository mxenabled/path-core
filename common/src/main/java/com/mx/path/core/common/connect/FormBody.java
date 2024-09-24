package com.mx.path.core.common.connect;

import com.mx.path.core.common.collection.MultiValueMap;

/**
 * Form body interface.
 */
public interface FormBody {

  /**
   * @return multi-value map of form
   */
  MultiValueMap<String, String> toForm();
}
