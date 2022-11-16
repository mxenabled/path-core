package com.mx.common.http

import com.mx.common.accessors.PathResponseStatus

import spock.lang.Specification
import spock.lang.Unroll

class HttpStatusTest extends Specification {
  @Unroll
  def "test toPathResponseStatus"() {
    when:
    def result = httpStatus.toPathResponseStatus()

    then:
    result == pathResponseStatus

    where:
    httpStatus                                           || pathResponseStatus
    HttpStatus.CONTINUE                                  || PathResponseStatus.UNAVAILABLE
    HttpStatus.SWITCHING_PROTOCOLS                       || PathResponseStatus.UNAVAILABLE
    HttpStatus.PROCESSING                                || PathResponseStatus.OK
    HttpStatus.CHECKPOINT                                || PathResponseStatus.UNAVAILABLE
    HttpStatus.OK                                        || PathResponseStatus.OK
    HttpStatus.CREATED                                   || PathResponseStatus.OK
    HttpStatus.ACCEPTED                                  || PathResponseStatus.ACCEPTED
    HttpStatus.NON_AUTHORITATIVE_INFORMATION             || PathResponseStatus.UNAVAILABLE
    HttpStatus.NO_CONTENT                                || PathResponseStatus.NO_CONTENT
    HttpStatus.RESET_CONTENT                             || PathResponseStatus.UNAVAILABLE
    HttpStatus.PARTIAL_CONTENT                           || PathResponseStatus.UNAVAILABLE
    HttpStatus.MULTI_STATUS                              || PathResponseStatus.UNAVAILABLE
    HttpStatus.ALREADY_REPORTED                          || PathResponseStatus.UNAVAILABLE
    HttpStatus.IM_USED                                   || PathResponseStatus.UNAVAILABLE
    HttpStatus.MULTIPLE_CHOICES                          || PathResponseStatus.UNAVAILABLE
    HttpStatus.MOVED_PERMANENTLY                         || PathResponseStatus.NOT_FOUND
    HttpStatus.FOUND                                     || PathResponseStatus.OK
    HttpStatus.MOVED_TEMPORARILY                         || PathResponseStatus.NOT_FOUND
    HttpStatus.SEE_OTHER                                 || PathResponseStatus.UNAVAILABLE
    HttpStatus.NOT_MODIFIED                              || PathResponseStatus.UNAVAILABLE
    HttpStatus.USE_PROXY                                 || PathResponseStatus.UNAVAILABLE
    HttpStatus.TEMPORARY_REDIRECT                        || PathResponseStatus.UNAVAILABLE
    HttpStatus.PERMANENT_REDIRECT                        || PathResponseStatus.UNAVAILABLE
    HttpStatus.BAD_REQUEST                               || PathResponseStatus.BAD_REQUEST
    HttpStatus.UNAUTHORIZED                              || PathResponseStatus.UNAUTHORIZED
    HttpStatus.PAYMENT_REQUIRED                          || PathResponseStatus.UNAUTHORIZED
    HttpStatus.FORBIDDEN                                 || PathResponseStatus.NOT_ALLOWED
    HttpStatus.NOT_FOUND                                 || PathResponseStatus.NOT_FOUND
    HttpStatus.METHOD_NOT_ALLOWED                        || PathResponseStatus.NOT_ALLOWED
    HttpStatus.NOT_ACCEPTABLE                            || PathResponseStatus.USER_ERROR
    HttpStatus.PROXY_AUTHENTICATION_REQUIRED             || PathResponseStatus.UNAUTHORIZED
    HttpStatus.REQUEST_TIMEOUT                           || PathResponseStatus.TIMEOUT
    HttpStatus.CONFLICT                                  || PathResponseStatus.NOT_ALLOWED
    HttpStatus.GONE                                      || PathResponseStatus.NOT_FOUND
    HttpStatus.LENGTH_REQUIRED                           || PathResponseStatus.BAD_REQUEST
    HttpStatus.PRECONDITION_FAILED                       || PathResponseStatus.USER_ERROR
    HttpStatus.PAYLOAD_TOO_LARGE                         || PathResponseStatus.BAD_REQUEST
    HttpStatus.REQUEST_ENTITY_TOO_LARGE                  || PathResponseStatus.BAD_REQUEST
    HttpStatus.URI_TOO_LONG                              || PathResponseStatus.BAD_REQUEST
    HttpStatus.REQUEST_URI_TOO_LONG                      || PathResponseStatus.BAD_REQUEST
    HttpStatus.UNSUPPORTED_MEDIA_TYPE                    || PathResponseStatus.BAD_REQUEST
    HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE           || PathResponseStatus.USER_ERROR
    HttpStatus.EXPECTATION_FAILED                        || PathResponseStatus.USER_ERROR
    HttpStatus.I_AM_A_TEAPOT                             || PathResponseStatus.OK
    HttpStatus.INSUFFICIENT_SPACE_ON_RESOURCE            || PathResponseStatus.INTERNAL_ERROR
    HttpStatus.METHOD_FAILURE                            || PathResponseStatus.INTERNAL_ERROR
    HttpStatus.DESTINATION_LOCKED                        || PathResponseStatus.NOT_ALLOWED
    HttpStatus.UNPROCESSABLE_ENTITY                      || PathResponseStatus.USER_ERROR
    HttpStatus.LOCKED                                    || PathResponseStatus.NOT_ALLOWED
    HttpStatus.FAILED_DEPENDENCY                         || PathResponseStatus.USER_ERROR
    HttpStatus.TOO_EARLY                                 || PathResponseStatus.NOT_ALLOWED
    HttpStatus.UPGRADE_REQUIRED                          || PathResponseStatus.USER_ERROR
    HttpStatus.PRECONDITION_REQUIRED                     || PathResponseStatus.USER_ERROR
    HttpStatus.TOO_MANY_REQUESTS                         || PathResponseStatus.TOO_MANY_REQUESTS
    HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE           || PathResponseStatus.BAD_REQUEST
    HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS             || PathResponseStatus.NOT_ALLOWED
    HttpStatus.INTERNAL_SERVER_ERROR                     || PathResponseStatus.INTERNAL_ERROR
    HttpStatus.NOT_IMPLEMENTED                           || PathResponseStatus.NOT_IMPLEMENTED
    HttpStatus.BAD_GATEWAY                               || PathResponseStatus.UNAVAILABLE
    HttpStatus.SERVICE_UNAVAILABLE                       || PathResponseStatus.UNAVAILABLE
    HttpStatus.GATEWAY_TIMEOUT                           || PathResponseStatus.TIMEOUT
    HttpStatus.HTTP_VERSION_NOT_SUPPORTED                || PathResponseStatus.BAD_REQUEST
    HttpStatus.VARIANT_ALSO_NEGOTIATES                   || PathResponseStatus.NOT_ALLOWED
    HttpStatus.INSUFFICIENT_STORAGE                      || PathResponseStatus.INTERNAL_ERROR
    HttpStatus.LOOP_DETECTED                             || PathResponseStatus.INTERNAL_ERROR
    HttpStatus.BANDWIDTH_LIMIT_EXCEEDED                  || PathResponseStatus.OK
    HttpStatus.NOT_EXTENDED                              || PathResponseStatus.INTERNAL_ERROR
    HttpStatus.NETWORK_AUTHENTICATION_REQUIRED           || PathResponseStatus.NOT_ALLOWED
    HttpStatus.UPSTREAM_SERVICE_UNAVAILABLE              || PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE
  }
}
