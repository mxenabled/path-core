package com.mx.path.core.common.exception;

/**
 * A PathRequestException wrapper.
 *
 * <p>This exception type would indicate that something unexpected happened and the cause should be inspected, since
 * this exception would give much contextual information. Commonly used in behaviors and outside the gateway
 * to standardize exception handling.
 *
 * <p>Example:
 *
 * <p><pre>{@code
 *   try {
 *     try {
 *       setupWebRequest(); // this could throw any kind of error
 *       gateway.status();
 *     } catch (PathRequestException e) {
 *       throw e;
 *     } catch (Exception e) {
 *       // Wrap the exception and set status and user message.
 *       throw new PathRequestExceptionWrapper(e.getMessage(), e)
 *         .withStatus(PathResponseStatus.INTERNAL_ERROR)
 *         .withUserMessage("An unexpected gateway error occurred");
 *     }
 *   } catch (PathRequestException exception) {
 *     // This exception handler will catch all request exceptions and unexpected
 *     System.out.println("An error occurred with status: " + exception.getStatus());
 *     System.out.println(exception.getUserMessage());
 *   }
 * }</pre>
 */
public class PathRequestExceptionWrapper extends PathRequestException {
  /**
   * Default constructor.
   */
  public PathRequestExceptionWrapper() {
    super();
  }

  /**
   * Build new {@link PathRequestExceptionWrapper} with specified cause.
   *
   * @param cause cause
   */
  public PathRequestExceptionWrapper(Throwable cause) {
    super(cause);
  }

  /**
   * Build new {@link PathRequestExceptionWrapper} with specified description message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public PathRequestExceptionWrapper(String message, Throwable cause) {
    super(message, cause);
  }
}
