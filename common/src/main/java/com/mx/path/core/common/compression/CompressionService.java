package com.mx.path.core.common.compression;

/**
 * Interface for compression operations.
 */
public interface CompressionService {

  /**
   * Check if object is compressed.
   *
   * @param data data to compress
   * @return true if compressed
   */
  boolean isCompressed(String data);

  /**
   * Compress data.
   *
   * @param data data to compress
   * @return data compressed
   */
  String compress(String data);

  /**
   * Decompress data.
   *
   * @param data data to decompress
   * @return data decompressed
   */
  String decompress(String data);
}
