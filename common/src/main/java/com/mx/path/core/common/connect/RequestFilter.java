package com.mx.path.core.common.connect;

/**
 * Notation for request filters.
 */
public interface RequestFilter {

  // Public Methods

  /**
   * Execute.
   *
   * @param request request
   * @param response response
   */
  void execute(Request request, Response response);

  /**
   * Get next request filter.
   *
   * @return nest request filter
   */
  RequestFilter getNext();

  /**
   * Get and execute next request filter.
   *
   * @param request request
   * @param response response
   */
  default void next(Request request, Response response) {
    if (getNext() != null) {
      getNext().execute(request, response);
    }
  }

  /**
   * Set next request filter.
   *
   * @param filter next request filter
   */
  void setNext(RequestFilter filter);
}
