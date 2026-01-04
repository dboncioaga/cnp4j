package io.github.dboncioaga.cnp4j.internal;

import io.github.dboncioaga.cnp4j.internal.internal.CnpEngine;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CnpEngineTest {

    @Test
    void testIsValidWithValidCNP() {
        String validCnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        assertTrue(CnpEngine.isValid(validCnp));
    }

    @Test
    void testIsValidWithNull() {
        assertFalse(CnpEngine.isValid(null));
    }

    @Test
    void testIsValidWithInvalidLength() {
        assertFalse(CnpEngine.isValid("123456789012"));
        assertFalse(CnpEngine.isValid("12345678901234"));
    }

    @Test
    void testValidateWithValidCNP() {
        String validCnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        ValidationResult result = CnpEngine.validate(validCnp);
        assertTrue(result.isValid());
    }

    @Test
    void testValidateWithNull() {
        ValidationResult result = CnpEngine.validate(null);
        assertFalse(result.isValid());
        assertTrue(result instanceof InvalidResult);
    }

    @Test
    void testValidateWithInvalidLength() {
        ValidationResult result = CnpEngine.validate("123456789012");
        assertFalse(result.isValid());
        assertTrue(result instanceof InvalidResult);
    }

    @Test
    void testGetDateOfBirth() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        LocalDate dob = CnpEngine.getDateOfBirth(cnp);
        assertNotNull(dob);
        assertEquals(1980, dob.getYear());
        assertEquals(1, dob.getMonthValue());
        assertEquals(1, dob.getDayOfMonth());
    }

    @Test
    void testGetDateOfBirthWithNull() {
        assertNull(CnpEngine.getDateOfBirth(null));
    }

    @Test
    void testGetDateOfBirthWithInvalidCNP() {
        assertNull(CnpEngine.getDateOfBirth("1234567890123"));
    }

    @Test
    void testGetSex() {
        String maleCnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        assertEquals("M", CnpEngine.getSex(maleCnp));

        String femaleCnp = CnpGenerator.generate(2, 85, 6, 15, 40, 123);
        assertEquals("F", CnpEngine.getSex(femaleCnp));
    }

    @Test
    void testGetSexWithNull() {
        assertNull(CnpEngine.getSex(null));
    }

    @Test
    void testGetSexWithInvalidCNP() {
        assertNull(CnpEngine.getSex("1234567890123"));
    }

    @Test
    void testGetCountyCode() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        Integer countyCode = CnpEngine.getCountyCode(cnp);
        assertNotNull(countyCode);
        assertEquals(22, countyCode);
    }

    @Test
    void testGetCountyCodeWithNull() {
        assertNull(CnpEngine.getCountyCode(null));
    }

    @Test
    void testGetCounty() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        String county = CnpEngine.getCounty(cnp);
        assertNotNull(county);
        assertEquals("Iași", county);
    }

    @Test
    void testGetCountyWithSII() {
        String siiCnp = CnpGenerator.generate(5, 20, 1, 1, 70, 119);
        String county = CnpEngine.getCounty(siiCnp);
        assertNotNull(county);
        assertEquals("Sistem Informatic Integrat (SII)", county);
    }

    @Test
    void testGetAge() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        Integer age = CnpEngine.getAge(cnp);
        assertNotNull(age);
        assertTrue(age >= 0);
        int expectedAge = LocalDate.now().getYear() - 1980;
        assertEquals(expectedAge, age);
    }

    @Test
    void testGetAgeWithNull() {
        assertNull(CnpEngine.getAge(null));
    }

    @Test
    void testGetAgeWithInvalidCNP() {
        assertNull(CnpEngine.getAge("1234567890123"));
    }
}

