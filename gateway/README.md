# mx-path-gateway

[Path SDK Issues](https://gitlab.mx.com/groups/mx/money-experiences/path/path-issues/-/issues?scope=all&utf8=%E2%9C%93&state=opened&label_name[]=Path%20SDK)

Interfaces and orchestration classes for MX Path Gateway.

## Concepts

### Accessors

An accessor is responsible for translating between MDX models and third-party APIs. To enable a model and it's available operations, an accessor needs to be provided. A required BaseAccessors needs to extend [com.mx.accessors.BaseAccessor](https://gitlab.mx.com/mx/java-mdx-models/-/blob/master/src/main/java/com/mx/accessors/BaseAccessor.java). Other referenced accessors can be implemented and embedded in the BaseAccessor.

### Behaviors

Behaviors are chainable modules that can modify the call stack before invoking the target accessor. A behavior can add value to a call by performing functions that do not belong in an accessor, allowing the accessors to stay as simple as possible. Examples are logging, caching, filtering. All behaviors implement [com.mx.path.gateway.behavior.GatewayBehavior](https://gitlab.mx.com/middleware/java-path-gateway/-/blob/master/src/main/java/com/mx/path/gateway/behavior/GatewayBehavior.java)

### Facilities

Facilities provide a pre-determined set of functions such as key-value store access, encryption/decryption, messaging. Currently, the set of facilities is defined in [com.mx.path.gateway.facility.Facilities](https://gitlab.mx.com/middleware/java-path-gateway/-/blob/master/src/main/java/com/mx/path/gateway/facility/Facilities.java)

### Services

Services are background processes. The most common use for a service is a message broker listener/responder. Services implement [com.mx.path.gateway.service.GatewayService](https://gitlab.mx.com/middleware/java-path-gateway/-/blob/master/src/main/java/com/mx/path/gateway/service/GatewayService.java)

## Client

Gateway configuration is structured to be run in a multi-tenant operation model. A clientId is used to identify the
client that a gateway belongs to.

## Configuration

A useable gateway can be generated from a configuration file.

_config.yaml_ or _config.yml_

```yaml
---
# A definitions block can be added to place configuration blocks that are shareable.
# This block gets stripped out before the gateway gets built, so make sure to only place
# anchors that get referenced in actual gateway config below.
definitions: 
  - &RedisStore
    class: com.mx.redis.RedisStore
    configurations:
      host: localhost
      port: 6379
      connectionTimeoutSeconds: 10
      computationThreadPoolSize: 5
      ioThreadPoolSize: 5
  - &EnsentaKeystore
    keystorePath: "./src/main/resources/certificates/keystore.jks"
    keystorePassword: "${env:KEYSTORE_PASSWORD}"
  - &AccountCacheInvalidationBehavior
    class: com.mx.web.mdx.behaviors.AccountCacheInvalidationBehavior
    configurations:
      scope: user
      'on':
        - create

clientID:
  rootBehaviors:
  - class: com.mx.web.mdx.behaviors.MdxExceptionBehavior
  facilities:
    # You can reuse a configuration block by adding a reference
    cacheStore: *RedisStore
    sessionStore: *RedisStore
  accessor:
    class: abagnale.repositories.EnsentaAccessor
    scope: singleton
    connections:
      ensenta:
        baseUrl: https://webdeposit.test.ensenta.com
        certificateAlias: afcu_ensenta
        # You can also merge a configuration block into another block if there is some
        # common configuration you'd like to reuse, but there's parts you'd like to add/change.
        <<: *EnsentaKeystore 
    configurations:
      partnerId: '8260'
  gateways:
    accounts:
      behaviors:
      - class: com.mx.web.mdx.behaviors.AccountCacheBehavior
        configurations:
          scope: session|global
          manager: com.mx.web.mdx.behaviors.AccountCache
      # You can also reuse common configuration in lists.
      - <<: *AccountCacheInvalidationBehavior
      gateways:
        transactions: {}
    remoteDeposits: {}
```

Additionally, JSON can be used:

_config.json_

```json
{
  "clientID": {
    "rootBehaviors": [
      { "class": "com.mx.web.mdx.behaviors.MdxExceptionBehavior" }
    ],
    "facilities": {
      "cacheStore": {
        "class": "com.mx.redis.RedisStore",
        "configurations": {
          "host": "localhost",
          "port": 6379,
          "connectionTimeoutSeconds": 10,
          "computationThreadPoolSize": 5,
          "ioThreadPoolSize": 5
        }
      },
      "sessionStore": {
        "class": "com.mx.redis.RedisStore",
        "configurations": {
          "host": "localhost",
          "port": 9379,
          "connectionTimeoutSeconds": 10,
          "computationThreadPoolSize": 5,
          "ioThreadPoolSize": 5
        }
      }
    },
    "accessor": {
      "class": "abagnale.repositories.EnsentaAccessor",
      "scope": "singleton",
      "connections": {
        "ensenta": {
          "baseUrl": "https://webdeposit.test.ensenta.com",
          "keystorePath": "./src/main/resources/certificates/keystore.jks",
          "keystorePassword": "${env:KEYSTORE_PASSWORD}",
          "certificateAlias": "afcu_ensenta"
        }
      },
      "configurations": {
        "partnerId": "8260"
      }
    },
    "gateways": {
      "accounts": {
        "behaviors": [
          {
            "class": "com.mx.web.mdx.behaviors.AccountCacheBehavior",
            "configurations": {
              "scope": "session|global",
              "manager": "com.mx.web.mdx.behaviors.AccountCache"
            }
          }
        ],
        "gateways": {
          "transactions": { }
        }
      },
      "remoteDeposits": { }
    }
  }
}
```

To Load:

```java
GatewayConfigurator configurator = new GatewayConfigurator();
String fileContents = new String(Files.readAllBytes(Paths.get(configurationPath)), StandardCharsets.UTF_8);

Map<String, Gateway> gateways = configurator.buildFromYaml(fileContents);

// Or, if your config is JSON:
// Map<String, Gateway> gateways = configurator.buildFromJson(fileContents);

Gateway gateway = gateways.get("clientID");
```
