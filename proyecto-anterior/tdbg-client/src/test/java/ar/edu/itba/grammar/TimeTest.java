package ar.edu.itba.grammar;

import ar.edu.itba.util.Time;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTest {

    @Test
    public void testValidateYearFormat() {
        String validDate = "1995";
        assertTrue(Time.validateTime(validDate));
    }

    @Test
    public void testValidateFullYearFormat() {
        String validDate = "1995-12";
        assertTrue(Time.validateTime(validDate));

        String invalidDate = "1995-13";
        assertFalse(Time.validateTime(invalidDate));

        String invalidFormat = "1995/05";
        assertFalse(Time.validateTime(invalidFormat));
    }

    @Test
    public void testValidateYearInterval() {
        String validFrom = "1995";
        String validTo = "1996";
        assertTrue(Time.validateTimeInterval(validFrom, validTo));

        String invalidFrom = validTo;
        String invalidTo = validFrom;
        assertFalse(Time.validateTimeInterval(invalidFrom, invalidTo));

        invalidFrom = "2000";
        invalidTo = "2000";
        assertFalse(Time.validateTimeInterval(invalidFrom, invalidTo));
    }

    @Test
    public void testValidateFullYearInterval() {
        String validFrom = "1995-05-03 20:00";
        String validTo = "1995-05-03 21:20";
        assertTrue(Time.validateTimeInterval(validFrom, validTo));

        String invalidFrom = validTo;
        String invalidTo = validFrom;
        assertFalse(Time.validateTimeInterval(invalidFrom, invalidTo));

        invalidFrom = "2000-05-05 16:00";
        invalidTo = "2000-05-05 16:00";
        assertFalse(Time.validateTimeInterval(invalidFrom, invalidTo));
    }

    @Test
    public void testValidateDateFormat() {
        String validDate = "1995-05-03";
        assertTrue(Time.validateTime(validDate));

        String invalidDate = "2020-02-30";
        assertFalse(Time.validateTime(invalidDate));

        String invalidFormat = "1995/05/03";
        assertFalse(Time.validateTime(invalidFormat));
    }

    @Test
    public void testValidateFullDateFormat() {
        String validDate = "1995-05-03 20:35";
        assertTrue(Time.validateTime(validDate));

        String invalidDate = "1995-05-03 24:00";
        assertFalse(Time.validateTime(invalidDate));

        String invalidFormat = "1995/05/03 16:00";
        assertFalse(Time.validateTime(invalidFormat));
    }

    @Test
    public void testValidateDateInterval() {
        String validFrom = "1995-05-03";
        String validTo = "1995-05-04";
        assertTrue(Time.validateTimeInterval(validFrom, validTo));

        String invalidFrom = validTo;
        String invalidTo = validFrom;
        assertFalse(Time.validateTimeInterval(invalidFrom, invalidTo));

        invalidFrom = "2000-05-05";
        invalidTo = "2000-05-05";
        assertFalse(Time.validateTimeInterval(invalidFrom, invalidTo));
    }

    @Test
    public void testValidateFullDateInterval() {
        String validFrom = "1995-05-03 20:00";
        String validTo = "1995-05-03 21:20";
        assertTrue(Time.validateTimeInterval(validFrom, validTo));

        String invalidFrom = validTo;
        String invalidTo = validFrom;
        assertFalse(Time.validateTimeInterval(invalidFrom, invalidTo));

        invalidFrom = "2000-05-05 16:00";
        invalidTo = "2000-05-05 16:00";
        assertFalse(Time.validateTimeInterval(invalidFrom, invalidTo));
    }
}
