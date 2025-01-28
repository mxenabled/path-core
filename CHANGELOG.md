## [3.2.2](https://github.com/mxenabled/path-core/compare/3.2.1...3.2.2) (2023-06-22)


### Bug Fixes

* making Annotations.fieldsAnAnnotations respect inheritance ([3b266dd](https://github.com/mxenabled/path-core/commit/3b266ddcb227766a32f16e88e7b9b2022737de53))

## [4.4.2](https://github.com/mxenabled/path-core/compare/v4.4.1...v4.4.2) (2025-01-28)


### Bug Fixes

* handle Connection Exception and socket exception ([b91af79](https://github.com/mxenabled/path-core/commit/b91af799686eb2a32b6a74b08ba349fd32327a62))

## [4.4.1](https://github.com/mxenabled/path-core/compare/v4.4.0...v4.4.1) (2025-01-22)


### Bug Fixes

* handle external timeout error with status code 531 ([aeba873](https://github.com/mxenabled/path-core/commit/aeba873636199969dbb42f2dde84b285bca54dc5))

## [4.4.0](https://github.com/mxenabled/path-core/compare/v4.3.0...v4.4.0) (2025-01-14)


### Features

* add tests to connection binding on gateway objects ([d5901f0](https://github.com/mxenabled/path-core/commit/d5901f08ebd5dda0916b412e8a30a6d21542cdbd))


### Bug Fixes

* bump connectTimeout to 10s from 5s ([db7f829](https://github.com/mxenabled/path-core/commit/db7f82923eb6d1eb95dae3df32604313adff0d34))

## [4.3.0](https://github.com/mxenabled/path-core/compare/v4.2.1...v4.3.0) (2024-12-19)


### Features

* add comments to new ConnectionBinder class ([9a90cca](https://github.com/mxenabled/path-core/commit/9a90ccaee3ebe3510b14f54af7138db84bea1b8e))
* add connection builder for gateway objects ([860a88f](https://github.com/mxenabled/path-core/commit/860a88f40a42625bff0f4cd81d3fef037ba7df23))


### Bug Fixes

* handle external timeout error with status code 531 ([ae407c4](https://github.com/mxenabled/path-core/commit/ae407c42264b07916f2f457f72a59326587c052f))

## [4.2.1](https://github.com/mxenabled/path-core/compare/v4.2.0...v4.2.1) (2024-11-27)


### Bug Fixes

* call afterInitialize on all accessors ([ed95a0d](https://github.com/mxenabled/path-core/commit/ed95a0dadaf4d727ae557b75e286d4299ce6b23d))
* initialize facilities when facilities are empty ([b3ddc6c](https://github.com/mxenabled/path-core/commit/b3ddc6c4f03ff79523cb6422c4152ca6fca7362f))

## [4.2.0](https://github.com/mxenabled/path-core/compare/v4.1.1...v4.2.0) (2024-10-28)


### Features

* add after initialize method on accessor ([0262d69](https://github.com/mxenabled/path-core/commit/0262d69eb64f72c1fba6c122d45dcb71e3ce023f))
* add all missing documentation and clear javadoc warnings ([3ef4291](https://github.com/mxenabled/path-core/commit/3ef429165a43491decf9b61e849acef6c7df809a))
* add hook to AccessorConnectionBase that allows for call stack modifications ([d3a71a4](https://github.com/mxenabled/path-core/commit/d3a71a48eee05c05303ad2bbdd32da4c401d9b33))


### Bug Fixes

* handle internal error flag for NATs timeout error ([35fe9ad](https://github.com/mxenabled/path-core/commit/35fe9ad3393be5a737b13b1222d9cee6c422edfa))
* handle internal error flag for NATs timeout error ([d29b62c](https://github.com/mxenabled/path-core/commit/d29b62caba5fd32365d807c9cece2ba0098f76e1))
* logic error on request_method logging ([2239690](https://github.com/mxenabled/path-core/commit/223969055b9e33996d513b8354f24ccebb014ffa))
* tweak upstream log fields for better log viewing ([bb64241](https://github.com/mxenabled/path-core/commit/bb64241bbfbdcf8ef01c2a7868205a349df253bb))

## [4.1.1](https://github.com/mxenabled/path-core/compare/v4.1.0...v4.1.1) (2024-09-05)


### Bug Fixes

* change name of variable to avoid http://checkstyle.sourceforge.io/config_coding.html#HiddenField ([1cdb774](https://github.com/mxenabled/path-core/commit/1cdb774e552a7651c92a4375901f4a3430ba682b))
* deprecate function ([fbedb5d](https://github.com/mxenabled/path-core/commit/fbedb5d636505765b99ddff852cf22ae041549a1))
* drop default connect timeout to 5 seconds ([d57c26a](https://github.com/mxenabled/path-core/commit/d57c26afe6805af5b3b05ded25402e5c8a7ccfd7))

## [4.1.0](https://github.com/mxenabled/path-core/compare/v4.0.0...v4.1.0) (2024-09-05)


### Features

* add configurable ConnectTimeout and RequestTimeout ([232987d](https://github.com/mxenabled/path-core/commit/232987dfbab9924e79244f1c2e231a4d3ca4a9e4))

## [4.0.0](https://github.com/mxenabled/path-core/compare/v3.15.0...v4.0.0) (2024-08-21)


### ⚠ BREAKING CHANGES

* Updating classes used for tracing

### Code Refactoring

* change from brave tracing to datadog tracing ([5915de7](https://github.com/mxenabled/path-core/commit/5915de75f807e44fe10da9ba177a985ac426eaf4))

## [3.15.0](https://github.com/mxenabled/path-core/compare/v3.14.0...v3.15.0) (2024-08-16)


### Features

* add internal flag to PathRequestException ([6d954c7](https://github.com/mxenabled/path-core/commit/6d954c7a0d7086dba8a7e3efdce1bc30a3d50797))

## [3.14.0](https://github.com/mxenabled/path-core/compare/v3.13.2...v3.14.0) (2024-07-24)


### Features

* adding products feature ([7c66094](https://github.com/mxenabled/path-core/commit/7c66094c4aa5d97aeb184135283bf66b62a87d93))

## [3.13.2](https://github.com/mxenabled/path-core/compare/v3.13.1...v3.13.2) (2024-07-02)


### Bug Fixes

* set Cookie Spec ([a241460](https://github.com/mxenabled/path-core/commit/a241460687bf1babd82d234efc22c715e117c0ad))

## [3.13.1](https://github.com/mxenabled/path-core/compare/v3.13.0...v3.13.1) (2024-07-01)


### Bug Fixes

* removing uses of deprecated constructor from gateways ([f60a580](https://github.com/mxenabled/path-core/commit/f60a580c81b0845fe3eae4fda1b12b664f5db1d1))

## [3.13.0](https://github.com/mxenabled/path-core/compare/v3.12.0...v3.13.0) (2024-04-12)


### Features

* removing LoginHash From Logging ([f9d3c9e](https://github.com/mxenabled/path-core/commit/f9d3c9e3926dbf5527d413d9abd2fe8bbcb0672e))

## [3.12.0](https://github.com/mxenabled/path-core/compare/v3.11.2...v3.12.0) (2024-04-10)


### Features

* IM-99 - adding a new utility method to fetch all claims from the token ([f6f05d8](https://github.com/mxenabled/path-core/commit/f6f05d857932b0926df373412130494da4f7b030))

## [3.11.2](https://github.com/mxenabled/path-core/compare/v3.11.1...v3.11.2) (2024-02-21)


### Bug Fixes

* fix ambiguous cast of incoming object in gateway methods ([6b3bd48](https://github.com/mxenabled/path-core/commit/6b3bd488fe00215421a4fd9963ef60e97c1176a7))

## [3.11.1](https://github.com/mxenabled/path-core/compare/v3.11.0...v3.11.1) (2024-02-20)


### Bug Fixes

* correct ambiguous model namespaing in generated gateways ([ac914f9](https://github.com/mxenabled/path-core/commit/ac914f966f35bf55913428dfc1db5dfde538c6f9))

## [3.11.0](https://github.com/mxenabled/path-core/compare/v3.10.1...v3.11.0) (2024-01-18)


### Features

* add support for .env files ([1d69da5](https://github.com/mxenabled/path-core/commit/1d69da509df62d8b7594856544540c71eb0ce550))

## [3.10.1](https://github.com/mxenabled/path-core/compare/v3.10.0...v3.10.1) (2023-12-20)


### Reverts

* 2b70c91ee0cbabe9b524d62cd0362285bed3e048 ([20480f5](https://github.com/mxenabled/path-core/commit/20480f5c08d8b585e1016454ddea64519f3753a7))

## [3.10.0](https://github.com/mxenabled/path-core/compare/v3.9.2...v3.10.0) (2023-12-20)


### Features

* add support for .env files ([2b70c91](https://github.com/mxenabled/path-core/commit/2b70c91ee0cbabe9b524d62cd0362285bed3e048))

## [3.9.2](https://github.com/mxenabled/path-core/compare/v3.9.1...v3.9.2) (2023-12-18)


### Bug Fixes

* masking request URIs in UpstreamLogger ([4aed61f](https://github.com/mxenabled/path-core/commit/4aed61f3413bbc7cb55b27b4321fda0ec2cb7c8c))

## [3.9.1](https://github.com/mxenabled/path-core/compare/v3.9.0...v3.9.1) (2023-11-29)


### Bug Fixes

* adding Java17 fix for requests with non-string bodies ([8097673](https://github.com/mxenabled/path-core/commit/8097673e750a35cc04c655c419421dfa4fbd5fd3))

## [3.9.0](https://github.com/mxenabled/path-core/compare/v3.8.0...v3.9.0) (2023-11-07)


### Features

* add login to logs for troubleshooting failed authentication ([a6bec24](https://github.com/mxenabled/path-core/commit/a6bec24db901d3455ec9d5a236a1bbe5d2d208e2))
* move session key to "loginHash" and rename log field to "login_hash" ([057eff6](https://github.com/mxenabled/path-core/commit/057eff65daf2f064d3d01c9f2a438f5275730be0))

## [3.8.0](https://github.com/mxenabled/path-core/compare/v3.7.0...v3.8.0) (2023-10-06)


### Features

* add retry configuration node ([f3dab3a](https://github.com/mxenabled/path-core/commit/f3dab3a15815131a64350dbc181a110c5ba7bd37))
* add retry exception supplier override ([19af9cd](https://github.com/mxenabled/path-core/commit/19af9cdb247082e6def31cbd333000ce43c246dc))
* add support for request retries ([0512ea7](https://github.com/mxenabled/path-core/commit/0512ea7f7bb61af6f784a1703c0689ab5f0a2edb))
* add throwable type adapter ([41bca46](https://github.com/mxenabled/path-core/commit/41bca46d45eaa96b97d12273a2dc0050e51feb02))
* log request attempt ([352fd84](https://github.com/mxenabled/path-core/commit/352fd841e283a35224a7a3e622eb2eccab639fce))
* **serialization:** add LocalDateTimeTypeAdapter ([cf54aa9](https://github.com/mxenabled/path-core/commit/cf54aa9c46faa9f11a278f2c473cfff610c9350d))
* **serialization:** add LocalDateTypeAdapter ([6f2f0aa](https://github.com/mxenabled/path-core/commit/6f2f0aac1ed1f4ec8d51609515cdf35bb4876fec))
* **serialization:** convert OffsetDateTimeDeserializer to TypeAdapter ([8b3cde3](https://github.com/mxenabled/path-core/commit/8b3cde33f5643e47be5cfb9dade389d9a47960f4))
* **serialization:** convert ZonedDateTimeDeserializer to TypeAdapter ([141886b](https://github.com/mxenabled/path-core/commit/141886b1e7ab9d5517f914f751c3aad908e2fff3))


### Bug Fixes

* adding OffsetDateTime and ZonedDateTime serializers ([d9521a3](https://github.com/mxenabled/path-core/commit/d9521a371eadea3468f3d0459e5cb536f6dde269))
* handle nulls in Date/Time deserialization ([09eb67d](https://github.com/mxenabled/path-core/commit/09eb67d9d0110fba25948f79463202fe0011b46b))
* **serialization:** implement Duration deserialization ([e217457](https://github.com/mxenabled/path-core/commit/e2174579261469279666197be6d479e658161dd0))
* **serialization:** implement Pattern deserialization ([32dcda7](https://github.com/mxenabled/path-core/commit/32dcda740b51ddca5ba7ac2b5b50e94df0641c2c))

## [3.7.0](https://github.com/mxenabled/path-core/compare/v3.6.0...v3.7.0) (2023-08-23)


### Features

* add basic compression to session puts ([6a1d293](https://github.com/mxenabled/path-core/commit/6a1d293b1f763ea7ce89ee26e514f9754c116d67))
* **session:** track keys saved to session store and DEL on logout ([c28d7d3](https://github.com/mxenabled/path-core/commit/c28d7d3712fa24ead46b43baba0ec631ba1ea7cd))


### Bug Fixes

* allow setting of default session expiration ([681d7dc](https://github.com/mxenabled/path-core/commit/681d7dc89e0b7c10cee7b37bb60ff566d3115a4d))

## [3.6.0](https://github.com/mxenabled/path-core/compare/v3.5.0...v3.6.0) (2023-08-10)


### Features

* **configuration:** allow coerced types in arrays ([f5af605](https://github.com/mxenabled/path-core/commit/f5af6059f75bcda327ec79d9aab10ae764a868cd))


### Bug Fixes

* **configuration:** add type adapters for complex configuration types ([e706be1](https://github.com/mxenabled/path-core/commit/e706be100d6fb17c439df39b9dee5abd6d22ade3))

## [3.5.0](https://github.com/mxenabled/path-core/compare/v3.4.0...v3.5.0) (2023-08-07)


### Features

* add response status ([6b7e4f9](https://github.com/mxenabled/path-core/commit/6b7e4f9c48c59b9fa28ac8e8966026584a42acbe))
* add slice convinience method to maps ([88175fa](https://github.com/mxenabled/path-core/commit/88175faeb83080eb5d98e279d1f334ef693d3043))
* add upstream request processing ([a344fef](https://github.com/mxenabled/path-core/commit/a344fef97e40c2a54cd61eb59d462a0c81bfac8a))

## [3.4.0](https://github.com/mxenabled/path-core/compare/v3.3.0...v3.4.0) (2023-07-25)


### Features

* add support for Class binding ([3b4b63c](https://github.com/mxenabled/path-core/commit/3b4b63c50a587f32758f9edb49a6bb131fe2e5d5))
* add support for ZoneId binding ([2647b4c](https://github.com/mxenabled/path-core/commit/2647b4c6b3855c41da723c44fa6639fdda48f2f5))

## [3.3.0](https://github.com/mxenabled/path-core/compare/3.2.2...v3.3.0) (2023-07-06)


### Features

* adding unit tests for no config accessors ([208616e](https://github.com/mxenabled/path-core/commit/208616ec11fb3c46536cdde0eb6434320afd162c))
* removing requirement for AccessorConfiguration parameter ([208616e](https://github.com/mxenabled/path-core/commit/208616ec11fb3c46536cdde0eb6434320afd162c))


### Bug Fixes

* applying spotless ([208616e](https://github.com/mxenabled/path-core/commit/208616ec11fb3c46536cdde0eb6434320afd162c))
* deprecating constructor ([208616e](https://github.com/mxenabled/path-core/commit/208616ec11fb3c46536cdde0eb6434320afd162c))
* deprecating getConfiguration ([208616e](https://github.com/mxenabled/path-core/commit/208616ec11fb3c46536cdde0eb6434320afd162c))

## [3.2.1](https://github.com/mxenabled/path-core/compare/3.2.0...3.2.1) (2023-06-16)


### Bug Fixes

* removing PARAMETER target from ClientID annotation ([08d69fd](https://github.com/mxenabled/path-core/commit/08d69fd2657b83a749327d08e5a319e80ca243a0))

## [3.2.0](https://github.com/mxenabled/path-core/compare/3.1.0...3.2.0) (2023-06-14)


### Features

* add support for byte binding ([9ac0350](https://github.com/mxenabled/path-core/commit/9ac03508ce20f03f93b1fd156269b1e41b132059))
* add support for char binding ([62e666b](https://github.com/mxenabled/path-core/commit/62e666bf3c012a138a01a0815127cdd7f71b86a3))
* add support for short binding ([bc13c02](https://github.com/mxenabled/path-core/commit/bc13c025f3369d8c4125b0e7912c1c9085d7b324))

## [3.1.0](https://github.com/mxenabled/path-core/compare/3.0.1...3.1.0) (2023-06-09)


### Features

* add support for regex pattern configuration binding ([8c8e848](https://github.com/mxenabled/path-core/commit/8c8e8484d65882ff4f767829c59fe05e72e40b4e))

## [3.0.1](https://github.com/mxenabled/path-core/compare/3.0.0...3.0.1) (2023-06-08)


### Bug Fixes

* move mutual auth hash code to interface ([9f626dc](https://github.com/mxenabled/path-core/commit/9f626dcc293ebe4cba123493cd754ee95cbc7259))

## [3.0.0](https://github.com/mxenabled/path-core/compare/2.1.2...3.0.0) (2023-06-06)


### ⚠ BREAKING CHANGES

* **mutual_auth:** replace use of ConnectionSettings hashcode

### Bug Fixes

* **mutual_auth:** replace use of ConnectionSettings hashcode ([bd7bbac](https://github.com/mxenabled/path-core/commit/bd7bbac755884ff80644a11929fd6bd8e261b4ea))

## [2.1.2](https://github.com/mxenabled/path-core/compare/v2.1.1...2.1.2) (2023-06-06)


### Bug Fixes

* semantic release ([c33e3e1](https://github.com/mxenabled/path-core/commit/c33e3e1af2702e5975e7ce985550b7f32bc34dea))

## [2.1.2](https://github.com/mxenabled/path-core/compare/v2.1.1...2.1.2) (2023-06-06)


### Bug Fixes

* semantic release ([c33e3e1](https://github.com/mxenabled/path-core/commit/c33e3e1af2702e5975e7ce985550b7f32bc34dea))

# Changelog

## [2.1.1](https://github.com/mxenabled/path-core/compare/v2.1.0...v2.1.1) (2023-05-30)


### Bug Fixes

* use only relevant fields in AccessorConnectionSettings hashcode ([7d15919](https://github.com/mxenabled/path-core/commit/7d15919732fe14bbfbbfca568d6c3b4e6d0648ea))

## [2.1.0](https://github.com/mxenabled/path-core/compare/v2.0.0...v2.1.0) (2023-05-26)


### Features

* collection ordering annotation and comparator ([8c7f5fb](https://github.com/mxenabled/path-core/commit/8c7f5fb42740b73629b556020958b689af416b4c))


### Bug Fixes

* handle null ObjectMap in ConfigurationBinder ([77eaea6](https://github.com/mxenabled/path-core/commit/77eaea696066b0fed4acc3a6322091c714c50719))

## [2.0.0](https://github.com/mxenabled/path-core/compare/v1.11.0...v2.0.0) (2023-05-16)


### ⚠ BREAKING CHANGES

* correct constructors and make abstract
* move com.mx.path.api.connect.http.certificates to com.mx.path.api.connect.http.certificate
* remove deprecated com.mx.path.gateway.security classes
* move accessor classes back to gateway
* move com.mx.common to com.mx.path.common
* move com.mx.path.common.accessors to com.mx.path.common.accessor
* move com.mx.path.common.collections to com.mx.path.common.collection
* move com.mx.path.common.events to com.mx.path.common.event
* move com.mx.path.common.models to com.mx.path.common.model
* move com.mx.path.gateway.events to com.mx.path.gateway.event
* move com.mx.path.model.context to com.mx.path.core.context
* move com.mx.path.utilities to com.mx.path.core.utility
* move com.mx.path.core.utility.OAuth to com.mx.path.core.utility.oauth
* move com.mx.path.gateway.connect.filters to com.mx.path.gateway.connect.filter
* move com.mx.path.api.connect.http to com.mx.path.connect.http
* move com.mx.path.api.connect.messaging to com.mx.path.connect.messaging
* move all classes to standard packages
* remove ServiceIdentifier
* rename base models and fix up gateway generator to support the change.
* move account behavior classes to legacy artifact
* remove Request.featureName
* remove Request.mutualAuthSettings
* remove Request.isCircuitBreakerOpen
* remove Response.checkStatus
* remove com.mx.common.accessors.AccessorConnection
* remove com.mx.path.gateway.util.MdxApiException.java
* remove com.mx.path.gateway.net classes
* remove com.mx.path.gateway.net.executors classes
* clean up deprecated entities

### Bug Fixes

* mark Response JSON deserialization as deprecated ([5ccb138](https://github.com/mxenabled/path-core/commit/5ccb138c3b10c3ef550e007acad29a8afac9188b))
* rename base models and fix up gateway generator to support the change. ([bcd6ca6](https://github.com/mxenabled/path-core/commit/bcd6ca657832de6bf8f3e8309f5eb6bde59d321b))
* squash ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))


### Code Refactoring

* clean up deprecated entities ([378dd57](https://github.com/mxenabled/path-core/commit/378dd576c41891f1878041d9c0820b1afe4d7572))
* correct constructors and make abstract ([3fa086e](https://github.com/mxenabled/path-core/commit/3fa086ef021f7cda101c4bbfa4ba181b41084285))
* move accessor classes back to gateway ([c67970c](https://github.com/mxenabled/path-core/commit/c67970cdacc71a058263c5fa5e16adbfd98732c0))
* move account behavior classes to legacy artifact ([92b257c](https://github.com/mxenabled/path-core/commit/92b257cf70b75f23f6638492c45452303945cd88))
* move all classes to standard packages ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.common to com.mx.path.common ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.api.connect.http to com.mx.path.connect.http ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.api.connect.http.certificates to com.mx.path.api.connect.http.certificate ([41706c6](https://github.com/mxenabled/path-core/commit/41706c64275259a684e6ae1e50c2959404aa9213))
* move com.mx.path.api.connect.messaging to com.mx.path.connect.messaging ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.common.accessors to com.mx.path.common.accessor ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.common.collections to com.mx.path.common.collection ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.common.events to com.mx.path.common.event ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.common.models to com.mx.path.common.model ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.core.utility.OAuth to com.mx.path.core.utility.oauth ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.gateway.connect.filters to com.mx.path.gateway.connect.filter ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.gateway.events to com.mx.path.gateway.event ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.model.context to com.mx.path.core.context ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* move com.mx.path.utilities to com.mx.path.core.utility ([cc69a85](https://github.com/mxenabled/path-core/commit/cc69a85e31a721c00878130a693f936c108dbcf7))
* remove com.mx.common.accessors.AccessorConnection ([378dd57](https://github.com/mxenabled/path-core/commit/378dd576c41891f1878041d9c0820b1afe4d7572))
* remove com.mx.path.gateway.net classes ([378dd57](https://github.com/mxenabled/path-core/commit/378dd576c41891f1878041d9c0820b1afe4d7572))
* remove com.mx.path.gateway.net.executors classes ([378dd57](https://github.com/mxenabled/path-core/commit/378dd576c41891f1878041d9c0820b1afe4d7572))
* remove com.mx.path.gateway.util.MdxApiException.java ([378dd57](https://github.com/mxenabled/path-core/commit/378dd576c41891f1878041d9c0820b1afe4d7572))
* remove deprecated com.mx.path.gateway.security classes ([d4a4f4b](https://github.com/mxenabled/path-core/commit/d4a4f4bb71613674004a8644cc0ef47ac6d65c12))
* remove Request.featureName ([378dd57](https://github.com/mxenabled/path-core/commit/378dd576c41891f1878041d9c0820b1afe4d7572))
* remove Request.isCircuitBreakerOpen ([378dd57](https://github.com/mxenabled/path-core/commit/378dd576c41891f1878041d9c0820b1afe4d7572))
* remove Request.mutualAuthSettings ([378dd57](https://github.com/mxenabled/path-core/commit/378dd576c41891f1878041d9c0820b1afe4d7572))
* remove Response.checkStatus ([378dd57](https://github.com/mxenabled/path-core/commit/378dd576c41891f1878041d9c0820b1afe4d7572))
* remove ServiceIdentifier ([add274b](https://github.com/mxenabled/path-core/commit/add274b7b1c65b045b9bbd8bc63ebb8eddd1578f))

## [1.11.0](https://github.com/mxenabled/path-core/compare/v1.10.0...v1.11.0) (2023-04-26)


### Features

* add an 'optional' field to ConnectionAnnotation ([440e0a4](https://github.com/mxenabled/path-core/commit/440e0a45272edea5944f377a06f4140e8cf2c52b))


### Bug Fixes

* apply spotless ([440e0a4](https://github.com/mxenabled/path-core/commit/440e0a45272edea5944f377a06f4140e8cf2c52b))
* **configurator:** show correct field name on missing error value ([e43dcd8](https://github.com/mxenabled/path-core/commit/e43dcd80229abf84abaae6d32beacf34c6df9813))

## [1.10.0](https://github.com/mxenabled/path-core/compare/v1.9.1...v1.10.0) (2023-04-03)


### Features

* **gateway:** add client facilities initialized event to configurator ([e9d0a9a](https://github.com/mxenabled/path-core/commit/e9d0a9a372307c3b5bc261806b151aefc0e850fe))


### Bug Fixes

* **gateway:** change visibility of event classes to public ([e9d0a9a](https://github.com/mxenabled/path-core/commit/e9d0a9a372307c3b5bc261806b151aefc0e850fe))

## [1.9.1](https://github.com/mxenabled/path-core/compare/v1.9.0...v1.9.1) (2023-03-11)


### Bug Fixes

* allow gateway generator to support accessor and accessors package ([90b7ccb](https://github.com/mxenabled/path-core/commit/90b7ccb02d17a7291229794bfafe2da41e9a5fb1))

## [1.9.0](https://github.com/mxenabled/path-core/compare/v1.8.1...v1.9.0) (2023-02-28)


### Features

* add new PathRequestExceptionWrapper ([403a1d5](https://github.com/mxenabled/path-core/commit/403a1d5d7f374f311aaa7309d9aa4fdeea0f4a61))

## [1.8.1](https://github.com/mxenabled/path-core/compare/v1.8.0...v1.8.1) (2023-02-22)


### Bug Fixes

* expose the current ConfigurationState ([08fa3dc](https://github.com/mxenabled/path-core/commit/08fa3dcb94240def36ccc8292a4893408b3e5dea))
* move RemoteService dispatch tracing scope up so errors have tracing info ([f07c9a6](https://github.com/mxenabled/path-core/commit/f07c9a6fc0ee38d14298b03e22e2f81018fa534f))

## [1.8.0](https://github.com/mxenabled/path-core/compare/v1.7.2...v1.8.0) (2023-02-17)


### Features

* add configurator observer ([c78a702](https://github.com/mxenabled/path-core/commit/c78a7024dcc0e69342aa153bee659ec32b270076))
* configuration and gateway object configuration hooks ([63ca562](https://github.com/mxenabled/path-core/commit/63ca562eda084dcf5d0859aadb5529f64df88094))


### Bug Fixes

* tighten up ConfiguratorObserver types ([2ea2d13](https://github.com/mxenabled/path-core/commit/2ea2d13750708693d966b4d6b871213c002e7b79))

## [1.7.2](https://github.com/mxenabled/path-core/compare/v1.7.1...v1.7.2) (2023-02-07)


### Bug Fixes

* handle more specific HttpClient exceptions ([a956927](https://github.com/mxenabled/path-core/commit/a95692784932b1e0e34d91adf512edb944b22235))
* update connect exception statuses and set codes ([492c6c8](https://github.com/mxenabled/path-core/commit/492c6c8dfca6c019deb7c85d88b478ec1ea03657))

## [1.7.1](https://github.com/mxenabled/path-core/compare/v1.7.0...v1.7.1) (2023-01-26)


### Miscellaneous Chores

* release 1.7.1 ([1c7d2af](https://github.com/mxenabled/path-core/commit/1c7d2aff0e256f42312050d1b8f08952ad177a99))

## [1.7.0](https://github.com/mxenabled/path-core/compare/v1.7.0-SNAPSHOT...v1.7.0) (2023-01-23)


### Miscellaneous Chores

* release 1.7.0 ([09e1924](https://github.com/mxenabled/path-core/commit/09e1924d898d9d544b59ab75041eb78d633a9c2d))

## [1.7.0-SNAPSHOT](https://github.com/mxenabled/path-core/compare/v1.6.2...v1.7.0-SNAPSHOT) (2023-01-18)


### Features

* add exception reporter facility ([1447e66](https://github.com/mxenabled/path-core/commit/1447e663cc3f5574646a07b2902a899b53b8440b))


### Bug Fixes

* add some more context fields to ExceptionContext ([6aa2e0c](https://github.com/mxenabled/path-core/commit/6aa2e0ca477f8d6092bb951018b2074b5abd1cae))
* add user id to exception context ([7a87488](https://github.com/mxenabled/path-core/commit/7a874886ee4a446cda2596910d0f47d161e36a46))
* remove unused Facilities.populate ([1447e66](https://github.com/mxenabled/path-core/commit/1447e663cc3f5574646a07b2902a899b53b8440b))


### Miscellaneous Chores

* release 1.7.0-SNAPSHOT ([1041b35](https://github.com/mxenabled/path-core/commit/1041b3552b05c93c43c93b3c27176f44acc4fb6b))

## [1.6.2](https://github.com/mxenabled/path-core/compare/v1.6.1...v1.6.2) (2023-01-12)


### Bug Fixes

* duration serialization fail on null ([687371f](https://github.com/mxenabled/path-core/commit/687371f675f1f9151f26213744d5282723c67208))

## [1.6.1](https://github.com/mxenabled/path-core/compare/v1.6.0...v1.6.1) (2023-01-11)


### Bug Fixes

* log configurations after binding ([3d701fb](https://github.com/mxenabled/path-core/commit/3d701fb0b36eb2282c6370a09f3998b47a206b5d))
* log configurations after binding with debug level ([4b596ba](https://github.com/mxenabled/path-core/commit/4b596ba23bfedf8642b94fb068cb0892e8cfd7b0))

## [1.6.0](https://github.com/mxenabled/path-core/compare/v1.5.0...v1.6.0) (2022-12-21)


### Features

* add safe configuration object serializer ([1c1ee2c](https://github.com/mxenabled/path-core/commit/1c1ee2ccc48c5011298fe3026b53b570c5c1db7e))
* add secret field to ConfigurationField annotation ([1c1ee2c](https://github.com/mxenabled/path-core/commit/1c1ee2ccc48c5011298fe3026b53b570c5c1db7e))
* log configurations after binding ([d103fa4](https://github.com/mxenabled/path-core/commit/d103fa4ef98122022a2ee8fa2109241851cc2760))
* support ConfigurationField.secret lists ([02a063f](https://github.com/mxenabled/path-core/commit/02a063f91c3e41c672b1c85e6dec28c4b3725221))

## [1.5.0](https://github.com/mxenabled/path-core/compare/v1.4.1...v1.5.0) (2022-12-20)


### Features

* add support for enum configuration bindings ([ed9eff1](https://github.com/mxenabled/path-core/commit/ed9eff1a3dc044d18c8b6f6104cbb144e729171d))
* adding RemoteException ([c8146f8](https://github.com/mxenabled/path-core/commit/c8146f830024f91886ffca34bfb5818ec2e7dbfe))


### Bug Fixes

* cleanup throw text ([c8146f8](https://github.com/mxenabled/path-core/commit/c8146f830024f91886ffca34bfb5818ec2e7dbfe))
* fill in stack trace ([c8146f8](https://github.com/mxenabled/path-core/commit/c8146f830024f91886ffca34bfb5818ec2e7dbfe))
* return a known exception ([c8146f8](https://github.com/mxenabled/path-core/commit/c8146f830024f91886ffca34bfb5818ec2e7dbfe))
* spotless apply ([c8146f8](https://github.com/mxenabled/path-core/commit/c8146f830024f91886ffca34bfb5818ec2e7dbfe))

## [1.4.1](https://github.com/mxenabled/path-core/compare/v1.4.0...v1.4.1) (2022-12-19)


### Bug Fixes

* fix incompatible jaxb-runtime version constraint ([3c6c07c](https://github.com/mxenabled/path-core/commit/3c6c07c9a2d4f20aa77b8a886685ce375aa90dc7))

## [1.4.0](https://github.com/mxenabled/path-core/compare/v1.3.0...v1.4.0) (2022-12-16)


### Features

* add placeholder field to ConfigurationField annotation ([ec98e1f](https://github.com/mxenabled/path-core/commit/ec98e1f6407b23002378f627d263a75a6433a159))
* add support for Duration configuration binding ([1c9085f](https://github.com/mxenabled/path-core/commit/1c9085fb21c0999de6a1f9516006ab8f30f7e4df))
* add value coercion to configuration binding ([ad8718b](https://github.com/mxenabled/path-core/commit/ad8718b2580bfeaa30a49057196372786b9590dd))

## [1.3.0](https://github.com/mxenabled/path-core/compare/1.2.0...1.3.0) (2022-10-29)


### Features

* publish platform ([73c883c](https://github.com/mxenabled/path-core/commit/73c883ca2093d835ef4952a3801125333da67e1d))

## [1.2.0](https://github.com/mxenabled/path-core/compare/v1.1.1...1.2.0) (2022-10-27)


### Features

* adding two new AccessorUserExceptions ([30613f8](https://github.com/mxenabled/path-core/commit/30613f81e4dcb6e206558fbd6e9168dfe15b52ec))
* publish javadocs jar ([42901d8](https://github.com/mxenabled/path-core/commit/42901d8647f8970dc87b7eb84ba3583dbf6d1aaa))
* publish sources jar ([42901d8](https://github.com/mxenabled/path-core/commit/42901d8647f8970dc87b7eb84ba3583dbf6d1aaa))


### Bug Fixes

* remove unneeded annotations attribute from GatewayBaseClass ([98cbe04](https://github.com/mxenabled/path-core/commit/98cbe0489ad258fa542d12b238c788152cc87453))

## [1.1.1](https://github.com/mxenabled/path-sdk/compare/v1.1.0...v1.1.1) (2022-10-20)


### Bug Fixes

* force minor version bump to fix stuck publish ([1e24895](https://github.com/mxenabled/path-sdk/commit/1e24895fa689431dfa2a3632557fe23fdcb5e887))

## [1.1.0](https://github.com/mxenabled/path-sdk/compare/v1.0.2...v1.1.0) (2022-10-20)


### Features

* Adding GatewayEventBus as a default ([#38](https://github.com/mxenabled/path-sdk/issues/38)) ([84eecd5](https://github.com/mxenabled/path-sdk/commit/84eecd5635f3e32141c36fa6d03110191c320b73))


### Bug Fixes

* set clientid in request context if not present ([1f5d5eb](https://github.com/mxenabled/path-sdk/commit/1f5d5eb6bd9e963d145e96b8827aaf7fe2f03bb8))

## [1.0.2](https://github.com/mxenabled/path-sdk/compare/v1.0.1...v1.0.2) (2022-10-19)


### Bug Fixes

* remove redundant code field from MdxApiException ([99b496b](https://github.com/mxenabled/path-sdk/commit/99b496bd0194b1d34fa9572721c5827a2eb73b26))

## [1.0.1](https://github.com/mxenabled/path-sdk/compare/v1.0.0...v1.0.1) (2022-10-14)


### Bug Fixes

* change MessageError parent to PathRequestException ([31796aa](https://github.com/mxenabled/path-sdk/commit/31796aabd3dbc50d6ec1cad960afd2f4bc55c792))

## [1.0.0](https://github.com/mxenabled/path-sdk/compare/0.0.2...v1.0.0) (2022-10-07)


### ⚠ BREAKING CHANGES

* relocate classes into appropriate packages
* combine response status enumerations
* move ObjectMapJsonDeserializerTest from mdx-models
* remove SessionRepositoryMutex
* remove StoreMutex
* remove SessionStore

Upgrade notes: [v1](upgrades/v1/README.md)


### Features

* relocate classes and exception rework ([2429040](https://github.com/mxenabled/path-sdk/commit/24290409fb46b3d239dd6cd1b14178d357d18ed9))
* add new exception hierarchy ([2429040](https://github.com/mxenabled/path-sdk/commit/24290409fb46b3d239dd6cd1b14178d357d18ed9))


### Code Refactoring

* relocate classes into appropriate packages ([2429040](https://github.com/mxenabled/path-sdk/commit/24290409fb46b3d239dd6cd1b14178d357d18ed9))
* combine response status enumerations ([2429040](https://github.com/mxenabled/path-sdk/commit/24290409fb46b3d239dd6cd1b14178d357d18ed9))
* move ObjectMapJsonDeserializerTest from mdx-models ([530cce4](https://github.com/mxenabled/path-sdk/commit/530cce4db235ed965636676aab52d6e069a69f82))
* update Response#throwException to ([bc94f62](https://github.com/mxenabled/path-sdk/commit/bc94f62473620cbb5631e565f5dd9c0ba94ed1c0))
* remove SessionRepositoryMutex ([4e94411](https://github.com/mxenabled/path-sdk/commit/4e94411c48c01487be4cf7210606032570418cf8))
* remove StoreMutex ([4e94411](https://github.com/mxenabled/path-sdk/commit/4e94411c48c01487be4cf7210606032570418cf8))
* remove SessionStore ([c0e7d2f](https://github.com/mxenabled/path-sdk/commit/c0e7d2f7949a4f720626c4b0a23a2f77e0b7553d))
