package com.mx.path.core.common.connect;

import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.google.common.base.Predicates;
import com.mx.path.core.common.configuration.ConfigurationField;
import com.mx.path.core.common.http.HttpStatus;

/**
 * Rule for matching a {@link Response}, based on attributes. Also provides a way to provide a {@link Predicate} so
 * code can be executed with the response to determine if it matches.
 *
 * <p>When all present conditions are satisfied, the rule will match the {@link Response}
 */
@SuppressWarnings("PMD.UnusedPrivateMethod")
@NoArgsConstructor
public class ResponseMatcher<T extends Response<?, ?>> implements Predicate<T> {

  /**
   * When present, matches if this type is assignable from {@link Response#getException()} class.
   */
  @ConfigurationField
  private Class<? extends Throwable> exception;

  /**
   * -- GETTER --
   * Return predicate instance.
   *
   * @return predicate instance
   */
  @Getter(value = AccessLevel.PRIVATE)
  private transient Predicate<T> instance;

  /**
   * Code-provided predicate used to evaluate response for match.
   */
  private transient Predicate<T> predicate;

  /**
   * When present, matches if this status equals {@link Response#getStatus()}
   */
  @ConfigurationField
  private HttpStatus status;

  @Builder
  private static <T extends Response<?, ?>> ResponseMatcher<T> of(Class<? extends Throwable> exception, Predicate<? extends Response<?, ?>> predicate, HttpStatus status) {
    ResponseMatcher<T> rule = new ResponseMatcher<>();
    rule.exception = exception;
    rule.predicate = (Predicate<T>) predicate;
    rule.status = status;

    return rule;
  }

  /**
   * Test instance of {@link Request} {@link T} against this matcher.
   *
   *
   * @param request instance of request
   * @return true, if all conditions match
   */
  @Override
  public boolean test(T request) {
    return predicate().test(request);
  }

  /**
   * Gets instance of {@link Predicate}. Builds if it doesn't exist.
   *
   * @return new or existing instance of {@link Predicate}
   */
  @SuppressWarnings("unchecked")
  private Predicate<T> predicate() {
    if (getInstance() != null) {
      return (Predicate<T>) getInstance();
    }

    buildExceptionCondition();
    buildStatusCondition();
    buildPredicateCondition();

    return instanceOrDefault();
  }

  private void andPredicate(Predicate<T> another) {
    if (getInstance() == null) {
      instance = another;
    } else {
      instance = Predicates.and(getInstance()::test, another::test);
    }
  }

  private void buildExceptionCondition() {
    if (exception != null) {
      andPredicate((response) -> {
        if (response == null || response.getException() == null) {
          return false;
        }

        return exception.isAssignableFrom(response.getException().getClass());
      });
    }
  }

  private void buildPredicateCondition() {
    if (predicate != null) {
      andPredicate((Predicate<T>) predicate);
    }
  }

  private void buildStatusCondition() {
    if (status != null) {
      andPredicate((response) -> {
        if (response == null || response.getStatus() == null) {
          return false;
        }

        return status == response.getStatus();
      });
    }
  }

  @SuppressWarnings("unchecked")
  private Predicate<T> instanceOrDefault() {
    if (getInstance() != null) {
      return (Predicate<T>) getInstance();
    }

    // No conditions set, don't reject any responses
    return (Predicate<T>) (t -> false);
  }
}
