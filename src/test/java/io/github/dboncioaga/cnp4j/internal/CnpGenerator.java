package io.github.dboncioaga.cnp4j.internal;

/**
 * Helper class to generate valid CNP numbers for testing purposes.
 * This is a utility class for testing only.
 */
final class CnpGenerator {

    /**
     * Check digit calculation weights.
     */
    private static final int[] CHECK_DIGIT_WEIGHTS = {2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};

    private CnpGenerator() {
        // Utility class
    }

    /**
     * Generates a valid CNP for testing purposes.
     *
     * @param s sex and century digit (1-8)
     * @param year last 2 digits of birth year
     * @param month month of birth (1-12)
     * @param day day of birth (1-31)
     * @param countyCode county code (1-52 or 70)
     * @param sequential sequential number (0-999)
     * @return a valid CNP string
     */
    static String generate(int s, int year, int month, int day, int countyCode, int sequential) {
        StringBuilder cnp = new StringBuilder(13);

        // S digit
        cnp.append(s);

        // Year (2 digits)
        cnp.append(String.format("%02d", year % 100));

        // Month (2 digits)
        cnp.append(String.format("%02d", month));

        // Day (2 digits)
        cnp.append(String.format("%02d", day));

        // County code (2 digits)
        cnp.append(String.format("%02d", countyCode));

        // Sequential number (3 digits)
        cnp.append(String.format("%03d", sequential % 1000));

        // Calculate check digit
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(cnp.charAt(i));
            sum += digit * CHECK_DIGIT_WEIGHTS[i];
        }

        int remainder = sum % 11;
        int checkDigit = (remainder < 10) ? remainder : 1;
        cnp.append(checkDigit);

        return cnp.toString();
    }
}

