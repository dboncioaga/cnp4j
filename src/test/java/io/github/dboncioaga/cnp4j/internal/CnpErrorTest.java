package io.github.dboncioaga.cnp4j.internal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CnpErrorTest {

    @Test
    void testConstructor() {
        String message = "Test error message";
        CnpError error = new CnpError(message);
        assertEquals(message, error.toString());
    }

    @Test
    void testToString() {
        CnpError error = new CnpError("Invalid CNP");
        String str = error.toString();
        assertNotNull(str);
        assertEquals("Invalid CNP", str);
    }
}

