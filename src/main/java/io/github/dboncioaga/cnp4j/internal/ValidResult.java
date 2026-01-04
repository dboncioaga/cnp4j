package io.github.dboncioaga.cnp4j.internal;

/**
 * <p>Implementation of {@link ValidationResult} representing a valid CNP.
 *
 * <p>This class is used when a CNP passes all validation checks.
 *
 * <p>#Immutable#
 *
 * @author Daniel Boncioaga
 * @since 0.1.0
 */
public final class ValidResult implements ValidationResult {

    /**
     * <p>Returns {@code true} indicating the CNP is valid.
     *
     * @return {@code true}
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * <p>Returns a string representation of this valid result.
     *
     * @return the string "ValidResult"
     */
    @Override
    public String toString() {
        return "ValidResult";
    }
}
