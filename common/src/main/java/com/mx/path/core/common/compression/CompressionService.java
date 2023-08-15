package com.mx.path.core.common.compression;

public interface CompressionService {
  boolean isCompressed(String data);

  String compress(String data);

  String decompress(String data);
}
