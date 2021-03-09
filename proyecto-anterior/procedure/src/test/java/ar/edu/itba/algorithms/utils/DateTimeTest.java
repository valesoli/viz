package ar.edu.itba.algorithms.utils;

import ar.edu.itba.algorithms.utils.time.DateTime;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateTimeTest {

    @Test
    void parse() {
        DateTime dateTime = DateTime.parse("2015-07-29 19:30");
        assertEquals(2015, dateTime.asDateTime().getYear());
        assertEquals(29, dateTime.asDateTime().getDayOfMonth());
        assertEquals(7, dateTime.asDateTime().getMonthValue());
        assertEquals(19, dateTime.asDateTime().getHour());
        assertEquals(30, dateTime.asDateTime().getMinute());
    }

    @Test
    void parseWithoutTime() {
        assertThrows(DateTimeParseException.class, () -> DateTime.parse("2015-07-29"));
    }
}
