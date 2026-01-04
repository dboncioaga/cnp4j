# CNP4J

A Java library for validating and extracting information from Romanian Personal Numeric Code (CNP - Cod Numeric Personal).

## Features

- ✅ **CNP Validation**: Validates Romanian CNP with check digit verification
- 📅 **Date of Birth Extraction**: Extracts birth date from CNP
- 👤 **Sex Extraction**: Determines sex (M/F) from CNP
- 🗺️ **County Information**: Extracts county code and name
- 🎂 **Age Calculation**: Calculates age based on date of birth
- 🔒 **JSpecify Annotations**: Full null-safety support with JSpecify
- ☕ **Java 25**: Built with Java 25
- 🎯 **Dual API**: Static convenience methods and instance-based validators

## Requirements

- Java 25 or higher
- Maven 3.6+

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.dboncioaga</groupId>
    <artifactId>cnp4j</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'io.github.dboncioaga:cnp4j:0.1.0'
```

## Usage

### Static API (CnpUtils)

The static API provides Apache Commons-style convenience methods for quick validation and extraction.

```java
import io.github.dboncioaga.cnp4j.internal.CnpUtils;
import java.time.LocalDate;

// Quick validation (doesn't throw)
boolean valid = CnpUtils.isValid("1800101221144");

// Validate and get result
ValidationResult result = CnpUtils.validate("1800101221144");
if (result.isValid()) {
    System.out.println("CNP is valid!");
} else {
    InvalidResult invalid = (InvalidResult) result;
    System.out.println("Validation error: " + invalid.getError());
}

// Extract information (returns null if invalid)
String sex = CnpUtils.getSex("1800101221144");
String county = CnpUtils.getCounty("1800101221144");
LocalDate dob = CnpUtils.getDateOfBirth("1800101221144");
Integer age = CnpUtils.getAge("1800101221144");

if (sex != null) {
    System.out.println("Sex: " + sex); // M or F
}
if (county != null) {
    System.out.println("County: " + county); // Iași
}
if (dob != null) {
    System.out.println("Date of Birth: " + dob); // 1980-01-01
}
if (age != null) {
    System.out.println("Age: " + age); // Current age in years
}
```

### Instance API (CnpValidator)

The instance API provides reusable validator instances that store CNP and validation results.

```java
import io.github.dboncioaga.cnp4j.internal.CnpValidator;
import java.time.LocalDate;

// Constructor validates and stores result (doesn't throw)
CnpValidator validator = new CnpValidator("1800101221144");

// Check validation status
if (validator.isValid()) {
    // All extraction methods return null if invalid
    LocalDate dob = validator.getDateOfBirth();
    String sex = validator.getSex();
    String county = validator.getCounty();
    Integer countyCode = validator.getCountyCode();
    Integer age = validator.getAge();

    if (dob != null) {
        System.out.println("DOB: " + dob);
    }
    if (sex != null) {
        System.out.println("Sex: " + sex);
    }
    if (county != null) {
        System.out.println("County: " + county);
    }
} else {
    // Get error details
    CnpError error = validator.getError();
    if (error != null) {
        System.out.println("Error: " + error);
    }
}
```

### CNP Structure

The Romanian CNP is a 13-digit code with the following structure:

- **S** (1 digit): Sex and century of birth
  - 1: Male, 1900-1999
  - 2: Female, 1900-1999
  - 3: Male, 1800-1899
  - 4: Female, 1800-1899
  - 5: Male, 2000-2099
  - 6: Female, 2000-2099
  - 7: Resident, Male
  - 8: Resident, Female
- **AA** (2 digits): Last 2 digits of birth year
- **LL** (2 digits): Month of birth (01-12)
- **ZZ** (2 digits): Day of birth (01-31)
- **JJ** (2 digits): County code - represents the county or sector where the person was born,
  or where they had their domicile or residence at the time of CNP assignment.
  After the implementation of the Integrated Information System, this component
  is replaced with the unique code "70" for any new registration, regardless of county.
- **NNN** (3 digits): Sequential number
- **C** (1 digit): Check digit

## API Reference

### `CnpUtils` Class (Static API)

Static utility class providing Apache Commons-style convenience methods.

#### Static Methods

- `boolean isValid(@Nullable String cnp)` - Quick validation check (doesn't throw)
- `@NonNull ValidationResult validate(@NonNull String cnp)` - Validates and returns result
- `@Nullable LocalDate getDateOfBirth(@NonNull String cnp)` - Extracts date of birth, returns null if invalid
- `@Nullable String getSex(@NonNull String cnp)` - Extracts sex ("M" or "F"), returns null if invalid
- `@Nullable String getCounty(@NonNull String cnp)` - Extracts county name, returns null if invalid
- `@Nullable Integer getCountyCode(@NonNull String cnp)` - Extracts county code, returns null if invalid
- `@Nullable Integer getAge(@NonNull String cnp)` - Calculates age, returns null if invalid

### `CnpValidator` Class (Instance API)

Instance-based validator that stores CNP and validation results.

#### Constructors

- `CnpValidator(@NonNull String cnp)` - Creates a validator instance, validates on construction (doesn't throw)

#### Instance Methods

- `boolean isValid()` - Returns validation status (doesn't throw)
- `@Nullable CnpError getError()` - Returns error if invalid, null if valid
- `@NonNull String getCnp()` - Returns the original CNP string
- `@Nullable LocalDate getDateOfBirth()` - Extracts date of birth, returns null if invalid
- `@Nullable String getSex()` - Extracts sex ("M" or "F"), returns null if invalid
- `@Nullable String getCounty()` - Extracts county name, returns null if invalid
- `@Nullable Integer getCountyCode()` - Extracts county code, returns null if invalid
- `@Nullable Integer getAge()` - Calculates age, returns null if invalid

### `ValidationResult` Interface

Sealed interface representing validation results.

#### Implementations

- `ValidResult` - Represents a valid CNP
- `InvalidResult` - Represents an invalid CNP with error details

#### Static Factory Methods

- `@NonNull ValidationResult valid()` - Creates a valid result
- `@NonNull ValidationResult invalid(@NonNull CnpError error)` - Creates an invalid result

#### Methods

- `boolean isValid()` - Returns true if valid, false otherwise
- `CnpError getError()` - (InvalidResult only) Gets the validation error

### `CnpError` Class

Represents a validation error for CNP processing.

#### Constants

- `String NULL_CNP` - Error message for null CNP
- `String INVALID_LENGTH` - Error message for invalid length
- `String NON_NUMERIC` - Error message for non-numeric CNP
- `String INVALID_S` - Error message for invalid S digit
- `String INVALID_MONTH` - Error message for invalid month
- `String INVALID_DAY` - Error message for invalid day
- `String BAD_CHECK_DIGIT` - Error message for bad check digit
- `String INVALID_DATE` - Error message for invalid date

#### Constructors

- `CnpError(@NonNull String message)` - Creates an error with the specified message

#### Methods

- `String toString()` - Returns the error message

## Building from Source

```bash
git clone https://github.com/dboncioaga/cnp4j.git
cd cnp4j
mvn clean install
```

## Running Tests

```bash
mvn test
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Author

**Daniel Boncioaga**

- GitHub: [@dboncioaga](https://github.com/dboncioaga)

## References

- [Romanian CNP Wikipedia Article](https://ro.wikipedia.org/wiki/Cod_numeric_personal_(Rom%C3%A2nia))
- [JSpecify Documentation](https://jspecify.dev/)

## Acknowledgments

This library is based on the official Romanian CNP specification and validation rules.
