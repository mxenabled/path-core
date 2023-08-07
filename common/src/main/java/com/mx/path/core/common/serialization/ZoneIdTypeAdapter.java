package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.time.ZoneId;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ZoneIdTypeAdapter extends TypeAdapter<ZoneId> {
  @Override
  public final void write(JsonWriter out, ZoneId value) throws IOException {
    if (value != null) {
      out.value(value.getId());
    }
  }

  @Override
  public final ZoneId read(JsonReader in) throws IOException {
    throw new UnsupportedOperationException();
  }
}
