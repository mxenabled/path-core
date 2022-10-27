/**
 * Base classes and other helpers for Path Exceptions
 *
 * <p>Throwing the correct exception is vital for the Path system to behave in an expected way. The use of more specific exceptions is encouraged.
 *
 * <p><strong>Path Exception Categories</strong>
 *
 * <p><i>System</i>
 *
 * <p>{@link com.mx.common.exception.PathSystemException} type exceptions are typically thrown at application boot time, when the Path gateway is being configured for use. It is mostly used to convey configuration issues or unexpected states.
 *
 * <p>Example:
 * <pre>{@code
 *   Gateway gateway;
 *   try {
 *     gateway = new Configurator().buildFromYaml(yamlConfiguration);
 *   } catch (PathSystemException e) {
 *     throw new RuntimeException("Unable to initialize the Path Gateway. Unable to continue.", e)
 *   }
 * }</pre>
 *
 * <p><i>Request</i>
 *
 * <p>{@link com.mx.common.exception.PathRequestException} type exceptions are thrown on errors when _using_ the Path Gateway.
 *
 * <p>Example:
 * <pre>{@code
 *   try{
 *     gateway.accounts().get("A-1234");
 *   } catch (PathRequestException e) {
 *     throw new Exception("Unable to load account A-1234", e);
 *   }
 * }</pre>
 *
 * <p>All request-type exceptions are children of {@link com.mx.common.exception.PathRequestException}. When choosing the correct exception to throw in a given situation, it is better to use the more specific exceptions and avoid using the high-level exceptions like {@link com.mx.common.exception.PathRequestException } or {@link com.mx.common.accessors.AccessorException }. If an appropriate exception does not exist, the creation of a new exception is encouraged, and the new exception should derive from the appropriate, existing exception. If the situation is likely to appear in other accessors, we may want to add the exception to the SDK for use in other accessors. If the situation is very specific to the current accessor, a custom exception can be created and kept in accessor repo.
 *
 * <p>Example:
 * <pre>{@code
 *   import com.mx.common.accessors.PathResponseStatus;
 *   import com.mx.common.accessors.AccessorUserException;
 *
 *   public class AccountLockedException extends AccessorUserException {
 *     public AccountLockedException() {
 *       super("User's account is locked", "Your account is locked, please contact customer support.", PathResponseStatus.NOT_ALLOWED);
 *     }
 *   }
 * }</pre>
 */
package com.mx.common.exception;
