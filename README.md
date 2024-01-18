[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mx.path-core/common/badge.svg)](https://search.maven.org/search?q=com.mx.path-core)
![img](https://img.shields.io/badge/semver-2.0.0-green)
[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-%23FE5196?logo=conventionalcommits&logoColor=white)](https://conventionalcommits.org)

# Path Core - Subprojects

* [Common](common/README.md)
* [Context](context/README.md)
* [Gateway](gateway/README.md)
* [Gateway Generator](gateway-generator/README.md)
* [Http](http/README.md)
* [Messaging](messaging/README.md)
* [Testing](testing/README.md)
* [Utilities](utilities/README.md)

[Documentation](https://docs.mx.com/path-sdk#overview_getting_started)

## Usage

### Using platform (preferred)

_Gradle_
<!-- x-release-please-start-version -->
```groovy
dependencies {
  api platform("com.mx.path-core:platform:3.11.0")

  implementation "com.mx.path-core:common"
  implementation "com.mx.path-core:context"
  implementation "com.mx.path-core:gateway"
  implementation "com.mx.path-core:http"
  implementation "com.mx.path-core:messaging"
  implementation "com.mx.path-core:utilities"

  annotationProcessor "com.mx.path-core:gateway-generator"

  testImplementation "com.mx.path-core:testing"
}
```
<!-- x-release-please-end -->

### Using without platform

_Gradle_
<!-- x-release-please-start-version -->
```groovy
dependencies {
  implementation "com.mx.path-core:common:3.11.0"
  implementation "com.mx.path-core:context:3.11.0"
  implementation "com.mx.path-core:gateway:3.11.0"
  implementation "com.mx.path-core:http:3.11.0"
  implementation "com.mx.path-core:messaging:3.11.0"
  implementation "com.mx.path-core:utilities:3.11.0"

  annotationProcessor "com.mx.path-core:gateway-generator:3.11.0"

  testImplementation "com.mx.path-core:testing:3.11.0"
}
```
<!-- x-release-please-end -->

## Releases

The Path SDK is published to Maven Central at https://search.maven.org/search?q=com.mx.path-core

### Branching Strategy

#### master

`master` contains the edge development for the current major version. All code merged into `master` should be complete, tested, and releasable (from a feature branch).

#### feature branches

  1. Developers create feature branches to introduce code changes
  2. When complete, a Pull Request is created from the branch
  3. Once approved, the pull request is merged into `master`

#### release branches

When work on a _planned_ major version is ready to start, a release branch will be created to hold the in-progress work. Multiple feature branches and pull requests will be generated against this branch until the version is ready for release.

### Release versioning

We use [semantic versioning](https://semver.org/spec/v2.0.0.html). git tags are placed at the point (SHA) where the version was generated.

_Current Major Version_: On the _current major version_, minor and patch versions can be generated at any time and are expected to be backward compatible.

_Past Major Versions_: If a severe issue is discovered in a past major version, a release branch will be created off of the last patch version and a service pack will be released (example: 2.3.2-sp.1) from that branch.

_Next Major Version_: Major version releases can come follow 2 different paths:

  1. Planned (typical): When a large upgrade is planned, a release branch will be generated to hold the new code away from master as it is built.
  2. Unplanned: Some breaking changes may a) be deemed necessary for the health of the project or b) be of minor impact to users of the library. These changes will follow the normal flow and will be merged directly to _master_ (from feature branches).

Regardless of the type, when a major version is ready to be published, a release candidate will be generated (example: 3.0.0-rc.1). This is considered an optional, production-ready release but should be used with some caution. Once the release candidate has been vetted in production, a new release will be generated without the release candidate designation (in the case of a planned version, the version branch will be merged to master at this time).

## Contributing

Commits must conform to the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) specification. The commit types are used to automate version bumps, using [release-please](https://github.com/googleapis/release-please).

#### Clone the repository

`git clone git@github.com:mxenabled/path-core.git`

`cd path-core`

#### Install git conventional commit tools (optional)

This will install commitizen and commitlint to help ensure your commits are formatted correctly before you push them up to Github:

`bin/setup`

(To remove commitizen and githooks use `bin/reset`)

#### Contribute changes

To contribute changes:

  1. create a feature branch off of master or the current release branch (`git checkout -b feature/name_of_feature`)
  2. Commit changes to branch (use `git cz` for help with conventional commit)
  3. Push branch up `git push origin master`
  4. create a pull request

## Building everything

```shell
$ ./gradlew clean build
```

## Assembling everything

```shell
$ ./gradlew clean assemble
```

## Formatting everything

```shell
$ ./gradlew spotlessApply
```

## Testing everything

```shell
$ ./gradlew clean test
```

## Building, assembling, formatting, and testing a subproject

Via helper scripts:

```shell
$ ./bin/build $projectName
```

```shell
$ ./bin/assemble $projectName
```

```shell
$ ./bin/format $projectName
```

```shell
$ ./bin/test $projectName
```

Via gradlew:

```shell
$ ./gradlew :$projectName:clean :$projectName:build
```

```shell
$ ./gradlew :$projectName:clean :$projectName:assemble
```

```shell
$ ./gradlew :$projectName:spotlessApply
```

```shell
$ ./gradlew :$projectName:clean :$projectName:test
```

## Managing Dependencies

This project uses gradle to manage dependencies with dependency locking. The Vogue plugin is used to help keep the dependencies up-to-date. [Vogue](https://github.com/mxenabled/vogue)

**Generating lockfiles for all projects**

```shell
$ ./gradlew assemble --write-locks
```

**Generating lockfiles for a single project**

```shell
$ ./gradlew :$projectName:dependencies --write-locks
```

**View out-of-date dependencies**

```shell
$ ./gradlew vogueReport
```

**Determine source of dependency**

```shell
$ ./gradlew dependencies
```

**Scan dependencies for vulnerabilities**

```shell
$ ./gradlew hushReport
```

To view the generated report of found vulnerabilities open `build/reports/dependency-check-report.html` in a browser.

## Publishing Locally

To create a local build of the project:

```shell
$ ./gradlew publishToMavenLocal
```

This will create a local build in your local maven repository that you can
then reference in other services.

On macOS using gradle the default location for the local maven repository is

```shell
~/.m2/repository/
```
