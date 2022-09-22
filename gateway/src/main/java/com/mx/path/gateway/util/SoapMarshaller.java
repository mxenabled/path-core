package com.mx.path.gateway.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.mx.common.accessors.RequestPayloadException;
import com.mx.common.accessors.ResponsePayloadException;

/**
 * Static methods for marshalling to and from SOAP Envelopes
 *
 * NOTE: This should be moved to Common or Models with the other serializer utilities.
 */
public class SoapMarshaller {

  private static Map<Class, JAXBContext> jaxbContextMap = new HashMap<Class, JAXBContext>();

  /**
   * Object to SOAP envelope. Marshalls to requestObject.getClass().
   *
   * <p>
   * The class must have an XmlRoot annotation. If it does not (because it is generated), wrap in a {@link javax.xml.bind.JAXBElement} proxy and use {@code toEnvelope(Object requestObj, Class<?> klass)}
   * </p>
   *
   * @param requestObj
   * @return SOAP Envelope
   */
  public static String toEnvelope(Object requestObj) {
    if (requestObj instanceof JAXBElement) {
      throw new RequestPayloadException("Trying to marshall JAXBElement without specifying class. Use toEnvelope(Object requestObj, Class<?> klass) instead.", null);
    }
    return toEnvelope(requestObj, requestObj.getClass());
  }

  /**
   * Object to SOAP envelope. Allows the desired object marshalling type to be specified.
   *
   * <p>
   * Use this if the request object is wrapped in a {@link javax.xml.bind.JAXBElement} proxy.
   * </p>
   * @param requestObj
   * @param klass convert to this type.
   * @return SOAP Envelope
   */
  public static String toEnvelope(Object requestObj, Class<?> klass) {
    return toEnvelope(null, null, requestObj, klass);
  }

  /**
   * Object to SOAP envelope. Allows the desired object marshalling type to be specified.
   * Allows SOAP headers to also be set
   * Expects you to have a Map of classes to static JAXBContexts
   *
   * @param requestHeaderObj
   * @param headerKlass
   * @param requestBodyObj
   * @param bodyKlass
   * @return
   */
  public static String toEnvelope(Object requestHeaderObj, Class headerKlass, Object requestBodyObj, Class bodyKlass) {
    try {
      SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();

      if (requestHeaderObj != null) {
        JAXBContext headerContext = jaxbContextMap.get(headerKlass);
        if (headerContext == null) {
          headerContext = JAXBContext.newInstance(headerKlass);
          jaxbContextMap.put(headerKlass, headerContext);
        }
        Marshaller headerMarshaller = headerContext.createMarshaller();
        headerMarshaller.marshal(requestHeaderObj, soapMessage.getSOAPHeader());
      }

      if (requestBodyObj != null) {
        JAXBContext bodyContext = jaxbContextMap.get(bodyKlass);
        if (bodyContext == null) {
          bodyContext = JAXBContext.newInstance(bodyKlass);
          jaxbContextMap.put(bodyKlass, bodyContext);
        }
        Marshaller bodyMarshaller = bodyContext.createMarshaller();
        bodyMarshaller.marshal(requestBodyObj, soapMessage.getSOAPBody());
      }

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      soapMessage.writeTo(outputStream);

      return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    } catch (IOException | JAXBException | SOAPException ex) {
      throw new RequestPayloadException(ex.getMessage(), ex);
    }
  }

  /**
   * Marshall SOAP envelope to object
   * @param <T> type to marshall to
   * @param envelopeXml of SOAP response
   * @param klass type to marshall to
   * @return marshalled object of type <T>
   */
  public static <T> T toResponse(String envelopeXml, Class<T> klass) {
    try {
      SOAPMessage message = MessageFactory.newInstance().createMessage(null,
          new ByteArrayInputStream(envelopeXml.getBytes(StandardCharsets.UTF_8)));
      JAXBContext responseContext = jaxbContextMap.get(klass);
      if (responseContext == null) {
        responseContext = JAXBContext.newInstance(klass);
        jaxbContextMap.put(klass, responseContext);
      }
      Unmarshaller unmarshaller = responseContext.createUnmarshaller();
      JAXBElement<T> response = unmarshaller.unmarshal(message.getSOAPBody().extractContentAsDocument(), klass);

      return response.getValue();
    } catch (JAXBException | IOException | SOAPException ex) {
      throw new ResponsePayloadException(ex.getMessage(), ex);
    }
  }

}
