package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.util.regex.Pattern;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class PatternTypeAdapter extends TypeAdapter<Pattern> {
  @Override
  public final void write(JsonWriter out, Pattern value) throws IOException {
    if (value != null) {
      out.value(value.pattern());
    }
  }

  @Override
  public final Pattern read(JsonReader in) throws IOException {
    throw new UnsupportedOperationException();
  }
}
