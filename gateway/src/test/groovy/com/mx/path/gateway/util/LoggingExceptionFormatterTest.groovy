package com.mx.path.gateway.util

import com.mx.common.http.HttpStatus

import spock.lang.Specification

class LoggingExceptionFormatterTest extends Specification {
  def "formatLoggingException()"() {
    given:
    def exception = new MdxApiException("Some failure occurred!", HttpStatus.BAD_REQUEST, false, null);

    when:
    def result = LoggingExceptionFormatter.formatLoggingException(exception)

    then:
    result == "com.mx.path.gateway.util.MdxApiException: Some failure occurred!"

    when:
    result = LoggingExceptionFormatter.formatLoggingException(new MdxApiException("Another failure!", HttpStatus.UNPROCESSABLE_ENTITY, false, new Exception("Something broke internally")))

    then:
    result == "com.mx.path.gateway.util.MdxApiException: Another failure!\n\tCaused by: java.lang.Exception: Something broke internally"
  }

  def "formatLoggingExceptionWithStacktrace()"() {
    given:
    def exception = new MdxApiException("Some failure occurred!", HttpStatus.BAD_REQUEST, false, null);

    when:
    def result = LoggingExceptionFormatter.formatLoggingExceptionWithStacktrace(exception)

    then:
    result == "com.mx.path.gateway.util.MdxApiException: Some failure occurred!\n" +
        "\tsun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)\n" +
        "\t\t sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)\n" +
        "\t\t sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)\n" +
        "\t\t java.lang.reflect.Constructor.newInstance(Constructor.java:423)\n" +
        "\t\t org.codehaus.groovy.reflection.CachedConstructor.invoke(CachedConstructor.java:73)\n" +
        "\t\t org.codehaus.groovy.runtime.callsite.ConstructorSite\$ConstructorSiteNoUnwrapNoCoerce.callConstructor(ConstructorSite.java:108)\n" +
        "\t\t org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallConstructor(CallSiteArray.java:58)\n" +
        "\t\t org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:263)\n" +
        "\t\t org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:304)\n" +
        "\t\t com.mx.path.gateway.util.LoggingExceptionFormatterTest.\$spock_feature_0_1(LoggingExceptionFormatterTest.groovy:27)\n" +
        "\t\t sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
        "\t\t sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)"

    when:
    result = LoggingExceptionFormatter.formatLoggingExceptionWithStacktrace(new MdxApiException("Another failure!", HttpStatus.UNPROCESSABLE_ENTITY, false, new Exception("Something broke internally")))

    then:
    result == "com.mx.path.gateway.util.MdxApiException: Another failure!\n" +
        "\tsun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)\n" +
        "\t\t sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)\n" +
        "\t\t sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)\n" +
        "\t\t java.lang.reflect.Constructor.newInstance(Constructor.java:423)\n" +
        "\t\t org.codehaus.groovy.reflection.CachedConstructor.invoke(CachedConstructor.java:73)\n" +
        "\t\t org.codehaus.groovy.runtime.callsite.ConstructorSite\$ConstructorSiteNoUnwrapNoCoerce.callConstructor(ConstructorSite.java:108)\n" +
        "\t\t org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallConstructor(CallSiteArray.java:58)\n" +
        "\t\t org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:263)\n" +
        "\t\t org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:304)\n" +
        "\t\t com.mx.path.gateway.util.LoggingExceptionFormatterTest.\$spock_feature_0_1(LoggingExceptionFormatterTest.groovy:48)\n" +
        "\t\t sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
        "\t\t sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" +
        "\tCaused by: java.lang.Exception: Something broke internally\n" +
        "\t\tsun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)\n" +
        "\t\t\t sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)\n" +
        "\t\t\t sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)\n" +
        "\t\t\t java.lang.reflect.Constructor.newInstance(Constructor.java:423)\n" +
        "\t\t\t org.codehaus.groovy.reflection.CachedConstructor.invoke(CachedConstructor.java:73)\n" +
        "\t\t\t org.codehaus.groovy.runtime.callsite.ConstructorSite\$ConstructorSiteNoUnwrapNoCoerce.callConstructor(ConstructorSite.java:108)\n" +
        "\t\t\t org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallConstructor(CallSiteArray.java:58)\n" +
        "\t\t\t org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:263)\n" +
        "\t\t\t org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:277)\n" +
        "\t\t\t com.mx.path.gateway.util.LoggingExceptionFormatterTest.\$spock_feature_0_1(LoggingExceptionFormatterTest.groovy:48)\n" +
        "\t\t\t sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
        "\t\t\t sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)"
  }
}
