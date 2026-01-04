package io.github.dboncioaga.cnp4j.internal;

import io.github.dboncioaga.cnp4j.internal.internal.CnpEngine;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;

/**
 * <p>Static utility class providing Apache Commons-style convenience methods for validating
 * and extracting information from Romanian Personal Numeric Code (CNP).
 *
 * <p>This class provides a static API for quick, stateless operations. All methods are
 * null-safe and return {@code null} for invalid CNP strings.
 *
 * <p>Example usage:
 * <pre>
 * boolean valid = CnpUtils.isValid("1800101221144");
 * LocalDate dob = CnpUtils.getDateOfBirth("1800101221144");
 * String sex = CnpUtils.getSex("1800101221144");
 * </pre>
 *
 * @author Daniel Boncioaga
 * @since 0.1.0
 */
public final class CnpUtils {

    /**
     * <p>Private constructor to prevent instantiation.
     * This is a utility class.
     */
    private CnpUtils() { }

    /**
     * <p>Checks if a CNP string is valid.
     *
     * <p>A {@code null} or invalid CNP string will return {@code false}.
     *
     * @param cnp the CNP string to check, may be null
     * @return {@code true} if the CNP is valid, {@code false} otherwise
     */
    public static boolean isValid(@Nullable final String cnp) {
        return CnpEngine.isValid(cnp);
    }

    /**
     * <p>Validates a CNP string and returns a validation result.
     *
     * <p>This method performs comprehensive validation including length, numeric format,
     * S digit, month, day, check digit, and date validity.
     *
     * @param cnp the CNP string to validate, must not be null
     * @return a {@link ValidationResult} indicating whether the CNP is valid or invalid,
     *         never null
     */
    @NonNull
    public static ValidationResult validate(@NonNull final String cnp) {
        return CnpEngine.validate(cnp);
    }

    /**
     * <p>Extracts the date of birth from a CNP string.
     *
     * <p>Returns {@code null} if the CNP is invalid or null.
     *
     * @param cnp the CNP string, must not be null
     * @return the date of birth, or {@code null} if the CNP is invalid
     */
    @Nullable
    public static LocalDate getDateOfBirth(@NonNull final String cnp) {
        return CnpEngine.getDateOfBirth(cnp);
    }

    /**
     * <p>Extracts the sex from a CNP string.
     *
     * <p>The sex is determined by the S digit (first digit):
     * <ul>
     *   <li>Odd digits (1, 3, 5, 7) represent "M" (Male)</li>
     *   <li>Even digits (2, 4, 6, 8) represent "F" (Female)</li>
     * </ul>
     *
     * <p>Returns {@code null} if the CNP is invalid or null.
     *
     * @param cnp the CNP string, must not be null
     * @return the sex ("M" for Male or "F" for Female), or {@code null} if the CNP is invalid
     */
    @Nullable
    public static String getSex(@NonNull final String cnp) {
        return CnpEngine.getSex(cnp);
    }

    /**
     * <p>Extracts the county code from a CNP string.
     *
     * <p>The county code is a 2-digit number (JJ) representing the county or sector
     * where the person was born, or where they had their domicile or residence at
     * the time of CNP assignment. After the implementation of the Integrated Information
     * System, this component is replaced with the unique code "70" for any new registration.
     *
     * <p>Returns {@code null} if the CNP is invalid or null.
     *
     * @param cnp the CNP string, must not be null
     * @return the county code (1-52 or 70), or {@code null} if the CNP is invalid
     */
    @Nullable
    public static Integer getCountyCode(@NonNull final String cnp) {
        return CnpEngine.getCountyCode(cnp);
    }

    /**
     * <p>Extracts the county name from a CNP string.
     *
     * <p>Returns the county name based on the county code (JJ) in the CNP.
     * For the special code 70, returns "Sistem Informatic Integrat (SII)".
     *
     * <p>Returns {@code null} if the CNP is invalid, null, or if the county code
     * is not recognized.
     *
     * @param cnp the CNP string, must not be null
     * @return the county name, or {@code null} if the CNP is invalid or county is not recognized
     */
    @Nullable
    public static String getCounty(@NonNull final String cnp) {
        return CnpEngine.getCounty(cnp);
    }

    /**
     * <p>Calculates the age based on the date of birth extracted from a CNP string.
     *
     * <p>The age is calculated as the number of years between the date of birth
     * and the current date.
     *
     * <p>Returns {@code null} if the CNP is invalid or null, or if the date of birth
     * cannot be determined.
     *
     * @param cnp the CNP string, must not be null
     * @return the age in years, or {@code null} if the CNP is invalid or age cannot be calculated
     */
    @Nullable
    public static Integer getAge(@NonNull final String cnp) {
        return CnpEngine.getAge(cnp);
    }
}
