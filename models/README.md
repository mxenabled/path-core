# Models

A subproject that defines as MDX model POJOs along with serializers needed to use them in a Path application.

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

### Internal Fields

Models can include additional fields that store data for internal use only. These fields will never be rendered in MDX responses.

To mark a field as internal, use the @Internal annotation:

```java
@Internal
private String fullAccountNumber;
```
