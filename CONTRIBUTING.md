# CONTRIBUTING

- Follow [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/).
    - References:
      - https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716
      - https://ec.europa.eu/component-library/v1.15.0/eu/docs/conventions/git/

## Kotlin coding conventions

Ref: https://kotlinlang.org/docs/reference/coding-conventions.html#naming-rules

### Naming rules - packages

Names of packages are always lower case and do not use underscores (org.example.myproject). Using multi-word names is generally discouraged, but if you do need to use multiple words, you can either simply concatenate them together or use camel humps (org.example.myProject). Ref: https://kotlinlang.org/docs/coding-conventions.html#naming-rules

## Maven commands

- mvn clean install -DskipTests

### Format files using ktlint

Use a command: ``mvn ktlint:format`` in `kotlin` module.

*Add a JVM option `--add-opens=java.base/java.lang=ALL-UNNAMED` if you encounter any access errors running the command.*

Ref: https://gantsign.com/ktlint-maven-plugin/usage.html

### Release a new version

Run a Maven command using Maven Release plugin:
```text
mvn release:clean release:prepare release:perform
```

*Remote artifact repositories are not set so skip `release:perform` goal.*

If you want to only update versions (not recommended), use below command:
```text
mvn release:update-versions -DautoVersionSubmodules=true
```

The `release:prepare` goal will:
- Make sure that there are no uncommitted changes or SNAPSHOT dependencies (see above)
- Update the SNAPSHOT version number to a release version (e.g. going from "1.0.1-SNAPSHOT" to "1.0.1")
- Update the SCM section of the POM file to point to the release tag rather than the trunk in the Subversion repository
- Run all the application tests to make sure everything still works
- Commit the changes made to the POM file
- Create a new tag in Subversion for this release
- Update the SNAPSHOT version number to a new SNAPSHOT version (e.g. going from "1.0.1" to "1.0.2-SNAPSHOT")
- Commit the changes made to the POM file

The `release:perform` goal to do the following:
- Check out the release we just tagged
- Build the application (compiling, testing and packaging)
- Deploy the release version to local and remote repositories

References: 
- https://www.infoworld.com/article/2072325/using-the-maven-release-plugin.html
- https://maven.apache.org/maven-release/maven-release-plugin/usage/prepare-release.html
