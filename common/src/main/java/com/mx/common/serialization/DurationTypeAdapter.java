package com.mx.common.serialization;

import java.io.IOException;
import java.time.Duration;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mx.common.lang.Durations;

public class DurationTypeAdapter extends TypeAdapter<Duration> {
  @Override
  public final void write(JsonWriter out, Duration value) throws IOException {
    if (value != null) {
      out.value(Durations.toCompactString(value));
    }
  }

  @Override
  public final Duration read(JsonReader in) throws IOException {
    throw new UnsupportedOperationException();
  }
}
