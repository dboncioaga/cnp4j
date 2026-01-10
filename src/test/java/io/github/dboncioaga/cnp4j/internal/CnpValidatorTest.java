package io.github.dboncioaga.cnp4j.internal;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CnpValidatorTest {

    @Test
    void testConstructorWithValidCNP() {
        String validCnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        CnpValidator validator = new CnpValidator(validCnp);
        assertTrue(validator.isValid());
        assertEquals(validCnp, validator.getCnp());
    }

    @Test
    void testConstructorWithInvalidCNP() {
        CnpValidator validator = new CnpValidator("1234567890123");
        assertFalse(validator.isValid());
    }

    @Test
    void testConstructorWithNull() {
        assertThrows(NullPointerException.class, () -> new CnpValidator(null));
    }

    @Test
    void testGetError() {
        CnpValidator validator = new CnpValidator("1234567890123");
        assertFalse(validator.isValid());
        CnpError error = validator.getError();
        assertNotNull(error);
    }

    @Test
    void testGetErrorWithValidCNP() {
        String validCnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        CnpValidator validator = new CnpValidator(validCnp);
        assertTrue(validator.isValid());
        assertNull(validator.getError());
    }

    @Test
    void testGetDateOfBirth() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        CnpValidator validator = new CnpValidator(cnp);
        LocalDate dob = validator.getDateOfBirth();
        assertNotNull(dob);
        assertEquals(1980, dob.getYear());
        assertEquals(1, dob.getMonthValue());
        assertEquals(1, dob.getDayOfMonth());
    }

    @Test
    void testGetDateOfBirthWithInvalidCNP() {
        CnpValidator validator = new CnpValidator("1234567890123");
        assertNull(validator.getDateOfBirth());
    }

    @Test
    void testGetDateOfBirthForDifferentCenturies() {
        // 1900-1999 (S=1)
        String cnp1900 = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        CnpValidator validator1900 = new CnpValidator(cnp1900);
        LocalDate dob1900 = validator1900.getDateOfBirth();
        assertEquals(1980, dob1900.getYear());

        // 2000-2099 (S=5)
        String cnp2000 = CnpGenerator.generate(5, 20, 1, 1, 22, 115);
        CnpValidator validator2000 = new CnpValidator(cnp2000);
        LocalDate dob2000 = validator2000.getDateOfBirth();
        assertEquals(2020, dob2000.getYear());

        // 1800-1899 (S=3)
        String cnp1800 = CnpGenerator.generate(3, 80, 1, 1, 22, 116);
        CnpValidator validator1800 = new CnpValidator(cnp1800);
        LocalDate dob1800 = validator1800.getDateOfBirth();
        assertEquals(1880, dob1800.getYear());
    }

    @Test
    void testGetSex() {
        String maleCnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        CnpValidator validator = new CnpValidator(maleCnp);
        assertEquals("M", validator.getSex());

        String femaleCnp = CnpGenerator.generate(2, 85, 6, 15, 40, 123);
        CnpValidator femaleValidator = new CnpValidator(femaleCnp);
        assertEquals("F", femaleValidator.getSex());
    }

    @Test
    void testGetSexWithInvalidCNP() {
        CnpValidator validator = new CnpValidator("1234567890123");
        assertNull(validator.getSex());
    }

    @Test
    void testGetCountyCode() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        CnpValidator validator = new CnpValidator(cnp);
        Integer countyCode = validator.getCountyCode();
        assertNotNull(countyCode);
        assertEquals(22, countyCode);
    }

    @Test
    void testGetCountyCodeWithInvalidCNP() {
        CnpValidator validator = new CnpValidator("1234567890123");
        assertNull(validator.getCountyCode());
    }

    @Test
    void testGetCounty() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        CnpValidator validator = new CnpValidator(cnp);
        String county = validator.getCounty();
        assertNotNull(county);
        assertEquals("Iași", county);
    }

    @Test
    void testGetCountyWithSII() {
        String siiCnp = CnpGenerator.generate(5, 20, 1, 1, 70, 119);
        CnpValidator validator = new CnpValidator(siiCnp);
        String county = validator.getCounty();
        assertNotNull(county);
        assertEquals("N/A", county);
    }

    @Test
    void testGetCountyWithInvalidCNP() {
        CnpValidator validator = new CnpValidator("1234567890123");
        assertNull(validator.getCounty());
    }

    @Test
    void testGetAge() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        CnpValidator validator = new CnpValidator(cnp);
        Integer age = validator.getAge();
        assertNotNull(age);
        assertTrue(age >= 0);
        int expectedAge = LocalDate.now().getYear() - 1980;
        assertEquals(expectedAge, age);
    }

    @Test
    void testGetAgeWithInvalidCNP() {
        CnpValidator validator = new CnpValidator("1234567890123");
        assertNull(validator.getAge());
    }

    @Test
    void testEquals() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        CnpValidator validator1 = new CnpValidator(cnp);
        CnpValidator validator2 = new CnpValidator(cnp);
        assertEquals(validator1, validator2);
        assertEquals(validator1.hashCode(), validator2.hashCode());
    }

    @Test
    void testToString() {
        String cnp = CnpGenerator.generate(1, 80, 1, 1, 22, 114);
        CnpValidator validator = new CnpValidator(cnp);
        String str = validator.toString();
        assertNotNull(str);
        assertTrue(str.contains(cnp));
        assertTrue(str.contains("valid="));
    }

    @Test
    void testResidentCNP() {
        String residentCnp = CnpGenerator.generate(7, 20, 1, 1, 40, 100);
        CnpValidator validator = new CnpValidator(residentCnp);
        assertTrue(validator.isValid());
        assertEquals("M", validator.getSex());

        String femaleResidentCnp = CnpGenerator.generate(8, 20, 1, 1, 40, 101);
        CnpValidator femaleValidator = new CnpValidator(femaleResidentCnp);
        assertTrue(femaleValidator.isValid());
        assertEquals("F", femaleValidator.getSex());
    }
}
