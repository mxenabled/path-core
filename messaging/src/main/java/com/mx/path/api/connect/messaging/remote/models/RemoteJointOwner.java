package com.mx.path.api.connect.messaging.remote.models;

import lombok.Data;

import com.mx.models.MdxBase;

@Data
public class RemoteJointOwner extends MdxBase<RemoteJointOwner> {
  private String association;
  private String fullName;
  private String subAccountId;
}
