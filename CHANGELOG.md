# Changelog

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
