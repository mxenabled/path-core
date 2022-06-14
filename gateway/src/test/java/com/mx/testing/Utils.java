package com.mx.testing;

import com.mx.common.collections.ObjectMap;
import com.mx.path.model.context.Session;

public class Utils {

  public static void injectSessionEncryptionService() {
    TestEncryptionService encryptionService = new TestEncryptionService(new ObjectMap());
    Session.setEncryptionServiceSupplier(() -> encryptionService);
  }

  public static void resetSessionEncryptionService() {
    Session.setEncryptionServiceSupplier(() -> null);
  }
}
