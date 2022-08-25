## Connection Testing

A collection of classes to be used as a testImplementation dependency. Assists in testing connections.

Includes:

* Plain java classes (for use in JUnit tests)
* Groovy Traits (for use in Spock tests)

### Usage

*Include*

```groovy
dependencies {
  testImplementation "com.mx.path.api:testing:16.0.4"
}
```

*Spock*

```groovy
class AccountsAPITest extends Specification implements WithRequestExpectations {
  private AccountsAPI subject
  private BankConnection connection

  def setup() {
    connection = setupConnection(new BankConnection(null))
    subject = new AccountsAPI(connection)
  }

  def "create account"() {
    given:
    def account = new Account().tap {
      type = "savings"
      currencyCode = "usd"
      creditLimit = 500.0
    }

    def accountCreateExpectation = expectConnection(withPath("accounts").withMethod("POST")).toRespond({ request, response ->
      response.setStatus(HttpStatus.CREATED)
      response.setBody("                {\n" +
          "                    \"account_guid\": \"269939a0-05cc-41a1-bf5d-77bd7ff28728\",\n" +
          "                    \"wallet_guid\": \"af6b61ff-2e1a-487d-aa11-7d6a78c1a02c\",\n" +
          "                    \"person_guid\": \"af6b61ff-2e1a-487d-aa11-7d6a78c1a02c\",\n" +
          "                    \"currency_code\": \"USD\",\n" +
          "                    \"account_name\": \"test\",\n" +
          "                    \"account_type\": \"CHECKING\",\n" +
          "                    \"available_balance\": 985065.1300,\n" +
          "                    \"ledger_balance\": 0.00,\n" +
          "                    \"suspense_balance\": 0.00,\n" +
          "                    \"account_status\": \"ACTIVE\"\n" +
          "                }\n")
    })

    when:
    def result = subject.create(account, token)

    then:
    result instanceof QoloAccount
    result.getAccountGuid() == "269939a0-05cc-41a1-bf5d-77bd7ff28728"

    def request = accountCreateExpectation.getReceivedRequest()
    def requestBody = qoloGson().fromJson(request.body as String, QoloCreateAccountBody.class)

    verifyAll {
      requestBody.personGuid == Session.current().get(Session.ServiceIdentifier.Session, "person_guid")
      requestBody.walletGuid == Session.current().get(Session.ServiceIdentifier.Session, "wallet_guid")
      requestBody.accountType == account.type
      requestBody.accountLimit == 500
      requestBody.currencies.first() == account.currencyCode
      requestBody.walletId == Session.current().get(Session.ServiceIdentifier.Session, "wallet_id")
      requestBody.accountName == account.name
    }
    request.getHeaders().get("IdempotencyKey") != null
  }
```
