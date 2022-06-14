package com.mx.models.ondemand;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Handles serialization of LocalDate for MDX OnDemand responses
 */
public class MdxOnDemandLocalDateSerializer extends StdSerializer<LocalDate> {

  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public MdxOnDemandLocalDateSerializer() {
    this(null);
  }

  public MdxOnDemandLocalDateSerializer(Class<LocalDate> t) {
    super(t);
  }

  @Override
  public final void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeString(formatter.format(value));
  }

}
