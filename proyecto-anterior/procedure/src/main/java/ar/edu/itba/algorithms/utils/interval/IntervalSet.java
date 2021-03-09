package ar.edu.itba.algorithms.utils.interval;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class IntervalSet {

    private final List<Interval> intervals;

    public IntervalSet() {
        this.intervals = new LinkedList<>();
    }

    public IntervalSet(Interval[] intervals) {
        this.intervals = Arrays.asList(intervals);
    }

    public IntervalSet(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public IntervalSet intersection(IntervalSet other) {
        if (other == null) {
            return new IntervalSet(new LinkedList<>(intervals));
        }
        List<Interval> intersection = new LinkedList<>();
        intervals.forEach(
                interval -> other.intervals.forEach(
                        otherInterval -> interval.intersection(otherInterval)
                                                 .ifPresent(intersection::add)
                )
        );
        return new IntervalSet(intersection);
    }

    public IntervalSet intersection(Interval interval) {
        List<Interval> intersection = new LinkedList<>();
        intervals.forEach(
                thisInterval -> thisInterval.intersection(interval).ifPresent(i -> intersection.add((Interval) i))
        );
        return new IntervalSet(intersection);
    }

    public IntervalSet intersectAndReturnThisIntervals(IntervalSet other) {
        if (other == null) {
            return new IntervalSet(new LinkedList<>(intervals));
        }
        List<Interval> intersection = new LinkedList<>();
        intervals.forEach(
                interval -> other.intervals.forEach(
                        otherInterval -> interval.intersection(otherInterval)
                                .ifPresent(n -> intersection.add(interval))
                )
        );
        return new IntervalSet(intersection);
    }

    public IntervalSet union(Interval interval) {
        return this.union(interval, 0L);
    }

    public IntervalSet union(Interval interval, Long difference) {
        Interval lastInterval = intervals.get(intervals.size() - 1);
        if (interval.getStart().compareTo(lastInterval.getEnd()) > 0) {
            List<Interval> newSet = new LinkedList<>(intervals);
            newSet.add(interval);
            return new IntervalSet(newSet);
        }
        return null;
    }

    public Interval getLast() {
        return this.intervals.get(this.intervals.size() - 1);
    }

    public boolean isEmpty() {
        return this.intervals.size() == 0;
    }

    private int compareAux(IntervalSet otherSet, Comparator<Interval> comparator) {
        boolean isLesser;
        for (Interval interval: intervals) {
            isLesser = true;
            for (Interval otherInterval: otherSet.intervals) {
                if (comparator.compare(interval, otherInterval) >= 0) {
                    isLesser = false;
                    break;
                }
            }
            if (isLesser) {
                return -1;
            }
        }
        return 1;
    }
}
