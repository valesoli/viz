package ar.edu.itba.algorithms.operations;

import ar.edu.itba.algorithms.utils.interval.InstantParser;
import ar.edu.itba.algorithms.utils.interval.Granularity;
import ar.edu.itba.algorithms.utils.interval.Interval;
import org.apache.commons.lang3.tuple.Pair;

public class Snapshot {

    private final Interval interval;

    public Snapshot(String snapshotTime) {
        Pair<Long, Granularity> instant = InstantParser.parse(snapshotTime);
        Long startTime = instant.getLeft();
        Long endTime = InstantParser.parse(snapshotTime, true).getLeft();
        this.interval = new Interval(startTime, endTime, instant.getRight());
    }

    public boolean shouldApply(Interval other) {
        return other.isIntersecting(interval);
    }

}
