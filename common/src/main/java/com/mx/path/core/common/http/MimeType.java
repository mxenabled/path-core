package com.mx.path.core.common.http;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Nullable;

/**
 * Represents a MIME Type, as originally defined in RFC 2046 and subsequently
 * used in other Internet protocols including HTTP.
 *
 * <p>This class, however, does not contain support for the q-parameters used
 * in HTTP content negotiation. Those can be found in the subclass
 * {@code org.springframework.http.MediaType} in the {@code spring-web} module.
 *
 * <p>Consists of a {@linkplain #getType() type} and a {@linkplain #getSubtype() subtype}.
 * Also has functionality to parse MIME Type values from a {@code String} using
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @author Rossen Stoyanchev
 * @author Sam Brannen
 * @since 4.0
 *
 * Based on:
 * https://github.com/spring-projects/spring-framework/blob/main/spring-core/src/main/java/org/springframework/util/MimeType.java
 */
@SuppressWarnings({ "checkstyle:MagicNumber", "checkstyle:DesignForExtension", "checkstyle:OperatorWrap", "PMD.CyclomaticComplexity", "PMD.UselessParentheses" })
public class MimeType implements Comparable<MimeType>, Serializable {

  private static final long serialVersionUID = 4085923477777865903L;

  protected static final String WILDCARD_TYPE = "*";

  private static final String PARAM_CHARSET = "charset";

  private static final BitSet TOKEN;

  static {
    // variable names refer to RFC 2616, section 2.2
    BitSet ctl = new BitSet(128);
    for (int i = 0; i <= 31; i++) {
      ctl.set(i);
    }
    ctl.set(127);

    BitSet separators = new BitSet(128);
    separators.set('(');
    separators.set(')');
    separators.set('<');
    separators.set('>');
    separators.set('@');
    separators.set(',');
    separators.set(';');
    separators.set(':');
    separators.set('\\');
    separators.set('\"');
    separators.set('/');
    separators.set('[');
    separators.set(']');
    separators.set('?');
    separators.set('=');
    separators.set('{');
    separators.set('}');
    separators.set(' ');
    separators.set('\t');

    TOKEN = new BitSet(128);
    TOKEN.set(0, 128);
    TOKEN.andNot(ctl);
    TOKEN.andNot(separators);
  }

  /**
   * Determine if the given objects are equal, returning {@code true} if
   * both are {@code null} or {@code false} if only one is {@code null}.
   * <p>Compares arrays with {@code Arrays.equals}, performing an equality
   * check based on the array elements rather than the array reference.
   * @param o1 first Object to compare
   * @param o2 second Object to compare
   * @return whether the given objects are equal
   * @see Object#equals(Object)
   * @see Arrays#equals
   */
  private static boolean nullSafeEquals(Object o1, Object o2) {
    if (o1 == o2) {
      return true;
    }
    if (o1 == null || o2 == null) {
      return false;
    }
    if (o1.equals(o2)) {
      return true;
    }
    if (o1.getClass().isArray() && o2.getClass().isArray()) {
      return arrayEquals(o1, o2);
    }
    return false;
  }

  /**
   * Compare the given arrays with {@code Arrays.equals}, performing an equality
   * check based on the array elements rather than the array reference.
   * @param o1 first array to compare
   * @param o2 second array to compare
   * @return whether the given objects are equal
   * @see #nullSafeEquals(Object, Object)
   * @see Arrays#equals
   */
  private static boolean arrayEquals(Object o1, Object o2) {
    if (o1 instanceof Object[] && o2 instanceof Object[]) {
      return Arrays.equals((Object[]) o1, (Object[]) o2);
    }
    if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
      return Arrays.equals((boolean[]) o1, (boolean[]) o2);
    }
    if (o1 instanceof byte[] && o2 instanceof byte[]) {
      return Arrays.equals((byte[]) o1, (byte[]) o2);
    }
    if (o1 instanceof char[] && o2 instanceof char[]) {
      return Arrays.equals((char[]) o1, (char[]) o2);
    }
    if (o1 instanceof double[] && o2 instanceof double[]) {
      return Arrays.equals((double[]) o1, (double[]) o2);
    }
    if (o1 instanceof float[] && o2 instanceof float[]) {
      return Arrays.equals((float[]) o1, (float[]) o2);
    }
    if (o1 instanceof int[] && o2 instanceof int[]) {
      return Arrays.equals((int[]) o1, (int[]) o2);
    }
    if (o1 instanceof long[] && o2 instanceof long[]) {
      return Arrays.equals((long[]) o1, (long[]) o2);
    }
    if (o1 instanceof short[] && o2 instanceof short[]) {
      return Arrays.equals((short[]) o1, (short[]) o2);
    }
    return false;
  }

  private final String type;

  private final String subtype;

  private final Map<String, String> parameters;

  @Nullable
  private transient Charset resolvedCharset;

  @Nullable
  private volatile String toStringValue;

  /**
   * Create a new {@code MimeType} for the given primary type.
   * <p>The {@linkplain #getSubtype() subtype} is set to <code>"&#42;"</code>,
   * and the parameters are empty.
   * @param type the primary type
   * @throws IllegalArgumentException if any of the parameters contains illegal characters
   */
  public MimeType(String type) {
    this(type, WILDCARD_TYPE);
  }

  /**
   * Create a new {@code MimeType} for the given primary type and subtype.
   * <p>The parameters are empty.
   * @param type the primary type
   * @param subtype the subtype
   * @throws IllegalArgumentException if any of the parameters contains illegal characters
   */
  public MimeType(String type, String subtype) {
    this(type, subtype, Collections.emptyMap());
  }

  /**
   * Create a new {@code MimeType} for the given type, subtype, and character set.
   * @param type the primary type
   * @param subtype the subtype
   * @param charset the character set
   * @throws IllegalArgumentException if any of the parameters contains illegal characters
   */
  public MimeType(String type, String subtype, Charset charset) {
    this(type, subtype, Collections.singletonMap(PARAM_CHARSET, charset.name()));
    this.resolvedCharset = charset;
  }

  /**
   * Copy-constructor that copies the type, subtype, parameters of the given {@code MimeType},
   * and allows to set the specified character set.
   * @param other the other MimeType
   * @param charset the character set
   * @throws IllegalArgumentException if any of the parameters contains illegal characters
   * @since 4.3
   */
  public MimeType(MimeType other, Charset charset) {
    this(other.getType(), other.getSubtype(), addCharsetParameter(charset, other.getParameters()));
    this.resolvedCharset = charset;
  }

  /**
   * Copy-constructor that copies the type and subtype of the given {@code MimeType},
   * and allows for different parameter.
   * @param other the other MimeType
   * @param parameters the parameters (may be {@code null})
   * @throws IllegalArgumentException if any of the parameters contains illegal characters
   */
  public MimeType(MimeType other, @Nullable Map<String, String> parameters) {
    this(other.getType(), other.getSubtype(), parameters);
  }

  /**
   * Create a new {@code MimeType} for the given type, subtype, and parameters.
   * @param type the primary type
   * @param subtype the subtype
   * @param parameters the parameters (may be {@code null})
   * @throws IllegalArgumentException if any of the parameters contains illegal characters
   */
  public MimeType(String type, String subtype, @Nullable Map<String, String> parameters) {
    checkToken(type);
    checkToken(subtype);
    this.type = type.toLowerCase(Locale.ENGLISH);
    this.subtype = subtype.toLowerCase(Locale.ENGLISH);
    if (parameters != null && !parameters.isEmpty()) {
      Map<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
      parameters.forEach((parameter, value) -> {
        checkParameters(parameter, value);
        map.put(parameter, value);
      });
      this.parameters = Collections.unmodifiableMap(map);
    } else {
      this.parameters = Collections.emptyMap();
    }
  }

  /**
   * Checks the given token string for illegal characters, as defined in RFC 2616,
   * section 2.2.
   * @throws IllegalArgumentException in case of illegal characters
   * @see <a href="https://tools.ietf.org/html/rfc2616#section-2.2">HTTP 1.1, section 2.2</a>
   */
  private void checkToken(String token) {
    for (int i = 0; i < token.length(); i++) {
      char ch = token.charAt(i);
      if (!TOKEN.get(ch)) {
        throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + token + "\"");
      }
    }
  }

  protected void checkParameters(String parameter, String value) {
    checkToken(parameter);
    if (PARAM_CHARSET.equals(parameter)) {
      if (this.resolvedCharset == null) {
        this.resolvedCharset = Charset.forName(unquote(value));
      }
    } else if (!isQuotedString(value)) {
      checkToken(value);
    }
  }

  private boolean isQuotedString(String s) {
    if (s.length() < 2) {
      return false;
    } else {
      return ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")));
    }
  }

  protected final String unquote(String s) {
    return (isQuotedString(s) ? s.substring(1, s.length() - 1) : s);
  }

  /**
   * Indicates whether the {@linkplain #getType() type} is the wildcard character
   * <code>&#42;</code> or not.
   *
   * @return true if is specified wildcard character, false otherwise
   */
  public boolean isWildcardType() {
    return WILDCARD_TYPE.equals(getType());
  }

  /**
   * Indicates whether the {@linkplain #getSubtype() subtype} is the wildcard
   * character <code>&#42;</code> or the wildcard character followed by a suffix
   * (e.g. <code>&#42;+xml</code>).
   * @return whether the subtype is a wildcard
   */
  public boolean isWildcardSubtype() {
    return WILDCARD_TYPE.equals(getSubtype()) || getSubtype().startsWith("*+");
  }

  /**
   * Return the primary type.
   *
   * @return type
   */
  public String getType() {
    return this.type;
  }

  /**
   * Return the subtype.
   *
   * @return subtype
   */
  public String getSubtype() {
    return this.subtype;
  }

  /**
   * Return the subtype suffix as defined in RFC 6839.
   * @since 5.3
   *
   * @return subtype suffix
   */
  @Nullable
  public String getSubtypeSuffix() {
    int suffixIndex = this.subtype.lastIndexOf('+');
    if (suffixIndex != -1 && this.subtype.length() > suffixIndex) {
      return this.subtype.substring(suffixIndex + 1);
    }
    return null;
  }

  /**
   * Return the character set, as indicated by a {@code charset} parameter, if any.
   * @return the character set, or {@code null} if not available
   * @since 4.3
   */
  @Nullable
  public Charset getCharset() {
    return this.resolvedCharset;
  }

  /**
   * Return a generic parameter value, given a parameter name.
   * @param name the parameter name
   * @return the parameter value, or {@code null} if not present
   */
  @Nullable
  public final String getParameter(String name) {
    return this.parameters.get(name);
  }

  /**
   * Return all generic parameter values.
   * @return a read-only map (possibly empty, never {@code null})
   */
  public Map<String, String> getParameters() {
    return this.parameters;
  }

  /**
   * Indicate whether this MIME Type includes the given MIME Type.
   * <p>For instance, {@code text/*} includes {@code text/plain} and {@code text/html},
   * and {@code application/*+xml} includes {@code application/soap+xml}, etc.
   * This method is <b>not</b> symmetric.
   * @param other the reference MIME Type with which to compare
   * @return {@code true} if this MIME Type includes the given MIME Type;
   * {@code false} otherwise
   */
  public boolean includes(@Nullable MimeType other) {
    if (other == null) {
      return false;
    }
    if (isWildcardType()) {
      // */* includes anything
      return true;
    } else if (getType().equals(other.getType())) {
      if (getSubtype().equals(other.getSubtype())) {
        return true;
      }
      if (isWildcardSubtype()) {
        // Wildcard with suffix, e.g. application/*+xml
        int thisPlusIdx = getSubtype().lastIndexOf('+');
        if (thisPlusIdx == -1) {
          return true;
        } else {
          // application/*+xml includes application/soap+xml
          int otherPlusIdx = other.getSubtype().lastIndexOf('+');
          if (otherPlusIdx != -1) {
            String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
            String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
            String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
            if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && WILDCARD_TYPE.equals(thisSubtypeNoSuffix)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  /**
   * Indicate whether this MIME Type is compatible with the given MIME Type.
   * <p>For instance, {@code text/*} is compatible with {@code text/plain},
   * {@code text/html}, and vice versa. In effect, this method is similar to
   * {@link #includes}, except that it <b>is</b> symmetric.
   * @param other the reference MIME Type with which to compare
   * @return {@code true} if this MIME Type is compatible with the given MIME Type;
   * {@code false} otherwise
   */
  public boolean isCompatibleWith(@Nullable MimeType other) {
    if (other == null) {
      return false;
    }
    if (isWildcardType() || other.isWildcardType()) {
      return true;
    } else if (getType().equals(other.getType())) {
      if (getSubtype().equals(other.getSubtype())) {
        return true;
      }
      if (isWildcardSubtype() || other.isWildcardSubtype()) {
        String thisSuffix = getSubtypeSuffix();
        String otherSuffix = other.getSubtypeSuffix();
        if (getSubtype().equals(WILDCARD_TYPE) || other.getSubtype().equals(WILDCARD_TYPE)) {
          return true;
        } else if (isWildcardSubtype() && thisSuffix != null) {
          return (thisSuffix.equals(other.getSubtype()) || thisSuffix.equals(otherSuffix));
        } else if (other.isWildcardSubtype() && otherSuffix != null) {
          return (this.getSubtype().equals(otherSuffix) || otherSuffix.equals(thisSuffix));
        }
      }
    }
    return false;
  }

  /**
   * Similar to {@link #equals(Object)} but based on the type and subtype
   * only, i.e. ignoring parameters.
   * @param other the other mime type to compare to
   * @return whether the two mime types have the same type and subtype
   * @since 5.1.4
   */
  public boolean equalsTypeAndSubtype(@Nullable MimeType other) {
    if (other == null) {
      return false;
    }
    return this.type.equalsIgnoreCase(other.type) && this.subtype.equalsIgnoreCase(other.subtype);
  }

  @Override
  public boolean equals(@Nullable Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof MimeType)) {
      return false;
    }
    MimeType otherType = (MimeType) other;
    return (this.type.equalsIgnoreCase(otherType.type) &&
        this.subtype.equalsIgnoreCase(otherType.subtype) &&
        parametersAreEqual(otherType));
  }

  /**
   * Determine if the parameters in this {@code MimeType} and the supplied
   * {@code MimeType} are equal, performing case-insensitive comparisons
   * for {@link Charset Charsets}.
   * @since 4.2
   */
  private boolean parametersAreEqual(MimeType other) {
    if (this.parameters.size() != other.parameters.size()) {
      return false;
    }

    for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
      String key = entry.getKey();
      if (!other.parameters.containsKey(key)) {
        return false;
      }
      if (PARAM_CHARSET.equals(key)) {
        if (!nullSafeEquals(getCharset(), other.getCharset())) {
          return false;
        }
      } else if (!nullSafeEquals(entry.getValue(), other.parameters.get(key))) {
        return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = this.type.hashCode();
    result = 31 * result + this.subtype.hashCode();
    result = 31 * result + this.parameters.hashCode();
    return result;
  }

  @Override
  public String toString() {
    String value = this.toStringValue;
    if (value == null) {
      StringBuilder builder = new StringBuilder();
      appendTo(builder);
      value = builder.toString();
      this.toStringValue = value;
    }
    return value;
  }

  protected void appendTo(StringBuilder builder) {
    builder.append(this.type);
    builder.append('/');
    builder.append(this.subtype);
    appendTo(this.parameters, builder);
  }

  private void appendTo(Map<String, String> map, StringBuilder builder) {
    map.forEach((key, val) -> {
      builder.append(';');
      builder.append(key);
      builder.append('=');
      builder.append(val);
    });
  }

  /**
   * Compares this MIME Type to another alphabetically.
   * @param other the MIME Type to compare to
   */
  @Override
  public int compareTo(MimeType other) {
    int comp = getType().compareToIgnoreCase(other.getType());
    if (comp != 0) {
      return comp;
    }
    comp = getSubtype().compareToIgnoreCase(other.getSubtype());
    if (comp != 0) {
      return comp;
    }
    comp = getParameters().size() - other.getParameters().size();
    if (comp != 0) {
      return comp;
    }

    TreeSet<String> thisAttributes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    thisAttributes.addAll(getParameters().keySet());
    TreeSet<String> otherAttributes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    otherAttributes.addAll(other.getParameters().keySet());
    Iterator<String> thisAttributesIterator = thisAttributes.iterator();
    Iterator<String> otherAttributesIterator = otherAttributes.iterator();

    while (thisAttributesIterator.hasNext()) {
      String thisAttribute = thisAttributesIterator.next();
      String otherAttribute = otherAttributesIterator.next();
      comp = thisAttribute.compareToIgnoreCase(otherAttribute);
      if (comp != 0) {
        return comp;
      }
      if (PARAM_CHARSET.equals(thisAttribute)) {
        Charset thisCharset = getCharset();
        Charset otherCharset = other.getCharset();
        if (thisCharset != otherCharset) {
          if (thisCharset == null) {
            return -1;
          }
          if (otherCharset == null) {
            return 1;
          }
          comp = thisCharset.compareTo(otherCharset);
          if (comp != 0) {
            return comp;
          }
        }
      } else {
        String thisValue = getParameters().get(thisAttribute);
        String otherValue = other.getParameters().get(otherAttribute);
        if (otherValue == null) {
          otherValue = "";
        }
        comp = thisValue.compareTo(otherValue);
        if (comp != 0) {
          return comp;
        }
      }
    }

    return 0;
  }

  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    // Rely on default serialization, just initialize state after deserialization.
    ois.defaultReadObject();

    // Initialize transient fields.
    String charsetName = getParameter(PARAM_CHARSET);
    if (charsetName != null) {
      this.resolvedCharset = Charset.forName(unquote(charsetName));
    }
  }

  private static Map<String, String> addCharsetParameter(Charset charset, Map<String, String> parameters) {
    Map<String, String> map = new LinkedHashMap<>(parameters);
    map.put(PARAM_CHARSET, charset.name());
    return map;
  }

  /**
   * This has been added to protect against a Finalizer attack (because MimeType constructor can throw an exception)
   * See https://wiki.sei.cmu.edu/confluence/display/java/OBJ11-J.+Be+wary+of+letting+constructors+throw+exceptions for more details
   */
  @Override
  protected final void finalize() {
    // Do nothing
  }
}
