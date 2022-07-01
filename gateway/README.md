# Gateway

Interfaces and orchestration classes for the Path SDK.

## Concepts

### Accessors

Accessors provide translation between the MDX models and operations and external systems. These are the primary functional input to the Path system. All other inputs provide support for these. Each accessor extends a "Base" accessor. The Base Accessors define and organize the MDX models and their operations. The accessors are nested to represent the domain and features each model belongs to.

### Behaviors

Behaviors are chainable modules that can modify the call stack before invoking the target accessor. A behavior can add value to a call by performing functions that do not belong in an accessor, allowing the accessors to stay as simple as possible. Examples are logging, caching, filtering. All behaviors implement [com.mx.path.gateway.behavior.GatewayBehavior](https://gitlab.mx.com/middleware/java-path-gateway/-/blob/master/src/main/java/com/mx/path/gateway/behavior/GatewayBehavior.java)

### Facilities

Facilities provide infrastructure abstractions via a pre-determined set of functions such as key-value store access, encryption/decryption, messaging, and more. Currently, the set of facilities is defined in [com.mx.path.gateway.facility.Facilities](https://gitlab.mx.com/middleware/java-path-gateway/-/blob/master/src/main/java/com/mx/path/gateway/facility/Facilities.java)

### Services

Services are background processes. The most common use for a service is a message broker listener/responder. Services implement [com.mx.path.gateway.service.GatewayService](https://gitlab.mx.com/middleware/java-path-gateway/-/blob/master/src/main/java/com/mx/path/gateway/service/GatewayService.java)

## Client

Gateway configuration is structured to be run in a multi-tenant operation model. A clientId is used to identify the
client that a gateway belongs to.

## Configuration

A gateway can be generated from a configuration file.

_gateway.yaml_ or _gateway.yml_

```yaml
---
# A definitions block can be added to place configuration blocks that are shareable.
# This block gets stripped out before the gateway gets built, so make sure to only place
# anchors that get referenced in actual gateway config below.
definitions: 
  - &MemoryStore
    class: com.mx.store.MemoryStore
    configurations:
      host: localhost
      port: 6379
      connectionTimeoutSeconds: 10
      computationThreadPoolSize: 5
      ioThreadPoolSize: 5
  - &Keystore
    keystorePath: "./src/main/resources/certificates/keystore.jks"
    keystorePassword: "${env:KEYSTORE_PASSWORD}"
  - &RelationCacheInvalidationBehavior
    class: com.mx.behaviors.RelationCacheInvalidationBehavior
    configurations:
      scope: user
      'on':
        - create

clientID:
  rootBehaviors:
  - class: com.mx.behaviors.MdxExceptionBehavior
  facilities:
    # You can reuse a configuration block by adding a reference
    cacheStore: *MemoryStore
    sessionStore: *MemoryStore
  accessor:
    class: service.accessors.SomeBankBaseAccessor
    scope: singleton
    connections:
      thirdParty:
        baseUrl: https://example.com/third-party-api
        certificateAlias: some_alias
        # You can also merge a configuration block into another block if there is some
        # common configuration you'd like to reuse, but there's parts you'd like to add/change.
        <<: *Keystore 
    configurations:
      specialValue: '123abc'
  gateways:
    accounts:
      behaviors:
      - class: com.mx.behaviors.CachingBehavior
        configurations:
            'on':
              models:
                - com.mx.models.account.Account
            scope: user
            expirySeconds: 15
      # You can also reuse common configuration in lists.
      - <<: *RelationCacheInvalidationBehavior
      gateways:
        transactions: {}
    remoteDeposits: {}
```

Additionally, JSON can be used:

_gateway.json_

```json
{
  "clientID": {
    "rootBehaviors": [
      {
        "class": "com.mx.behaviors.MdxExceptionBehavior"
      }
    ],
    "facilities": {
      "cacheStore": {
        "class": "com.mx.store.MemoryStore",
        "configurations": {
          "host": "localhost",
          "port": 6379,
          "connectionTimeoutSeconds": 10,
          "computationThreadPoolSize": 5,
          "ioThreadPoolSize": 5
        }
      },
      "sessionStore": {
        "class": "com.mx.store.MemoryStore",
        "configurations": {
          "host": "localhost",
          "port": 6379,
          "connectionTimeoutSeconds": 10,
          "computationThreadPoolSize": 5,
          "ioThreadPoolSize": 5
        }
      }
    },
    "accessor": {
      "class": "service.accessors.SomeBankBaseAccessor",
      "scope": "singleton",
      "connections": {
        "thirdParty": {
          "baseUrl": "https://example.com/third-party-api",
          "certificateAlias": "some_alias",
          "keystorePath": "./src/main/resources/certificates/keystore.jks",
          "keystorePassword": "${env:KEYSTORE_PASSWORD}"
        }
      },
      "configurations": {
        "specialValue": "123abc"
      }
    },
    "gateways": {
      "accounts": {
        "behaviors": [
          {
            "class": "com.mx.behaviors.CachingBehavior",
            "configurations": {
              "on": {
                "models": [
                  "com.mx.models.account.Account"
                ]
              },
              "scope": "user",
              "expirySeconds": 15
            }
          },
          {
            "class": "com.mx.behaviors.RelationCacheInvalidationBehavior",
            "configurations": {
              "scope": "user",
              "on": [
                "create"
              ]
            }
          }
        ],
        "gateways": {
          "transactions": {}
        }
      },
      "remoteDeposits": {}
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
