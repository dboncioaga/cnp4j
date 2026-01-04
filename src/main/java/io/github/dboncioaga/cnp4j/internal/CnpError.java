package io.github.dboncioaga.cnp4j.internal;

import org.jspecify.annotations.NonNull;

/**
 * <p>Represents a validation error for CNP processing.
 *
 * <p>This class encapsulates error information when a CNP validation fails.
 * The error message describes the specific validation failure.
 *
 * <p>#Immutable#
 *
 * @author Daniel Boncioaga
 * @since 0.1.0
 */
public final class CnpError {

    /**
     * Error constant for null CNP.
     */
    public static final String NULL_CNP = "null cnp";

    /**
     * Error constant for invalid length.
     */
    public static final String INVALID_LENGTH = "invalid length";

    /**
     * Error constant for non-numeric CNP.
     */
    public static final String NON_NUMERIC = "non-numeric";

    /**
     * Error constant for invalid S digit.
     */
    public static final String INVALID_S = "invalid S";

    /**
     * Error constant for invalid month.
     */
    public static final String INVALID_MONTH = "invalid month";

    /**
     * Error constant for invalid day.
     */
    public static final String INVALID_DAY = "invalid day";

    /**
     * Error constant for bad check digit.
     */
    public static final String BAD_CHECK_DIGIT = "bad check digit";

    /**
     * Error constant for invalid date.
     */
    public static final String INVALID_DATE = "invalid date";

    /**
     * The error message.
     */
    private final String message;

    /**
     * <p>Constructs a CnpError with the specified error message.
     *
     * @param message the error message, must not be null
     */
    public CnpError(@NonNull final String message) {
        this.message = message;
    }

    /**
     * <p>Returns the error message.
     *
     * @return the error message, never null
     */
    @Override
    @NonNull
    public String toString() {
        return message;
    }
}
