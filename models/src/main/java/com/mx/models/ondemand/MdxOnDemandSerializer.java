package com.mx.models.ondemand;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mx.models.MdxWrappable;
import com.mx.models.ondemand.mixins.MixinDefinition;
import com.mx.models.ondemand.mixins.XmlSkipInternalAnnotationsIntrospector;

/**
 * Object serializer for MDX OnDemand
 *
 * Relies on Jackson Mix-ins to control details of serialization. For more information on Jackson Mix-ins see:
 * https://github.com/FasterXML/jackson-docs/wiki/JacksonMixInAnnotations
 */
public class MdxOnDemandSerializer<T extends MdxWrappable<?>> extends JsonSerializer<T> {

  private ObjectMapper mapper;

  /**
   * Build a serializer handler for targetClass.
   *
   * @param mixinDefinitions that define Jackson annotations to control serialization.
   */
  @SuppressWarnings("unchecked")
  public MdxOnDemandSerializer(MixinDefinition... mixinDefinitions) {
    super();
    this.mapper = new XmlMapper()
        .setAnnotationIntrospector(new XmlSkipInternalAnnotationsIntrospector())
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .registerModule(new JavaTimeModule().addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_DATE)))
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    for (MixinDefinition mixinDefinition : mixinDefinitions) {
      this.mapper.addMixIn(mixinDefinition.getTarget(), mixinDefinition.getMixin());
    }
  }

  @Override
  public final void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    ToXmlGenerator generator = (ToXmlGenerator) gen;

    if (value.getWrapped()) {
      generator.writeRaw("<mdx version=\"5.0\">\n");
    }

    // writeValue will set the state of xmlWriter to end, this is needed for Jackson calls to be closed successfully
    mapper.writer().writeValue(generator, value);

    if (value.getWrapped()) {
      generator.writeRaw("</mdx>\n");
    }
  }
}
