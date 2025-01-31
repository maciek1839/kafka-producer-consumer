# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 0.3.0 (2025-xx-xx)

### Added

- A new section "Production failure scenarios".

### Changed

- Update dependencies.
- Update documentation.

## 0.2.0 (2025-01-30)

### Added

- Kafka Streams example (Spring).
- JDK 17 support.
- A new section "5 Common Pitfalls When Using Apache Kafka".

### Changed

- Update dependencies.
- Update documentation.
- Produce Kafka messages asynchronously.

## 0.1.0 (2023-06-11)

### Added

- A new section "Kafka best practises".
- CONTRIBUTING.md.
- LICENSE.
- GitLab SAST (Static Application Security Testing).
- Apply `ktlint` for Kotlin modules.
- Configure Maven Release plugin.
- Publish a test report using Gitlab Pages.
- Set up SonarCloud integration.

### Changed

- Refactor Kotlin and Java examples (make them easier to compare).
- Bump Confluent to 7.4.0.
- Rename Maven modules - add the 'parent' suffix for Java/Kotlin/Spring modules which keep dependencies.
- Use builders in Java examples and rename Kotlin classes.
