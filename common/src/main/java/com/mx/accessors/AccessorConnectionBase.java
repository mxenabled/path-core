package com.mx.accessors;

import java.util.LinkedList;
import java.util.List;

import com.mx.common.connect.AccessorConnectionSettings;
import com.mx.common.connect.RequestFilter;

/**
 * todo: Move back to gateway after model extraction
 * @param <REQ>
 */
public abstract class AccessorConnectionBase<REQ> extends AccessorConnectionSettings {

  /**
   * The first RequestFilter in linked chain. Managed by connection and used by HttpRequest to execute.
   */
  private volatile RequestFilter filterChain;

  /**
   * Implement to provide filters specific to connection type. At least one filter should be provided that executes the
   * request and sets the response body and other information about the result.
   * @return list of connection-specific request filters
   */
  public abstract List<RequestFilter> connectionRequestFilters();

  /**
   * Override to create and configure a new REQ
   * @param path
   * @return new Request of type REQ
   */
  public abstract REQ request(String path);

  /**
   * Builds and caches filterChain.
   * @return linked filter chain
   */
  protected RequestFilter buildFilterChain() {
    if (filterChain == null) {
      synchronized (getClass()) {
        if (filterChain == null) {
          List<RequestFilter> requestFilters = new LinkedList<>();
          if (this.getBaseRequestFilters() != null) {
            requestFilters.addAll(this.getBaseRequestFilters());
          }
          requestFilters.addAll(connectionRequestFilters());

          RequestFilter last = null;
          for (RequestFilter current : requestFilters) {
            if (last == null) {
              last = current;
              continue;
            }
            last.setNext(current);
            current.setNext(null);
            last = current;
          }
          filterChain = requestFilters.get(0);
        }
      }
    }

    return filterChain;
  }
}
