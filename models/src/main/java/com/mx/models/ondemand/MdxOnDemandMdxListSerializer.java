package com.mx.models.ondemand;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
import com.mx.models.MdxList;
import com.mx.models.ondemand.mixins.MixinDefinition;
import com.mx.models.ondemand.mixins.XmlSkipInternalAnnotationsIntrospector;

/**
 * MdxList serializer for MDX OnDemand
 *
 * Relies on Jackson Mix-ins to control details of serialization. For more information on Jackson Mix-ins see:
 * https://github.com/FasterXML/jackson-docs/wiki/JacksonMixInAnnotations
 */
public class MdxOnDemandMdxListSerializer extends JsonSerializer<MdxListWrapper> {

  private final ObjectMapper mapper;

  /**
   * Build a list serializer.
   *
   * @param mixinDefinitions that define Jackson annotations to control serialization.
   */
  @SuppressWarnings("unchecked")
  public MdxOnDemandMdxListSerializer(MixinDefinition... mixinDefinitions) {
    super();

    this.mapper = new XmlMapper()
        .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        .setAnnotationIntrospector(new XmlSkipInternalAnnotationsIntrospector())
        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .registerModule(new JavaTimeModule().addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_DATE)))
        .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    for (MixinDefinition mixin : mixinDefinitions) {
      if (mixin != null) {
        mapper.addMixIn(mixin.getTarget(), mixin.getMixin());
      }
    }
  }

  /**
   * Custom serialize.
   *
   * Wraps response in <mdx></mdx> if marked as wrapped.
   *
   * @param value
   * @param gen
   * @param serializers
   * @throws IOException
   */
  @Override
  public final void serialize(MdxListWrapper value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    ToXmlGenerator generator = (ToXmlGenerator) gen;

    MdxList<?> list = value.getList();

    if (list.getWrapped()) {
      generator.writeRaw("<mdx version=\"5.0\">\n");
    }

    if (value.getWrapperName() != null && !Objects.equals(value.getWrapperName(), "")) {
      generator.writeRaw("<" + value.getWrapperName() + ">\n");
    }

    for (int i = 0; i < list.size(); i++) {
      // writeValue will set the state of xmlWriter to end, this is needed for Jackson calls to be closed successfully
      if (i == list.size() - 1) {
        mapper.writer().writeValue(generator, list.get(i));
      } else {
        generator.writeRaw(mapper.writer().writeValueAsString(list.get(i)));
      }
    }

    if (value.getWrapperName() != null && !Objects.equals(value.getWrapperName(), "")) {
      generator.writeRaw("</" + value.getWrapperName() + ">\n");
    }

    if (list.getWrapped()) {
      generator.writeRaw("</mdx>\n");
    }
  }
}
