package ar.edu.itba.population;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DateTimeIntervalTest {

    @Test
    public void intersectionOfSameInterval() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<DateTimeInterval> intervals = DateTimeInterval.randomDateTimeIntervals(3, currentTime, 60);

        assertEquals(3, intervals.size());

        DateTimeInterval firstInterval = intervals.get(0);
        DateTimeInterval secondInterval = intervals.get(1);
        assertTrue(firstInterval.getStart().isBefore(firstInterval.getEnd()));
        assertTrue(secondInterval.getStart().isAfter(firstInterval.getEnd()));
        assertTrue(secondInterval.getStart().isBefore(secondInterval.getEnd()));
    }
}
