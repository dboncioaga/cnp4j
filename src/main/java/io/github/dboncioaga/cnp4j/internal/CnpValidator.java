package io.github.dboncioaga.cnp4j.internal;

import io.github.dboncioaga.cnp4j.internal.internal.CnpEngine;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

/**
 * <p>Instance-based validator for Romanian Personal Numeric Code (CNP).
 *
 * <p>This class provides a reusable, testable object representing a CNP.
 * The validation is performed during construction, and the result is stored internally.
 * Subsequent calls to extraction methods will return {@code null} if the CNP is invalid.
 *
 * <p>Example usage:
 * <pre>
 * CnpValidator validator = new CnpValidator("1800101221144");
 * if (validator.isValid()) {
 *     LocalDate dob = validator.getDateOfBirth();
 *     String sex = validator.getSex();
 * }
 * </pre>
 *
 * <p>#Immutable#
 *
 * @author Daniel Boncioaga
 * @since 0.1.0
 */
public final class CnpValidator {

    /**
     * The CNP string.
     */
    private final String cnp;

    /**
     * The validation result.
     */
    private final ValidationResult validation;

    /**
     * <p>Constructs a CnpValidator instance from the given CNP string.
     *
     * <p>The CNP is validated during construction, and the validation status
     * is stored internally. The constructor does not throw exceptions for invalid CNPs.
     *
     * @param cnp the CNP string to validate and parse, must not be null
     * @throws NullPointerException if cnp is null
     */
    public CnpValidator(@NonNull final String cnp) {
        this.cnp = Objects.requireNonNull(cnp, "CNP cannot be null");
        this.validation = CnpEngine.validate(cnp);
    }

    /**
     * <p>Gets the original CNP string.
     *
     * @return the CNP string, never null
     */
    @NonNull
    public String getCnp() {
        return cnp;
    }

    /**
     * <p>Checks if this CnpValidator instance holds a valid CNP.
     *
     * @return {@code true} if the CNP is valid, {@code false} otherwise
     */
    public boolean isValid() {
        return validation.isValid();
    }

    /**
     * <p>Gets the validation error if the CNP is invalid.
     *
     * @return the {@link CnpError} if the CNP is invalid, or {@code null} if valid
     */
    @Nullable
    public CnpError getError() {
        if (validation instanceof InvalidResult) {
            return ((InvalidResult) validation).getError();
        }
        return null;
    }

    /**
     * <p>Extracts the date of birth from the CNP.
     *
     * <p>Returns {@code null} if the CNP is invalid.
     *
     * @return the date of birth, or {@code null} if the CNP is invalid
     */
    @Nullable
    public LocalDate getDateOfBirth() {
        return CnpEngine.getDateOfBirth(cnp);
    }

    /**
     * <p>Extracts the sex from the CNP.
     *
     * <p>Returns {@code null} if the CNP is invalid.
     *
     * @return the sex ("M" for Male or "F" for Female), or {@code null} if the CNP is invalid
     */
    @Nullable
    public String getSex() {
        return CnpEngine.getSex(cnp);
    }

    /**
     * <p>Extracts the county code from the CNP.
     *
     * <p>Returns {@code null} if the CNP is invalid.
     *
     * @return the county code, or {@code null} if the CNP is invalid
     */
    @Nullable
    public Integer getCountyCode() {
        return CnpEngine.getCountyCode(cnp);
    }

    /**
     * <p>Extracts the county name from the CNP.
     *
     * <p>Returns {@code null} if the CNP is invalid or if the county code is not recognized.
     *
     * @return the county name, or {@code null} if the CNP is invalid or county is not recognized
     */
    @Nullable
    public String getCounty() {
        return CnpEngine.getCounty(cnp);
    }

    /**
     * <p>Calculates the age based on the date of birth extracted from the CNP.
     *
     * <p>Returns {@code null} if the CNP is invalid or if the age cannot be calculated.
     *
     * @return the age in years, or {@code null} if the CNP is invalid or age cannot be calculated
     */
    @Nullable
    public Integer getAge() {
        return CnpEngine.getAge(cnp);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CnpValidator)) {
            return false;
        }
        final CnpValidator other = (CnpValidator) o;
        return Objects.equals(cnp, other.cnp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnp);
    }

    @Override
    @NonNull
    public String toString() {
        return "CnpValidator{cnp='" + cnp + "', valid=" + isValid() + '}';
    }
}
