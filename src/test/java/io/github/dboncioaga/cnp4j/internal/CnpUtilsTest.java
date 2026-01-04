package io.github.dboncioaga.cnp4j.internal;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CnpUtilsTest {

    @Test
    void testIsValid() {
        String validCnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        assertTrue(CnpUtils.isValid(validCnp));
        assertFalse(CnpUtils.isValid(null));
        assertFalse(CnpUtils.isValid("1234567890123"));
    }

    @Test
    void testValidate() {
        String validCnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        ValidationResult result = CnpUtils.validate(validCnp);
        assertTrue(result.isValid());

        ValidationResult invalidResult = CnpUtils.validate("123456789012");
        assertFalse(invalidResult.isValid());
        assertTrue(invalidResult instanceof InvalidResult);
    }

    @Test
    void testGetDateOfBirth() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        LocalDate dob = CnpUtils.getDateOfBirth(cnp);
        assertNotNull(dob);
        assertEquals(1980, dob.getYear());
        assertEquals(1, dob.getMonthValue());
        assertEquals(1, dob.getDayOfMonth());
    }

    @Test
    void testGetDateOfBirthWithNull() {
        assertNull(CnpUtils.getDateOfBirth(null));
    }

    @Test
    void testGetDateOfBirthWithInvalidCNP() {
        assertNull(CnpUtils.getDateOfBirth("1234567890123"));
    }

    @Test
    void testGetDateOfBirthForDifferentCenturies() {
        // 1900-1999 (S=1)
        String cnp1900 = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        LocalDate dob1900 = CnpUtils.getDateOfBirth(cnp1900);
        assertNotNull(dob1900);
        assertEquals(1980, dob1900.getYear());

        // 2000-2099 (S=5)
        String cnp2000 = CnpGenerator.generate(5, 20, 1, 1, 22, 115);
        LocalDate dob2000 = CnpUtils.getDateOfBirth(cnp2000);
        assertNotNull(dob2000);
        assertEquals(2020, dob2000.getYear());

        // 1800-1899 (S=3)
        String cnp1800 = CnpGenerator.generate(3, 80, 1, 1, 22, 116);
        LocalDate dob1800 = CnpUtils.getDateOfBirth(cnp1800);
        assertNotNull(dob1800);
        assertEquals(1880, dob1800.getYear());
    }

    @Test
    void testGetSex() {
        String maleCnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        assertEquals("M", CnpUtils.getSex(maleCnp));

        String femaleCnp = CnpGenerator.generate(2, 85, 6, 15, 40, 123);
        assertEquals("F", CnpUtils.getSex(femaleCnp));
    }

    @Test
    void testGetSexWithNull() {
        assertNull(CnpUtils.getSex(null));
    }

    @Test
    void testGetSexWithInvalidCNP() {
        assertNull(CnpUtils.getSex("1234567890123"));
    }

    @Test
    void testGetCountyCode() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        Integer countyCode = CnpUtils.getCountyCode(cnp);
        assertNotNull(countyCode);
        assertEquals(22, countyCode);
    }

    @Test
    void testGetCountyCodeWithNull() {
        assertNull(CnpUtils.getCountyCode(null));
    }

    @Test
    void testGetCountyCodeWithInvalidCNP() {
        assertNull(CnpUtils.getCountyCode("1234567890123"));
    }

    @Test
    void testGetCounty() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        String county = CnpUtils.getCounty(cnp);
        assertNotNull(county);
        assertEquals("Iași", county);
    }

    @Test
    void testGetCountyWithSII() {
        String siiCnp = CnpGenerator.generate(5, 20, 1, 1, 70, 119);
        String county = CnpUtils.getCounty(siiCnp);
        assertNotNull(county);
        assertEquals("Sistem Informatic Integrat (SII)", county);
    }

    @Test
    void testGetCountyWithNull() {
        assertNull(CnpUtils.getCounty(null));
    }

    @Test
    void testGetCountyWithInvalidCNP() {
        assertNull(CnpUtils.getCounty("1234567890123"));
    }

    @Test
    void testGetAge() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        Integer age = CnpUtils.getAge(cnp);
        assertNotNull(age);
        assertTrue(age >= 0);
        int expectedAge = LocalDate.now().getYear() - 1980;
        assertEquals(expectedAge, age);
    }

    @Test
    void testGetAgeWithNull() {
        assertNull(CnpUtils.getAge(null));
    }

    @Test
    void testGetAgeWithInvalidCNP() {
        assertNull(CnpUtils.getAge("1234567890123"));
    }
}
