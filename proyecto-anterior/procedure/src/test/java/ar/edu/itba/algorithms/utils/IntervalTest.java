package ar.edu.itba.algorithms.utils;

import ar.edu.itba.algorithms.utils.interval.Interval;
import ar.edu.itba.algorithms.utils.interval.IntervalParser;
import ar.edu.itba.algorithms.utils.time.DateTime;
import ar.edu.itba.algorithms.utils.time.Year;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IntervalTest {

    @Test
    void fromStringLong() {
        Interval interval = IntervalParser.fromString("2014—2018");
        assertEquals(new Year(2014L), interval.getStartTimeParsed());
        assertEquals(new Year(2018L), interval.getEndTimeParsed());
    }

    @Test
    void fromStringLongWithNow() {
        Interval interval = IntervalParser.fromString("2014—Now");
        assertEquals(new Year(2014L), interval.getStartTimeParsed());
        assertEquals(new Year((long) LocalDateTime.now().getYear()), interval.getEndTimeParsed());
    }

    @Test
    void fromStringDatetime() {
        DateTime start = new DateTime(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));
        DateTime end = new DateTime(LocalDateTime.of(2016, Month.JANUARY, 29, 12, 15, 27));
        Interval interval = IntervalParser.fromString(start + IntervalParser.SEPARATOR + end);
        assertEquals(start.toString(), interval.getStartTimeParsed().toString());
        assertEquals(end.toString(), interval.getEndTimeParsed().toString());
    }

    @Test
    void fromStringDatetimeWithNow() {
        DateTime start = new DateTime(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));
        Interval interval = IntervalParser.fromString(start + IntervalParser.SEPARATOR + "Now");
        assertEquals(start.toString(), interval.getStartTimeParsed().toString());
        assertEquals(LocalDateTime.now().getDayOfYear(), ((DateTime)interval.getEndTimeParsed()).asDateTime().getDayOfYear());
    }

    @Test
    void noIntersectionShouldReturnEmpty() {
        Interval first = IntervalParser.fromString("2014—2019");
        Interval second = IntervalParser.fromString("2020—2022");
        assertFalse(first.intersection(second).isPresent());
    }

    @Test
    void intersection() {
        Interval first = IntervalParser.fromString("2014—2019");
        Interval second = IntervalParser.fromString("2016—2022");
        Optional<Interval> intersection = first.intersection(second);
        assertTrue(intersection.isPresent());
        assertEquals(new Year(2016L).toEpochSecond(false), intersection.get().getStart());
        assertEquals(new Year(2019L).toEpochSecond(true), intersection.get().getEnd());
    }

    @Test
    void contains() {
        Interval interval = IntervalParser.fromString("2014—2018");
        assertFalse(interval.contains(new Year(2013L), false));
        assertFalse(interval.contains(new Year(2013L), true));

        assertTrue(interval.contains(new Year(2014L), false));
        assertTrue(interval.contains(new Year(2014L), true));

        assertTrue(interval.contains(new Year(2015L), false));
        assertTrue(interval.contains(new Year(2015L), true));

        assertTrue(interval.contains(new Year(2018L), false));
        assertTrue(interval.contains(new Year(2018L), true));

        assertFalse(interval.contains(new Year(2019L), false));
        assertFalse(interval.contains(new Year(2019L), true));
    }

    @Test
    void isBetween() {
        Interval interval = IntervalParser.fromString("2001—2010");
        assertTrue(interval.isBetween(IntervalParser.fromString("2000—2018")));
        assertFalse(interval.isBetween(IntervalParser.fromString("2000—2005")));
        assertFalse(interval.isBetween(IntervalParser.fromString("2005—2012")));
        assertFalse(interval.isBetween(IntervalParser.fromString("1995—2000")));
        assertFalse(interval.isBetween(IntervalParser.fromString("2020—2025")));
    }

    @Test
    void isIntersecting() {
        Interval interval = IntervalParser.fromString("2001—2010");
        assertTrue(interval.isIntersecting(IntervalParser.fromString("1998—2001")));
        assertTrue(interval.isIntersecting(IntervalParser.fromString("2010—2020")));
        assertTrue(interval.isIntersecting(IntervalParser.fromString("2000—2018")));
        assertTrue(interval.isIntersecting(IntervalParser.fromString("2000—2005")));
        assertTrue(interval.isIntersecting(IntervalParser.fromString("2005—2012")));
        assertFalse(interval.isIntersecting(IntervalParser.fromString("1995—2000")));
        assertFalse(interval.isIntersecting(IntervalParser.fromString("2020—2025")));
    }
}
