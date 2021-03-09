package ar.edu.itba.algorithms.utils.interval;

import ar.edu.itba.algorithms.utils.time.*;

import java.util.Objects;
import java.util.Optional;

public class Interval {

    private final Long start;
    private final Long end;
    private final Granularity granularity;

    public Interval(Long start, Long end, Granularity granularity) {
        this.start = start;
        this.end = end;
        this.granularity = granularity;
        this.checkTime();
    }

    public Interval(DateTime start, DateTime end) {
        this.start = start.toEpochSecond(false);
        this.end = end.toEpochSecond(true);
        this.granularity = Granularity.DATETIME;
        this.checkTime();
    }

    public Interval(Date start, Date end) {
        this.start = start.toEpochSecond(false);
        this.end = end.toEpochSecond(true);
        this.granularity = Granularity.DATE;
        this.checkTime();
    }

    public Interval(YearMonth start, YearMonth end) {
        this.start = start.toEpochSecond(false);
        this.end = end.toEpochSecond(true);
        this.granularity = Granularity.YEAR_MONTH;
        this.checkTime();
    }

    public Interval(Year start, Year end) {
        this.start = start.toEpochSecond(false);
        this.end = end.toEpochSecond(true);
        this.granularity = Granularity.YEAR;
        this.checkTime();
    }

    public void checkTime() {
        if (this.end < this.start) {
            throw new IllegalStateException(
                    "The finish time of the interval " + this.toString() + " is earlier than the starting time");
        }
    }

    public Optional<Interval> intersection(Interval other) {
        if (other == null) { return Optional.of(this); }
        Long start = this.start.compareTo(other.start) >= 0 ? this.start : other.start;
        Long end = this.end.compareTo(other.end) <= 0 ? this.end : other.end;
        if (start.compareTo(end) <= 0) {
            return Optional.of(
                    new Interval(start, end, this.granularity.getSmallerGranularity(other.granularity)));
        } else {
            return Optional.empty();
        }
    }

    public int compareTinier(Interval other) {
        Long thisLength = this.end - this.start;
        Long otherLength = other.end - other.start;
        return thisLength.compareTo(otherLength);
    }

    public int compareEarlier(Interval other) {
        return this.end.compareTo(other.end);
    }

    public int compareLatestArrival(Interval other) {
        return other.end.compareTo(this.end);
    }

    public int compareLatestDeparture(Interval other) {
        return other.start.compareTo(this.start);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start, this.end);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Interval interval = (Interval) obj;
        return this.end.equals(interval.getEnd()) &&
                this.start.equals(interval.getStart());
    }

    @Override
    public String toString() {
        return IntervalStringifier.intervalToString(this);
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public Comparable getStartTimeParsed() {
        return IntervalReverseParser.toTimeClass(start, granularity);
    }

    public Comparable getEndTimeParsed() {
        return IntervalReverseParser.toTimeClass(end, granularity);
    }

    public boolean contains(Long instant) {
        return instant.compareTo(start) >= 0 && instant.compareTo(end) <= 0;
    }

    public boolean contains(TimeClass timeClass, boolean intervalEnd) {
        return this.contains(timeClass.toEpochSecond(intervalEnd));
    }

    public Granularity getGranularity() {
        return this.granularity;
    }

    public boolean isBetween(Interval other) {
        return this.start.compareTo(other.start) > 0
                && this.end.compareTo(other.end) < 0;
    }

    public boolean isIntersecting(Interval other) {
        return this.intersection(other).isPresent();
    }

}
