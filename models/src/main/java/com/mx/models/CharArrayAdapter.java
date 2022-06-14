package com.mx.models;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class CharArrayAdapter extends TypeAdapter<char[]> {

  @Override
  public final void write(JsonWriter out, char[] value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    out.value(String.valueOf(value));
  }

  @Override
  public final char[] read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return new char[0];
    }
    if (in.hasNext()) {
      return in.nextString().toCharArray();
    } else {
      return new char[0];
    }
  }
}
