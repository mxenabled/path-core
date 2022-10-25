# Changelog

## [1.2.0](https://github.com/mxenabled/path-sdk/compare/v1.1.1...v1.2.0) (2022-10-25)


### Features

* add statement_closed_on to Account ([1b924c5](https://github.com/mxenabled/path-sdk/commit/1b924c5a5517d6cfcd5e0c068db9483b22c80aa7))

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
