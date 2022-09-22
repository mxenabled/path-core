package com.mx.path.gateway.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import com.mx.common.exception.request.accessor.RequestPayloadException;

import org.junit.jupiter.api.Test;

@SuppressWarnings("checkstyle:magicnumber")
public class SoapMarshallerTest {

  /**
   * Some classes to test marshalling with
   */

  @XmlRootElement
  static class MarshallTest {
    private String name;

    public final String getName() {
      return name;
    }

    public final void setName(String name) {
      this.name = name;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "unrootedMarshallTest")
  static class UnrootedMarshallTest {
    private String name;

    public final String getName() {
      return name;
    }

    public final void setName(String name) {
      this.name = name;
    }
  }

  /**
   * toEnvelope()
   */

  @Test
  public void marshallsRootedObject() {
    MarshallTest requestObject = new MarshallTest();
    requestObject.setName("tester");

    String xml = SoapMarshaller.toEnvelope(requestObject);
    assertEquals("<SOAP-ENV:Envelope xmlns:SOAP-ENV=`http://schemas.xmlsoap.org/soap/envelope/`><SOAP-ENV:Header/><SOAP-ENV:Body><marshallTest><name>tester</name></marshallTest></SOAP-ENV:Body></SOAP-ENV:Envelope>".replaceAll("`", "\""), xml);
  }

  @Test
  public void marshallsUnootedObject() {
    UnrootedMarshallTest requestObject = new UnrootedMarshallTest();
    requestObject.setName("tester");

    JAXBElement<UnrootedMarshallTest> wrappedRequestObject = new JAXBElement<UnrootedMarshallTest>(new QName("http://www.mx.com/UnitTest/UnrootedMarshallTest", "unrootedMarshallTest"), UnrootedMarshallTest.class, requestObject);

    String xml = SoapMarshaller.toEnvelope(wrappedRequestObject, UnrootedMarshallTest.class);
    assertEquals("<SOAP-ENV:Envelope xmlns:SOAP-ENV=`http://schemas.xmlsoap.org/soap/envelope/`><SOAP-ENV:Header/><SOAP-ENV:Body><ns3:unrootedMarshallTest xmlns:ns3=`http://www.mx.com/UnitTest/UnrootedMarshallTest`><name>tester</name></ns3:unrootedMarshallTest></SOAP-ENV:Body></SOAP-ENV:Envelope>".replaceAll("`", "\""), xml);
  }

  @Test
  public void marshallsUnootedObjectWithoutSpecifyingClass() {
    UnrootedMarshallTest requestObject = new UnrootedMarshallTest();
    requestObject.setName("tester");

    JAXBElement<UnrootedMarshallTest> wrappedRequestObject = new JAXBElement<UnrootedMarshallTest>(new QName("http://www.mx.com/UnitTest/UnrootedMarshallTest", "unrootedMarshallTest"), UnrootedMarshallTest.class, requestObject);

    try {
      SoapMarshaller.toEnvelope(wrappedRequestObject);
      fail("Should have thrown MdxApiException");
    } catch (RequestPayloadException ex) {
      assertEquals("Trying to marshall JAXBElement without specifying class. Use toEnvelope(Object requestObj, Class<?> klass) instead.", ex.getMessage());
    }
  }

  /**
   * toResponse()
   */

  @Test
  public void unmarshallsToReponseObject() {
    String envelope = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=`http://schemas.xmlsoap.org/soap/envelope/`><SOAP-ENV:Header/><SOAP-ENV:Body><marshallTest><name>tester</name></marshallTest></SOAP-ENV:Body></SOAP-ENV:Envelope>".replaceAll("`", "\"");
    MarshallTest responseObject = SoapMarshaller.toResponse(envelope, MarshallTest.class);
    assertNotNull(responseObject);
    assertEquals("tester", responseObject.getName());
  }

  @Test
  public void unmarshallsToUnrootedReponseObject() {
    String envelope = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=`http://schemas.xmlsoap.org/soap/envelope/`><SOAP-ENV:Header/><SOAP-ENV:Body><marshallTest><name>tester</name></marshallTest></SOAP-ENV:Body></SOAP-ENV:Envelope>".replaceAll("`", "\"");
    UnrootedMarshallTest responseObject = SoapMarshaller.toResponse(envelope, UnrootedMarshallTest.class);
    assertNotNull(responseObject);
    assertEquals("tester", responseObject.getName());
  }
}
