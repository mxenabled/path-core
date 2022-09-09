# Path SDK - Subprojects

* [Common](common/README.md)
* [Context](context/README.md)
* [Gateway](gateway/README.md)
* [Gateway Generator](gateway-generator/README.md)
* [Http](http/README.md)
* [Messaging](messaging/README.md)
* [Testing](testing/README.md)
* [Utilities](utilities/README.md)

## Contributing
Create a topic branch, make your changes, and create an PR.

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
$ ./gradlew dependencyCheckAnalyze
```
To view the generated report of found vulnerabilities open `build/reports/dependency-check-report.html` in a browser.

## Publishing Locally

To create a local build of the project:

```shell
$ ./gradlew publishToMavenLocal
```
This will create a local build in your local maven repository that you can
then reference in other services.

On OXS using gradle the default location for the local maven repository is
```shell
~/.m2/repository/
```
