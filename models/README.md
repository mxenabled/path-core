# MDX Models

A jar that defines as MDX model POJOs along with serializers needed to use them in a Spring Boot micro service.

See https://developer.mx.com/drafts/mdx/overview/

## Usage

In order to create a Gson serializer/deserializer, use the Resources.registerResources, passing in the builder.

Example

```java
GsonBuilder builder = new GsonBuilder()

    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .setDateFormat("YYYY-MM-dd")
    .setPrettyPrinting();

Resources.registerResources(builder);

Gson serializer = builder.create();
```

To provide Gson serialization to a Spring-Boot application register a FactoryBean.

Example

```java
public class MdxSerializerFactoryBean implements FactoryBean<Gson> {
  public MdxSerializerFactoryBean() {
  }

  @Override
  public final Gson getObject() throws Exception {
    GsonBuilder baseGson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setDateFormat("YYYY-MM-dd")
        .setPrettyPrinting();

    Resources.registerResources(baseGson);

    return baseGson.create();
  }

  @Override
  public final Class<?> getObjectType() {
    return Gson.class;
  }

  @Override
  public final boolean isSingleton() {
    return false;
  }
}
```

### Internal Fields

Models can include additional fields that store data for internal use only. These fields will never be rendered in MDX responses.

To mark a field as internal, use the @Internal annotation:

```java
@Internal
private String fullAccountNumber;
```
