package io.github.dboncioaga.cnp4j.internal;

import org.jspecify.annotations.NonNull;

/**
 * <p>Sealed interface representing the result of CNP validation.
 *
 * <p>This interface has two implementations:
 * <ul>
 *   <li>{@link ValidResult} - Represents a valid CNP</li>
 *   <li>{@link InvalidResult} - Represents an invalid CNP with error details</li>
 * </ul>
 *
 * <p>Use the static factory methods {@link #valid()} and {@link #invalid(CnpError)}
 * to create instances.
 *
 * @author Daniel Boncioaga
 * @since 0.1.0
 */
public sealed interface ValidationResult permits ValidResult, InvalidResult {

    /**
     * <p>Checks if this validation result represents a valid CNP.
     *
     * @return {@code true} if the CNP is valid, {@code false} otherwise
     */
    boolean isValid();

    /**
     * <p>Creates a validation result representing a valid CNP.
     *
     * @return a {@link ValidResult} instance, never null
     */
    @NonNull
    static ValidationResult valid() {
        return new ValidResult();
    }

    /**
     * <p>Creates a validation result representing an invalid CNP with the specified error.
     *
     * @param error the validation error, must not be null
     * @return an {@link InvalidResult} instance, never null
     */
    @NonNull
    static ValidationResult invalid(@NonNull final CnpError error) {
        return new InvalidResult(error);
    }
}
