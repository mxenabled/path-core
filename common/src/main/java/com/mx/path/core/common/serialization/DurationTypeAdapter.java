package com.mx.path.core.common.serialization;

import java.io.IOException;
import java.time.Duration;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mx.path.core.common.lang.Durations;

public class DurationTypeAdapter extends TypeAdapter<Duration> {
  @Override
  public final void write(JsonWriter out, Duration value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }

    out.value(Durations.toCompactString(value));
  }

  @Override
  public final Duration read(JsonReader in) throws IOException {
    return Durations.fromCompactString(in.nextString());
  }
}
