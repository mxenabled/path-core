package com.mx.models.payment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.mx.models.MdxBase;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PayeeCategory extends MdxBase<PayeeCategory> {
  private String payeeCategoryName;

  public final String getName() {
    return payeeCategoryName;
  }

  public final void setName(String name) {
    this.payeeCategoryName = name;
  }
}
