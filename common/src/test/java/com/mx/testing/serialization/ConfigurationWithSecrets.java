package com.mx.testing.serialization;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mx.path.core.common.configuration.ConfigurationField;
import com.mx.path.core.common.request.Feature;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationWithSecrets {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Embedded {
    @ConfigurationField(secret = true)
    private String embeddedSecret;

    @ConfigurationField
    private Inception inception;

    public static class EmbeddedBuilder {
      private String embeddedSecret;
      private Inception inception;

      public EmbeddedBuilder defaults() {
        embeddedSecret = "deep secret";
        inception = Inception.builder().defaults().build();

        return this;
      }
    }
  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Inception {
    @ConfigurationField(secret = true)
    private String whereAmI;

    @ConfigurationField
    private String id;

    public static class InceptionBuilder {
      private String whereAmI;
      private String id;

      public InceptionBuilder defaults() {
        whereAmI = "shhh";
        id = "66";

        return this;
      }
    }
  }

  @ConfigurationField
  private String notSecret;

  @ConfigurationField(secret = true)
  private String secret;

  @ConfigurationField
  private int pinteger;

  @ConfigurationField
  private Integer cinteger;

  @ConfigurationField
  private long plong;

  @ConfigurationField
  private Long clong;

  @ConfigurationField
  private Number number;

  @ConfigurationField
  private double pdouble;

  @ConfigurationField
  private Double cdouble;

  @ConfigurationField
  private boolean pboolean;

  @ConfigurationField
  private Boolean cboolean;

  @ConfigurationField
  private Feature enumeration;

  @ConfigurationField
  private Embedded embedded;

  private Embedded skipped;

  @ConfigurationField(elementType = String.class, secret = true)
  private List<String> secrets;

  public static class ConfigurationWithSecretsBuilder {
    private String notSecret;
    private String secret;
    private int pinteger;
    private Integer cinteger;
    private long plong;
    private Long clong;
    private Number number;
    private double pdouble;
    private Double cdouble;
    private boolean pboolean;
    private Boolean cboolean;
    private Feature enumeration;
    private Embedded embedded;
    private Embedded skipped;
    private List<String> secrets = new ArrayList<>();

    public ConfigurationWithSecretsBuilder defaults() {
      notSecret = "not a secret";
      secret = "do not serialize this";
      pinteger = 10;
      cinteger = 11;
      plong = 12;
      clong = Long.valueOf(13);
      number = 14;
      pdouble = 15.1;
      cdouble = 15.2;
      pboolean = true;
      cboolean = true;
      enumeration = Feature.TRANSFERS;
      secrets.add("secret 1");
      secrets.add("secret 2");
      embedded = Embedded.builder().defaults().build();
      skipped = Embedded.builder().defaults().build();
      return this;
    }
  }
}
