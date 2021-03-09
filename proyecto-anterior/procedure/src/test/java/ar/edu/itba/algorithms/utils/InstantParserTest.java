package ar.edu.itba.algorithms.utils;

import ar.edu.itba.algorithms.utils.interval.InstantParser;
import ar.edu.itba.algorithms.utils.time.Date;
import ar.edu.itba.algorithms.utils.time.DateTime;
import ar.edu.itba.algorithms.utils.time.Year;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InstantParserTest {

    @Test
    public void parseLong() {
        Long instant = InstantParser.parse("2018").getLeft();
        assertEquals(new Year(2018), DateTime.fromEpochSecond(instant).getYear());
    }

    @Test
    public void parseDate() {
        Long instant = InstantParser.parse("2018-02-25").getLeft();
        Date date = DateTime.fromEpochSecond(instant).getDate();
        assertEquals(2018, date.asLocalDate().getYear());
        assertEquals(Month.FEBRUARY, date.asLocalDate().getMonth());
        assertEquals(25, date.asLocalDate().getDayOfMonth());
    }

    @Test
    public void parseDatetime() {
        Long instantLong = InstantParser.parse("2018-02-25 13:40").getLeft();
        DateTime instant = DateTime.fromEpochSecond(instantLong);
        assertEquals(2018, instant.asDateTime().getYear());
        assertEquals(Month.FEBRUARY, instant.asDateTime().getMonth());
        assertEquals(25, instant.asDateTime().getDayOfMonth());
        assertEquals(13, instant.asDateTime().getHour());
        assertEquals(40, instant.asDateTime().getMinute());
    }

    @Test
    public void parseYearMonth() {
        Long instant = InstantParser.parse("2018-02").getLeft();
        YearMonth yearMonth = DateTime.fromEpochSecond(instant).getYearMonth().asYearMonth();
        assertEquals(2018, yearMonth.getYear());
        assertEquals(Month.FEBRUARY, yearMonth.getMonth());
    }

    @Test()
    public void parseYearMonthWithTime() {
        assertThrows(DateTimeParseException.class, () -> InstantParser.parse("2018-02 12:15"));
    }
}