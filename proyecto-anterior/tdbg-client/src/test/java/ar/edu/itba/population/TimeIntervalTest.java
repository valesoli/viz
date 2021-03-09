package ar.edu.itba.population;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TimeIntervalTest {

    @Test
    public void intersectionOfSameInterval() {
        TimeInterval interval = new TimeInterval(1950, 1980);
        Optional<TimeInterval> intersection = TimeInterval.intersection(interval, interval);
        assertTrue(intersection.isPresent());
        assertEquals(1950, intersection.get().getStart());
        assertEquals(1980, intersection.get().getEnd());
    }

    @Test
    public void intersectionOfOverlappingIntervals() {
        TimeInterval interval = new TimeInterval(1950, 1980);
        TimeInterval otherInterval = new TimeInterval(1940, 1960);
        Optional<TimeInterval> intersection = TimeInterval.intersection(interval, otherInterval);
        assertTrue(intersection.isPresent());
        assertEquals(1950, intersection.get().getStart());
        assertEquals(1960, intersection.get().getEnd());
    }

    @Test
    public void disjointIntervalsIntersection() {
        TimeInterval interval1 = new TimeInterval(1950, 1980);
        TimeInterval interval2 = new TimeInterval(1980, 1981);
        assertFalse(TimeInterval.intersection(interval1, interval2).isPresent());
    }

    @Test
    public void randomInterval() {
        int origin = 1950;
        int bound = 1980;
        TimeInterval timeInterval = TimeInterval.randomInterval(origin, bound);
        assertTrue(timeInterval.getStart() >= origin);
        assertTrue(timeInterval.getStart() < timeInterval.getEnd());
        assertTrue(timeInterval.getEnd() <= bound);
    }

    @Test
    public void createRandomDisjointIntervals() {
        int start = 1950;
        int end = 1960;
        int intervalAmount = 2;
        List<TimeInterval> intervals = TimeInterval.createRandomDisjointIntervals(new TimeInterval(start, end), intervalAmount);

        assertTrue(intervals.size() <= intervalAmount);
        TimeInterval firstInterval = intervals.get(0);

        assertTrue(firstInterval.getStart() >= start);
        assertTrue(firstInterval.getEnd() <= end);
        if (intervals.size() == intervalAmount) {
            TimeInterval secondInterval = intervals.get(1);
            assertTrue(firstInterval.getStart() < secondInterval.getStart());
            assertTrue(firstInterval.endsBefore(secondInterval));
            assertTrue(secondInterval.getStart() >= start);
            assertTrue(secondInterval.getEnd() <= end);
        }
    }

    @Test
    public void createRandomIntervalsShouldReturnTheSameInterval() {
        int start = 1950;
        int end = 1951;
        int intervalAmount = 2;
        List<TimeInterval> intervals = TimeInterval.createRandomDisjointIntervals(new TimeInterval(start, end), intervalAmount);

        TimeInterval firstInterval = intervals.get(0);
        assertEquals(1, intervals.size());
        assertEquals(1950, firstInterval.getStart());
        assertEquals(1951, firstInterval.getEnd());
    }

    @Test
    public void createRandomConsecutiveIntervals() {
        int start = 1920;
        int end = 1925;
        int intervalAmount = 2;
        List<TimeInterval> intervals = TimeInterval.createRandomConsecutiveIntervals(new TimeInterval(start, end), intervalAmount);

        assertEquals(intervalAmount, intervals.size());

        TimeInterval firstInterval = intervals.get(0);
        TimeInterval secondInterval = intervals.get(1);
        assertTrue(firstInterval.getStart() < secondInterval.getStart());
        assertEquals(firstInterval.getEnd() + 1, secondInterval.getStart());
    }

    @Test
    public void createRandomConsecutiveIntervalsTrivial() {
        int start = 1920;
        int end = 1923;
        int intervalAmount = 2;
        List<TimeInterval> intervals = TimeInterval.createRandomConsecutiveIntervals(new TimeInterval(start, end), intervalAmount);

        assertEquals(intervalAmount, intervals.size());

        TimeInterval firstInterval = intervals.get(0);
        TimeInterval secondInterval = intervals.get(1);
        assertEquals(1920, firstInterval.getStart());
        assertEquals(1921, firstInterval.getEnd());
        assertEquals(1922, secondInterval.getStart());
        assertEquals(1923, secondInterval.getEnd());
    }

    @Test
    public void createRandomConsecutiveIntervalsEdgeCase() {
        int start = 1920;
        int end = 1921;
        int intervalAmount = 1;
        List<TimeInterval> intervals = TimeInterval.createRandomConsecutiveIntervals(new TimeInterval(start, end), intervalAmount);

        assertEquals(intervalAmount, intervals.size());

        TimeInterval interval = intervals.get(0);
        assertEquals(1920, interval.getStart());
        assertEquals(1921, interval.getEnd());
    }

    @Test
    public void list() {
        TimeIntervalListWithStats intervalsWithStats = TimeInterval.list(4);
        List<TimeInterval> intervals = intervalsWithStats.getIntervals();
        assertEquals(4, intervals.size());
    }

    @Test
    public void listMaxStart() {
        TimeIntervalListWithStats intervalsWithStats = TimeInterval.list(4);
        List<TimeInterval> intervals = intervalsWithStats.getIntervals();
        int maxStart = intervalsWithStats.getMaxStart();
        List<Integer> starts = intervals.stream().map(TimeInterval::getStart).collect(Collectors.toList());
        assertTrue(starts.contains(maxStart));
        assertTrue(starts.stream().allMatch(start -> start <= maxStart));
    }

    @Test
    public void listMinEnd() {
        TimeIntervalListWithStats intervalsWithStats = TimeInterval.list(4);
        List<TimeInterval> intervals = intervalsWithStats.getIntervals();
        int minEnd = intervalsWithStats.getMinEnd();
        List<Integer> ends = intervals.stream().map(TimeInterval::getEnd).collect(Collectors.toList());
        assertTrue(ends.contains(minEnd));
        assertTrue(ends.stream().allMatch(end -> end >= minEnd));
    }

    @Test(expected = IllegalArgumentException.class)
    public void listWithInvalidArgument() {
        TimeInterval.list(-3);
    }

    @Test
    public void getRandomSubInterval() {
        TimeInterval t = new TimeInterval(2010, 2020);
        TimeInterval subInterval = t.getRandomSubInterval();
        assertTrue(subInterval.getStart() >= 2010);
        assertTrue(subInterval.getEnd() <= 2020);
    }
}
