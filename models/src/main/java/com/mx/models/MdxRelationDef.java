package com.mx.models;

import java.lang.reflect.Method;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a relationship between two MdxBase models
 */
@Data
@Builder
public class MdxRelationDef {
  private MdxRelationId relation;
  private Method method;
}
