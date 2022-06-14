package com.mx.path.utilities.net;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.Getter;

/**
 * Utility class for building and modifying URIs.
 */
public final class UriHelper {
  @Getter
  private URI uri;

  public UriHelper(String uri) throws URISyntaxException {
    this.uri = new URI(uri);
  }

  public UriHelper(URI uri) {
    this.uri = uri;
  }

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

  public UriHelper clearQueryParameters() throws URISyntaxException {
    uri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, uri.getFragment());

    return this;
  }

  @Override
  public String toString() {
    return uri.toString();
  }
}
