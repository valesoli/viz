package ar.edu.itba.population.models.cpath;

import ar.edu.itba.population.TimeInterval;
import ar.edu.itba.population.TimeIntervalListWithStats;
import ar.edu.itba.util.Pair;

import java.util.List;

public class CPathGenerator {

    private final CPathIntervalGenerator intervalGenerator;

    public CPathGenerator(CPathIntervalGenerator intervalGenerator) {
        this.intervalGenerator = intervalGenerator;
    }

    public Pair<List<TimeInterval>, TimeInterval> generate(TimeIntervalListWithStats timeIntervalListWithStats) {

        List<TimeInterval> objectNodeIntervals = timeIntervalListWithStats.getIntervals();

        if (objectNodeIntervals.isEmpty()) {
            throw new IllegalArgumentException("objectNodeIntervals list can not be empty");
        }

        TimeInterval intersectionBetweenAll = new TimeInterval(
            timeIntervalListWithStats.getMaxStart(),
            timeIntervalListWithStats.getMinEnd()
        );

        TimeInterval subInterval = intersectionBetweenAll.getRandomSubInterval();
        return intervalGenerator.generate(objectNodeIntervals, subInterval);
    }
}
