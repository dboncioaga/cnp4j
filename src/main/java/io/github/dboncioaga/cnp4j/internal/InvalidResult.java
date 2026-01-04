package io.github.dboncioaga.cnp4j.internal;

import org.jspecify.annotations.NonNull;

/**
 * <p>Implementation of {@link ValidationResult} representing an invalid CNP with an error.
 *
 * <p>This class is used when a CNP fails validation. It contains the error details
 * describing why the validation failed.
 *
 * <p>#Immutable#
 *
 * @author Daniel Boncioaga
 * @since 0.1.0
 */
public final class InvalidResult implements ValidationResult {

    /**
     * The validation error.
     */
    private final CnpError error;

    /**
     * <p>Constructs an InvalidResult with the specified error.
     *
     * @param error the validation error, must not be null
     */
    public InvalidResult(@NonNull final CnpError error) {
        this.error = error;
    }

    /**
     * <p>Returns {@code false} indicating the CNP is invalid.
     *
     * @return {@code false}
     */
    @Override
    public boolean isValid() {
        return false;
    }

    /**
     * <p>Gets the validation error that caused the CNP to be invalid.
     *
     * @return the validation error, never null
     */
    @NonNull
    public CnpError getError() {
        return error;
    }

    /**
     * <p>Returns a string representation of this invalid result.
     *
     * @return a string containing the error information
     */
    @Override
    @NonNull
    public String toString() {
        return "InvalidResult{" + error + '}';
    }
}
