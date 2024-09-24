package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.time.ZoneId;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Type adapter for zone id.
 */
public class ZoneIdTypeAdapter extends TypeAdapter<ZoneId> {

  /**
   * Write to json output.
   *
   * @param out output
   * @param value the Java object to write. May be null.
   * @throws IOException exception to be thrown
   */
  @Override
  public final void write(JsonWriter out, ZoneId value) throws IOException {
    if (value != null) {
      out.value(value.getId());
    }
  }

  /**
   * Read from json input.
   *
   * @param in input
   * @return object
   * @throws IOException to be thrown
   */
  @Override
  public final ZoneId read(JsonReader in) throws IOException {
    return ZoneId.of(in.nextString());
  }
}
