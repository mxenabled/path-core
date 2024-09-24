package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.util.regex.Pattern;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Type adapter for patterns.
 */
public class PatternTypeAdapter extends TypeAdapter<Pattern> {

  /**
   * Write to json output.
   * @param out output
   * @param value the Java object to write. May be null.
   * @throws IOException to be thrown
   */
  @Override
  public final void write(JsonWriter out, Pattern value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }

    out.value(value.pattern());
  }

  /**
   * Read from json.
   *
   * @param in input
   * @return object
   * @throws IOException
   */
  @Override
  public final Pattern read(JsonReader in) throws IOException {
    return Pattern.compile(in.nextString());
  }
}
