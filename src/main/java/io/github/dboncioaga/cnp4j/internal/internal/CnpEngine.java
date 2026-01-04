package io.github.dboncioaga.cnp4j.internal.internal;

import io.github.dboncioaga.cnp4j.internal.CnpError;
import io.github.dboncioaga.cnp4j.internal.CnpUtils;
import io.github.dboncioaga.cnp4j.internal.CnpValidator;
import io.github.dboncioaga.cnp4j.internal.ValidationResult;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Central engine implementing CNP validation and extraction logic.
 *
 * <p>This class is package-private and contains the core validation and extraction
 * algorithms. It is used internally by {@link CnpValidator} and {@link CnpUtils}.
 *
 * @author Daniel Boncioaga
 * @since 0.1.0
 */
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
    private static final Map<Integer, String> COUNTY_CODES;

    static {
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "Alba");
        map.put(2, "Arad");
        map.put(3, "Argeș");
        map.put(4, "Bacău");
        map.put(5, "Bihor");
        map.put(6, "Bistrița-Năsăud");
        map.put(7, "Botoșani");
        map.put(8, "Brașov");
        map.put(9, "Brăila");
        map.put(10, "Buzău");
        map.put(11, "Caraș-Severin");
        map.put(12, "Cluj");
        map.put(13, "Constanța");
        map.put(14, "Covasna");
        map.put(15, "Dâmbovița");
        map.put(16, "Dolj");
        map.put(17, "Galați");
        map.put(18, "Gorj");
        map.put(19, "Harghita");
        map.put(20, "Hunedoara");
        map.put(21, "Ialomița");
        map.put(22, "Iași");
        map.put(23, "Ilfov");
        map.put(24, "Maramureș");
        map.put(25, "Mehedinți");
        map.put(26, "Mureș");
        map.put(27, "Neamț");
        map.put(28, "Olt");
        map.put(29, "Prahova");
        map.put(30, "Sălaj");
        map.put(31, "Satu Mare");
        map.put(32, "Sibiu");
        map.put(33, "Suceava");
        map.put(34, "Teleorman");
        map.put(35, "Timiș");
        map.put(36, "Tulcea");
        map.put(37, "Vaslui");
        map.put(38, "Vâlcea");
        map.put(39, "Vrancea");
        map.put(40, "București");
        map.put(41, "București - Sector 1");
        map.put(42, "București - Sector 2");
        map.put(43, "București - Sector 3");
        map.put(44, "București - Sector 4");
        map.put(45, "București - Sector 5");
        map.put(46, "București - Sector 6");
        map.put(51, "Călărași");
        map.put(52, "Giurgiu");
        map.put(70, "Sistem Informatic Integrat (SII)");
        COUNTY_CODES = Map.copyOf(map);
    }

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
        if (cnp == null) {
            return ValidationResult.invalid(new CnpError(CnpError.NULL_CNP));
        }

        if (cnp.length() != CNP_LENGTH) {
            return ValidationResult.invalid(new CnpError(CnpError.INVALID_LENGTH));
        }

        if (!cnp.matches("\\d+")) {
            return ValidationResult.invalid(new CnpError(CnpError.NON_NUMERIC));
        }

        final int s = Character.getNumericValue(cnp.charAt(0));
        if (s < 1 || s > 8) {
            return ValidationResult.invalid(new CnpError(CnpError.INVALID_S));
        }

        final int month = Integer.parseInt(cnp.substring(3, 5));
        if (month < 1 || month > 12) {
            return ValidationResult.invalid(new CnpError(CnpError.INVALID_MONTH));
        }

        final int day = Integer.parseInt(cnp.substring(5, 7));
        if (day < 1 || day > 31) {
            return ValidationResult.invalid(new CnpError(CnpError.INVALID_DAY));
        }

        final int calculated = calculateCheckDigit(cnp);
        final int provided = Character.getNumericValue(cnp.charAt(12));
        if (calculated != provided) {
            return ValidationResult.invalid(new CnpError(CnpError.BAD_CHECK_DIGIT));
        }

        final int year = Integer.parseInt(cnp.substring(1, 3));
        final int century;
        if (s == 1 || s == 2) {
            century = 1900;
        } else if (s == 3 || s == 4) {
            century = 1800;
        } else if (s == 5 || s == 6) {
            century = 2000;
        } else {
            century = 2000;
        }

        final int fullYear = century + year;
        try {
            LocalDate.of(fullYear, month, day);
        } catch (final Exception e) {
            return ValidationResult.invalid(new CnpError(CnpError.INVALID_DATE));
        }

        return ValidationResult.valid();
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

        final int century;
        if (s == 1 || s == 2) {
            century = 1900;
        } else if (s == 3 || s == 4) {
            century = 1800;
        } else if (s == 5 || s == 6) {
            century = 2000;
        } else {
            century = 2000;
        }

        return LocalDate.of(century + year, month, day);
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

        try {
            return Integer.parseInt(cnp.substring(7, 9));
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * <p>Extracts the county name from a valid CNP string.
     *
     * <p>Returns the county name based on the county code (JJ) in the CNP.
     * For the special code 70, returns "Sistem Informatic Integrat (SII)".
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
}
