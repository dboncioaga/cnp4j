package io.github.dboncioaga.cnp4j.internal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationResultTest {

    @Test
    void testValidResult() {
        ValidationResult result = ValidationResult.valid();
        assertTrue(result.isValid());
        assertTrue(result instanceof ValidResult);
    }

    @Test
    void testInvalidResult() {
        CnpError error = new CnpError("Test error");
        ValidationResult result = ValidationResult.invalid(error);
        assertFalse(result.isValid());
        assertTrue(result instanceof InvalidResult);
        InvalidResult invalid = (InvalidResult) result;
        assertEquals(error, invalid.getError());
    }

    @Test
    void testValidResultToString() {
        ValidationResult result = ValidationResult.valid();
        String str = result.toString();
        assertNotNull(str);
        assertTrue(str.contains("ValidResult"));
    }

    @Test
    void testInvalidResultToString() {
        CnpError error = new CnpError("Test error message");
        ValidationResult result = ValidationResult.invalid(error);
        String str = result.toString();
        assertNotNull(str);
        assertTrue(str.contains("InvalidResult"));
    }
}

