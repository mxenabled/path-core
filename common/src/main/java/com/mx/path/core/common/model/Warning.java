package com.mx.path.core.common.model;

import lombok.Builder;
import lombok.Data;

/**
 * Warning object.  May be appended with MdxBase#appendWarning to any object that extends MdxBase.
 */
@Builder
@Data
public final class Warning {
  private String userMessage;
}
