package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.time.ZoneOffset;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ZoneOffsetTypeAdapter extends TypeAdapter<ZoneOffset> {
  @Override
  public final void write(JsonWriter out, ZoneOffset value) throws IOException {
    if (value != null) {
      out.value(value.getId());
    }
  }

  @Override
  public final ZoneOffset read(JsonReader in) throws IOException {
    return ZoneOffset.of(in.nextString());
  }
}
