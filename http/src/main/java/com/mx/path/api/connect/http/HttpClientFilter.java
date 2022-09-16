package com.mx.path.api.connect.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.mx.common.collections.MultiValueMap;
import com.mx.common.collections.SingleValueMap;
import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilterBase;
import com.mx.common.connect.Response;
import com.mx.common.exception.ConnectException;
import com.mx.common.http.HttpStatus;
import com.mx.path.api.connect.http.certificates.MutualAuthProvider;
import com.mx.path.api.connect.http.certificates.MutualAuthProviderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * HttpClient web request executor
 * <p>
 * Makes API call for given <class>HttpRequest</class> using Apache HttpClient
 * </p>
 */
public class HttpClientFilter extends RequestFilterBase {
  /**
   * String hints that may show up in a Content-Type header, hinting to us that we should return the raw body
   * instead of converting it to a String.
   */
  private static final List<String> RAW_BODY_CONTENT_TYPE_HINTS = Arrays.asList("image", "pdf", "msword");

  // Statics

  private static final Gson GSON = new Gson();

  // Public

  @Override
  public final void execute(Request request, Response response) {
    BasicCookieStore cookieStore = new BasicCookieStore();
    HttpClientBuilder clientBuilder = HttpClients.custom()
        .disableAutomaticRetries()
        .disableRedirectHandling()
        .setDefaultCookieStore(cookieStore);

    HttpRequest httpRequest = (HttpRequest) request;
    HttpResponse httpResponse = (HttpResponse) response;
    applySkipHostNameVerifySetting(httpRequest, clientBuilder);

    MutualAuthProvider mutualAuthProvider = MutualAuthProviderFactory.build(httpRequest.getConnectionSettings());
    if (mutualAuthProvider != null) {
      mutualAuthProvider.add(clientBuilder);
    }

    try {
      // NOTE: Good writeup on timeouts: https://www.baeldung.com/httpclient-timeout
      RequestConfig requestConfig = RequestConfig
          .custom()
          .setConnectionRequestTimeout((int) httpRequest.getRequestTimeOut().toMillis())
          .setConnectTimeout((int) httpRequest.getRequestTimeOut().toMillis())
          .setSocketTimeout((int) httpRequest.getRequestTimeOut().toMillis())
          .build();

      try (CloseableHttpClient client = clientBuilder.setDefaultRequestConfig(requestConfig).build()) {
        RequestBuilder req = RequestBuilder.create(httpRequest.getMethod());

        if (httpRequest.getQueryStringParams().size() != 0) {
          List<NameValuePair> parameters = new ArrayList<>();
          httpRequest.getQueryStringParams().forEach((name, value) -> {
            parameters.add(new BasicNameValuePair(name, value));
          });
          req.setUri(new URIBuilder(httpRequest.getUri()).addParameters(parameters).build());
        } else {
          req.setUri(httpRequest.getUri());
        }

        MultiValueMap<String, String> requestHeaders = httpRequest.getHeadersAsMultiValueMap();
        requestHeaders.flatEntrySet().forEach((entry) -> req.addHeader(entry.getKey(), entry.getValue()));

        HttpEntity entity = buildHttpEntity(httpRequest);

        if (entity != null) {
          req.setEntity(entity);
        } else {
          req.setHeader("Content-Length", "0");
        }

        // Create a custom response handler
        ResponseHandler<HttpResponse> responseHandler = resp -> {
          MultiValueMap<String, String> headers = new MultiValueMap<>();
          MultiValueMap<String, String> cookies = new MultiValueMap<>();

          Arrays.stream(resp.getAllHeaders()).forEach(h -> headers.add(h.getName(), h.getValue()));
          cookieStore.getCookies().forEach(cookie -> cookies.add(cookie.getName(), cookie.getValue()));

          HttpEntity httpEntity = resp.getEntity();
          if (httpEntity != null) {
            setResponseBody(httpRequest, httpResponse, headers, httpEntity);
          }

          httpResponse.withCookies(new SingleValueMap<>(cookies));
          httpResponse.setStatus(HttpStatus.resolve(resp.getStatusLine().getStatusCode()));
          httpResponse.setHeaders(new SingleValueMap<>(headers));

          return httpResponse;
        };

        try {
          request.start();
          try {
            client.execute(req.build(), responseHandler);
          } finally {
            response.finish();
          }
        } catch (IOException e) {
          throw new ConnectException("HttpClient Execute failed", e);
        }
      }
    } catch (RuntimeException | IOException | URISyntaxException e) {
      throw new ConnectException("HttpClient Setup failed", e);
    }

    next(request, response);
  }

  // Package Private

  /**
   * Looks at the Content-Type headers and decides whether the response should be returned as a block
   * of bytes, or whether we can convert the bytes into a String ahead of time.
   *
   * @param headers
   * @return boolean
   */
  boolean shouldReturnRawBody(MultiValueMap<String, String> headers) {
    List<String> contentTypes = headers.get("Content-Type");
    if (contentTypes != null && !contentTypes.isEmpty()) {
      for (String contentType : contentTypes) {
        String lowerCaseContentType = contentType.toLowerCase();
        for (String rawBodyContentTypeHint : RAW_BODY_CONTENT_TYPE_HINTS) {
          if (lowerCaseContentType.contains(rawBodyContentTypeHint)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Sets the HttpResponse's `body` and/or `rawBody` field based on the HttpRequest's PreferredResponseBodyType.
   * This allows the caller to have full control over what data they get back.
   *
   * @param request the incoming request object
   * @param response the response that will be returned to the caller
   * @param responseHeaders the response headers that have come back from the upstream service
   * @param httpEntity the HttpEntity that has the response data we need.
   * @throws IOException
   */
  void setResponseBody(HttpRequest request, HttpResponse response, MultiValueMap<String, String> responseHeaders, HttpEntity httpEntity) throws IOException {
    String bodyString = "";
    byte[] bodyData = new byte[0];

    Request.PreferredResponseBodyType preferredResponseBodyType = request.getPreferredResponseBodyType();
    switch (preferredResponseBodyType) {
      case INFERRED_FROM_CONTENT_TYPE:
        if (shouldReturnRawBody(responseHeaders)) {
          bodyData = getHttpEntityBodyAsBytes(httpEntity);
        } else {
          bodyString = getHttpEntityBodyAsString(httpEntity);
        }
        break;
      case STRING_AND_RAW:
        bodyData = getHttpEntityBodyAsBytes(httpEntity);
        bodyString = getHttpEntityBodyAsString(httpEntity);
        break;
      case RAW:
        bodyData = getHttpEntityBodyAsBytes(httpEntity);
        break;
      case STRING:
        bodyString = getHttpEntityBodyAsString(httpEntity);
        break;
      default:
        break;
    }
    response.withBody(bodyString).withRawData(bodyData);
  }

  /**
   * Converts the internal HttpEntity data to a String.
   *
   * @param httpEntity
   * @return
   * @throws IOException
   */
  String getHttpEntityBodyAsString(HttpEntity httpEntity) throws IOException {
    return EntityUtils.toString(httpEntity);
  }

  /**
   * Converts the internal HttpEntity data to a byte array.
   *
   * @param httpEntity
   * @return
   * @throws IOException
   */
  byte[] getHttpEntityBodyAsBytes(HttpEntity httpEntity) throws IOException {
    return EntityUtils.toByteArray(httpEntity);
  }

  // Private

  private void applySkipHostNameVerifySetting(HttpRequest request, HttpClientBuilder clientBuilder) {
    if (request.getConnectionSettings() != null && request.getConnectionSettings().getSkipHostNameVerify()) {
      clientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
    }
  }

  private HttpEntity buildHttpEntity(HttpRequest request) throws UnsupportedEncodingException {
    HttpEntity entity = null;

    if (request.getBody() != null) {
      if (request.getBody() instanceof byte[]) {
        byte[] byteBody = (byte[]) request.getBody();
        entity = new ByteArrayEntity(byteBody, ContentType.APPLICATION_OCTET_STREAM);
      } else if (request.getBody().getClass() == String.class) {
        entity = new StringEntity(request.getBody().toString());
      } else {
        // todo: Need to look at content type to determine the payload format.
        entity = new StringEntity(GSON.toJson(request.getBody()));
      }
    } else if (request.getFormBody() != null) {
      List<NameValuePair> form = new ArrayList<>();
      request.getFormBody().toForm().forEach((k, values) -> {
        values.forEach((v) -> {
          form.add(new BasicNameValuePair(k, v));
        });
      });
      entity = new UrlEncodedFormEntity(form);
    }

    return entity;
  }
}
