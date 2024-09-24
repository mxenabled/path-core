package com.mx.path.core.utility.net;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.Getter;

/**
 * Utility class for building and modifying URIs.
 */
public final class UriHelper {
  /**
   * Return uri object
   * @return uri
   */
  @Getter
  private URI uri;

  /**
   * Build and set uri object from string
   * @param uri used on URI string constructor
   * @throws URISyntaxException if provided URI string is not a valid URI
   */
  public UriHelper(String uri) throws URISyntaxException {
    this.uri = new URI(uri);
  }

  /**
   * Build and set uri object from URI
   * @param uri new URI to set
   */
  public UriHelper(URI uri) {
    this.uri = uri;
  }

  /**
   * Add a new query parameter to uri object
   * @param key new parameter key
   * @param value new parameter value
   * @return this helper with new query parameter
   * @throws URISyntaxException if updated URI string has invalid query parameter
   */
  public UriHelper appendQueryParameter(String key, String value) throws URISyntaxException {
    String query = uri.getQuery();
    StringBuilder sb = new StringBuilder(query == null ? "" : query);

    if (query == null) {
      sb.append(key)
          .append("=")
          .append(value);
    } else {
      sb.append("&")
          .append(key)
          .append("=")
          .append(value);
    }

    uri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), sb.toString(), uri.getFragment());

    return this;
  }

  /**
   * Remove query parameters from uri object
   * @return this helper with updated uri object
   * @throws URISyntaxException if updated URI has invalid parameters
   */
  public UriHelper clearQueryParameters() throws URISyntaxException {
    uri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, uri.getFragment());

    return this;
  }

  /**
   * Return uri on string format
   * @return uri on string format
   */
  @Override
  public String toString() {
    return uri.toString();
  }
}
