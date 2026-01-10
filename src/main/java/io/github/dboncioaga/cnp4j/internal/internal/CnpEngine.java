package io.github.dboncioaga.cnp4j.internal.internal;

import io.github.dboncioaga.cnp4j.internal.CnpError;
import io.github.dboncioaga.cnp4j.internal.CnpUtils;
import io.github.dboncioaga.cnp4j.internal.CnpValidator;
import io.github.dboncioaga.cnp4j.internal.ValidationResult;

import java.time.LocalDate;
import java.time.Period;
import java.time.DateTimeException;
import java.util.Map;

/**
 * <p>Central engine implementing CNP validation and extraction logic.
 *
 * <p>This class is package-private and contains the core validation and extraction
 * algorithms. It is used internally by {@link CnpValidator} and {@link CnpUtils}.
 *
 * <p>Note: this utility class contains multiple small helpers by design. We suppress
 * the PMD TooManyMethods rule because splitting these helpers into separate classes
 * would add unnecessary indirection for this focused implementation.
 *
 * @author Daniel Boncioaga
 * @since 0.1.0
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class CnpEngine {

    /**
     * CNP length constant.
     */
    private static final int CNP_LENGTH = 13;

    /**
     * Check digit calculation weights.
     */
    private static final int[] CHECK_DIGIT_WEIGHTS = {2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};

    /**
     * County codes mapping.
     */
    private static final Map<Integer, String> COUNTY_CODES = Map.ofEntries(
            Map.entry(1, "Alba"),
            Map.entry(2, "Arad"),
            Map.entry(3, "Argeș"),
            Map.entry(4, "Bacău"),
            Map.entry(5, "Bihor"),
            Map.entry(6, "Bistrița-Năsăud"),
            Map.entry(7, "Botoșani"),
            Map.entry(8, "Brașov"),
            Map.entry(9, "Brăila"),
            Map.entry(10, "Buzău"),
            Map.entry(11, "Caraș-Severin"),
            Map.entry(12, "Cluj"),
            Map.entry(13, "Constanța"),
            Map.entry(14, "Covasna"),
            Map.entry(15, "Dâmbovița"),
            Map.entry(16, "Dolj"),
            Map.entry(17, "Galați"),
            Map.entry(18, "Gorj"),
            Map.entry(19, "Harghita"),
            Map.entry(20, "Hunedoara"),
            Map.entry(21, "Ialomița"),
            Map.entry(22, "Iași"),
            Map.entry(23, "Ilfov"),
            Map.entry(24, "Maramureș"),
            Map.entry(25, "Mehedinți"),
            Map.entry(26, "Mureș"),
            Map.entry(27, "Neamț"),
            Map.entry(28, "Olt"),
            Map.entry(29, "Prahova"),
            Map.entry(30, "Sălaj"),
            Map.entry(31, "Satu Mare"),
            Map.entry(32, "Sibiu"),
            Map.entry(33, "Suceava"),
            Map.entry(34, "Teleorman"),
            Map.entry(35, "Timiș"),
            Map.entry(36, "Tulcea"),
            Map.entry(37, "Vaslui"),
            Map.entry(38, "Vâlcea"),
            Map.entry(39, "Vrancea"),
            Map.entry(40, "București"),
            Map.entry(41, "București - Sector 1"),
            Map.entry(42, "București - Sector 2"),
            Map.entry(43, "București - Sector 3"),
            Map.entry(44, "București - Sector 4"),
            Map.entry(45, "București - Sector 5"),
            Map.entry(46, "București - Sector 6"),
            Map.entry(51, "Călărași"),
            Map.entry(52, "Giurgiu"),
            Map.entry(70, "N/A")
    );

    /**
     * <p>Private constructor to prevent instantiation.
     * This is a utility class.
     */
    private CnpEngine() { }

    /**
     * <p>Checks if a CNP string is valid.
     *
     * <p>A {@code null} or invalid CNP string will return {@code false}.
     *
     * @param cnp the CNP string to check, may be null
     * @return {@code true} if the CNP is valid, {@code false} otherwise
     */
    public static boolean isValid(final String cnp) {
        return validate(cnp).isValid();
    }

    /**
     * <p>Validates a CNP string and returns a validation result.
     *
     * <p>This method performs comprehensive validation including:
     * <ul>
     *   <li>Length check (must be exactly 13 digits)</li>
     *   <li>Numeric format check</li>
     *   <li>S digit validation (must be between 1 and 8)</li>
     *   <li>Month validation (must be between 1 and 12)</li>
     *   <li>Day validation (must be between 1 and 31)</li>
     *   <li>Check digit validation</li>
     *   <li>Date validity check</li>
     * </ul>
     *
     * @param cnp the CNP string to validate, may be null
     * @return a {@link ValidationResult} indicating whether the CNP is valid or invalid,
     *         never null
     */
    public static ValidationResult validate(final String cnp) {
        // Basic null/length/numeric checks
        CnpError err = checkNullLengthNumeric(cnp);
        if (err != null) {
            return ValidationResult.invalid(err);
        }

        // S, month, day checks
        err = checkSDigitMonthDay(cnp);
        if (err != null) {
            return ValidationResult.invalid(err);
        }

        // Check digit
        err = checkCheckDigit(cnp);
        if (err != null) {
            return ValidationResult.invalid(err);
        }

        // Date validity
        err = checkDateValidity(cnp);
        if (err != null) {
            return ValidationResult.invalid(err);
        }

        return ValidationResult.valid();
    }

    /**
     * <p>Extracts the date of birth from a valid CNP string.
     *
     * <p>The century is determined by the S digit:
     * <ul>
     *   <li>S = 1, 2: 1900-1999</li>
     *   <li>S = 3, 4: 1800-1899</li>
     *   <li>S = 5, 6: 2000-2099</li>
     *   <li>S = 7, 8: 2000-2099 (residents)</li>
     * </ul>
     *
     * <p>Returns {@code null} if the CNP is invalid or null.
     *
     * @param cnp the CNP string, may be null
     * @return the date of birth, or {@code null} if the CNP is invalid
     */
    public static LocalDate getDateOfBirth(final String cnp) {
        if (!isValid(cnp)) {
            return null;
        }

        final int s = Character.getNumericValue(cnp.charAt(0));
        final int year = Integer.parseInt(cnp.substring(1, 3));
        final int month = Integer.parseInt(cnp.substring(3, 5));
        final int day = Integer.parseInt(cnp.substring(5, 7));

        return LocalDate.of(getCentury(s) + year, month, day);
    }

    /**
     * <p>Extracts the sex from a valid CNP string.
     *
     * <p>The sex is determined by the S digit (first digit):
     * <ul>
     *   <li>Odd digits (1, 3, 5, 7) represent "M" (Male)</li>
     *   <li>Even digits (2, 4, 6, 8) represent "F" (Female)</li>
     * </ul>
     *
     * <p>Returns {@code null} if the CNP is invalid or null.
     *
     * @param cnp the CNP string, may be null
     * @return the sex ("M" for Male or "F" for Female), or {@code null} if the CNP is invalid
     */
    public static String getSex(final String cnp) {
        if (!isValid(cnp)) {
            return null;
        }

        final int s = Character.getNumericValue(cnp.charAt(0));
        return (s % 2 == 1) ? "M" : "F";
    }

    /**
     * <p>Extracts the county code from a valid CNP string.
     *
     * <p>The county code is a 2-digit number (JJ) representing the county or sector
     * where the person was born, or where they had their domicile or residence at
     * the time of CNP assignment. After the implementation of the Integrated Information
     * System, this component is replaced with the unique code "70" for any new registration.
     *
     * <p>Returns {@code null} if the CNP is invalid or null.
     *
     * @param cnp the CNP string, may be null
     * @return the county code (1-52 or 70), or {@code null} if the CNP is invalid
     */
    public static Integer getCountyCode(final String cnp) {
        if (!isValid(cnp)) {
            return null;
        }

        return Integer.parseInt(cnp.substring(7, 9));
    }

    /**
     * <p>Extracts the county name from a valid CNP string.
     *
     * <p>Returns the county name based on the county code (JJ) in the CNP.
     * For the special code 70, returns "N/A".
     *
     * <p>Returns {@code null} if the CNP is invalid, null, or if the county code
     * is not recognized.
     *
     * @param cnp the CNP string, may be null
     * @return the county name, or {@code null} if the CNP is invalid or county is not recognized
     */
    public static String getCounty(final String cnp) {
        final Integer code = getCountyCode(cnp);
        if (code == null) {
            return null;
        }

        return COUNTY_CODES.get(code);
    }

    /**
     * <p>Calculates the age based on the date of birth extracted from a valid CNP string.
     *
     * <p>The age is calculated as the number of years between the date of birth
     * and the current date.
     *
     * <p>Returns {@code null} if the CNP is invalid or null, or if the date of birth
     * cannot be determined.
     *
     * @param cnp the CNP string, may be null
     * @return the age in years, or {@code null} if the CNP is invalid or age cannot be calculated
     */
    public static Integer getAge(final String cnp) {
        final LocalDate dob = getDateOfBirth(cnp);
        if (dob == null) {
            return null;
        }

        return Period.between(dob, LocalDate.now()).getYears();
    }

    // Helper: basic checks
    private static CnpError checkNullLengthNumeric(final String cnp) {
        if (cnp == null) {
            return new CnpError(CnpError.NULL_CNP);
        }
        if (cnp.length() != CNP_LENGTH) {
            return new CnpError(CnpError.INVALID_LENGTH);
        }
        if (!cnp.matches("\\d+")) {
            return new CnpError(CnpError.NON_NUMERIC);
        }
        return null;
    }

    // Helper: S digit, month and day checks
    private static CnpError checkSDigitMonthDay(final String cnp) {
        final int s = Character.getNumericValue(cnp.charAt(0));
        if (s < 1 || s > 8) {
            return new CnpError(CnpError.INVALID_S);
        }

        final int month = Integer.parseInt(cnp.substring(3, 5));
        if (month < 1 || month > 12) {
            return new CnpError(CnpError.INVALID_MONTH);
        }

        final int day = Integer.parseInt(cnp.substring(5, 7));
        if (day < 1 || day > 31) {
            return new CnpError(CnpError.INVALID_DAY);
        }

        return null;
    }

    // Helper: check digit
    private static CnpError checkCheckDigit(final String cnp) {
        final int calculated = calculateCheckDigit(cnp);
        final int provided = Character.getNumericValue(cnp.charAt(12));
        if (calculated != provided) {
            return new CnpError(CnpError.BAD_CHECK_DIGIT);
        }
        return null;
    }

    // Helper: date validity
    private static CnpError checkDateValidity(final String cnp) {
        final int s = Character.getNumericValue(cnp.charAt(0));
        final int year = Integer.parseInt(cnp.substring(1, 3));
        final int month = Integer.parseInt(cnp.substring(3, 5));
        final int day = Integer.parseInt(cnp.substring(5, 7));

        final int fullYear = getCentury(s) + year;
        try {
            LocalDate.of(fullYear, month, day);
        } catch (final DateTimeException e) {
            return new CnpError(CnpError.INVALID_DATE);
        }
        return null;
    }

    /**
     * <p>Calculates the check digit for a CNP string.
     *
     * <p>The check digit is calculated using a weighted sum of the first 12 digits.
     * If the remainder is 10, the check digit is set to 1.
     *
     * @param cnp the CNP string, must not be null and must be at least 12 characters
     * @return the calculated check digit (0-9)
     */
    private static int calculateCheckDigit(final String cnp) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += Character.getNumericValue(cnp.charAt(i)) * CHECK_DIGIT_WEIGHTS[i];
        }
        final int rem = sum % 11;
        return (rem < 10) ? rem : 1;
    }

    /**
     * <p>Determines the century based on the S digit.
     *
     * @param s the S digit (first digit of CNP)
     * @return the century (1800, 1900, or 2000)
     */
    private static int getCentury(final int s) {
        if (s == 1 || s == 2) {
            return 1900;
        } else if (s == 3 || s == 4) {
            return 1800;
        } else {
            return 2000;
        }
    }
}
