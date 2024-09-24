package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.time.ZoneOffset;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Type adapter for zone offset.
 */
public class ZoneOffsetTypeAdapter extends TypeAdapter<ZoneOffset> {

  /**
   * Write to json output.
   *
   * @param out output
   * @param value the Java object to write. May be null.
   * @throws IOException to be thrown
   */
  @Override
  public final void write(JsonWriter out, ZoneOffset value) throws IOException {
    if (value != null) {
      out.value(value.getId());
    }
  }

  /**
   * Read from json input.
   * @param in input
   * @return object
   * @throws IOException to be thrown
   */
  @Override
  public final ZoneOffset read(JsonReader in) throws IOException {
    return ZoneOffset.of(in.nextString());
  }
}
