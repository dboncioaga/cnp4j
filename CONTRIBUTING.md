# Contributing to CNP4J

Thank you for your interest in contributing to CNP4J! This document provides guidelines and instructions for contributing to the project.

## Code of Conduct

By participating in this project, you agree to maintain a respectful and inclusive environment for all contributors.

## How to Contribute

### Reporting Bugs

If you find a bug, please open an issue on GitHub with:

- A clear, descriptive title
- Steps to reproduce the issue
- Expected behavior
- Actual behavior
- Environment details (Java version, OS, etc.)
- Any relevant code snippets or error messages

### Suggesting Enhancements

Enhancement suggestions are welcome! Please open an issue with:

- A clear description of the enhancement
- Use cases and examples
- Any potential implementation considerations

### Pull Requests

1. **Fork the repository** and create a new branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** following the coding standards below

3. **Write or update tests** for your changes

4. **Ensure all checks pass**:
   ```bash
   mvn clean verify
   ```
   This will run:
   - Checkstyle
   - PMD
   - Unit tests
   - Compilation

5. **Commit your changes** with clear, descriptive commit messages:
   ```bash
   git commit -m "Add feature: description of what you did"
   ```

6. **Push to your fork** and open a Pull Request

7. **Ensure your PR**:
   - Has a clear title and description
   - References any related issues
   - Includes tests for new functionality
   - Passes all CI checks
   - Follows the project's coding standards

## Development Setup

### Prerequisites

- Java 25 or higher
- Maven 3.6 or higher
- Git

### Building the Project

```bash
# Clone the repository
git clone https://github.com/danielboncioaga/cnp4j.git
cd cnp4j

# Build the project
mvn clean install

# Run tests
mvn test

# Run code quality checks
mvn checkstyle:check
mvn pmd:check
```

## Coding Standards

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Keep methods focused and concise (max 150 lines)
- Maximum line length: 120 characters
- Use JSpecify annotations (`@NonNull`, `@Nullable`) for null safety

### JavaDoc

- All public classes and methods must have JavaDoc
- Include `@param` for parameters
- Include `@return` for return values
- Include `@throws` for exceptions

### Testing

- Write unit tests for all new functionality
- Aim for high test coverage
- Use descriptive test method names (e.g., `testIsValidWithValidCNP()`)
- Follow the existing test structure

### Code Quality

The project uses:
- **Checkstyle** for code style enforcement
- **PMD** for code quality analysis
- **JUnit 5** for testing

All code must pass these checks before being merged.

## Project Structure

```
cnp4j/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── dev/
│   │           └── danielb/
│   │               └── cnp4j/
│   │                   └── CNP.java
│   └── test/
│       └── java/
│           └── dev/
│               └── danielb/
│                   └── cnp4j/
│                       ├── CNPTest.java
│                       └── CNPGenerator.java
├── pom.xml
├── checkstyle.xml
└── README.md
```

## Commit Message Guidelines

Use clear, descriptive commit messages:

- Use the imperative mood ("Add feature" not "Added feature")
- Keep the first line under 72 characters
- Reference issues when applicable: "Fix #123: description"
- Use present tense ("Fix bug" not "Fixed bug")

Examples:
```
Add support for county code 70

Implement handling for the Integrated Information System
county code as specified in the CNP documentation.

Fixes #45
```

```
Update README with usage examples

Add comprehensive examples for static method usage
and instance-based API.
```

## Review Process

1. All PRs require at least one review before merging
2. Maintainers will review code quality, tests, and documentation
3. Address any feedback or requested changes
4. Once approved, a maintainer will merge your PR

## Publishing to Maven Central

When code is merged to the `main` branch, GitHub Actions will automatically:
- Run all CI checks (build, tests, code quality)
- Sign artifacts with GPG
- Publish to Maven Central (for non-SNAPSHOT versions)

**Note**: Only non-SNAPSHOT versions are published to Maven Central. To publish a release:
1. Update the version in `pom.xml` to remove `-SNAPSHOT`
2. Commit and push to `main`
3. The workflow will automatically publish to Maven Central

See `.github/workflows/README.md` for details on required secrets and configuration.

## Questions?

If you have questions about contributing, please:
- Open an issue with the `question` label
- Check existing issues and discussions
- Review the README for project documentation

## License

By contributing to CNP4J, you agree that your contributions will be licensed under the Apache License 2.0, the same license as the project.

Thank you for contributing to CNP4J! 🎉

