# Path Connect - HTTP

Base classes for building HTTP-style accessor connections.

## Example

### Connection

```java
public class BankConnection extends HttpAccessorConnection {
  @Getter
  private final BankConnectionConfiguration config;

  public BankConnection(@Configuration BankConnectionConfiguration configuration) {
    this.config = configuration;
  }

  public final List<BankAccount> getAccounts() {
    request("account.svc")
      .withOnComplete(response -> {
        if (response.getStatus() != HttpStatus.OK) {
          QoloUtils.throwResponseException(response, "Failed to retrieve account", HttpStatus.UNPROCESSABLE_ENTITY, true, null);
        }
      }).withProcessor(response -> new Gson().fromJson(response.getBody(), new TypeToken<List<BankAccount>>() {}.getType()))
      .get()
      .throwException()
      .getObject();
  }
}
```

### Accessor

```java
public class BankAccountAccessor extends AccountBaseAccessor {
  private final BankConnection bankConnection;

  public BankAccountAccessor(@Connection("bank") BankConnection bankConnection) {
    this.bankConnection = bankConnection;
  }

  @Override
  public final MdxList<Account> list() {
    List<BankAccount> bankAccounts = bankConnection.getAccounts();

    return MdxMappable.map(bankAccounts);
  }
}
```

### Configuration

_gateway.yml_

```yaml
bank:
  gateways:
    accounts:
      accessor:
        class: com.bank.BankBaseAccessor
        scope: singleton
        connections:
          bank:
            baseUrl: https://devapi.thebank.com/api/v5
```
