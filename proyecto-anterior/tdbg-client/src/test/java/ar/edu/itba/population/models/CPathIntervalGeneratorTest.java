package ar.edu.itba.population.models;

import ar.edu.itba.population.TimeInterval;
import ar.edu.itba.population.models.cpath.CPathIntervalGenerator;
import ar.edu.itba.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CPathIntervalGeneratorTest {

    private List<TimeInterval> objectNodeIntervals;

    @Before
    public void beforeAll() {
        this.objectNodeIntervals = Arrays.asList(
                new TimeInterval(1990, 2019),
                new TimeInterval(1991, 2020),
                new TimeInterval(1992, 2020),
                new TimeInterval(1993, 2018)
        );
    }

    @Test
    public void relationshipIntervals() {

        CPathIntervalGenerator cPathIntervalGenerator = new CPathIntervalGenerator();

        TimeInterval subInterval = new TimeInterval(2003, 2014);

        Pair<List<TimeInterval>, TimeInterval> cPathIntervals = cPathIntervalGenerator.generate(objectNodeIntervals, subInterval);
        List<TimeInterval> relationshipIntervals = cPathIntervals.getLeft();
        TimeInterval cPathInterval = cPathIntervals.getRight();
        assertEquals(3, relationshipIntervals.size());
        List<Integer> starts = relationshipIntervals.stream().map(TimeInterval::getStart).collect(Collectors.toList());
        List<Integer> ends = relationshipIntervals.stream().map(TimeInterval::getEnd).collect(Collectors.toList());
        assertThat(starts, everyItem(greaterThanOrEqualTo(2003)));
        assertThat(starts, everyItem(lessThanOrEqualTo(cPathInterval.getStart())));
        assertThat(ends, everyItem(lessThanOrEqualTo(2014)));
        assertThat(ends, everyItem(greaterThanOrEqualTo(cPathInterval.getEnd())));
    }

    @Test
    public void smallSubInterval() {
        CPathIntervalGenerator cPathIntervalGenerator = new CPathIntervalGenerator();

        TimeInterval subInterval = new TimeInterval(2016, 2017);

        Pair<List<TimeInterval>, TimeInterval> cPathIntervals = cPathIntervalGenerator.generate(objectNodeIntervals, subInterval);
        List<TimeInterval> relationshipIntervals = cPathIntervals.getLeft();
        TimeInterval cPathInterval = cPathIntervals.getRight();
        assertEquals(3, relationshipIntervals.size());
        List<Integer> starts = relationshipIntervals.stream().map(TimeInterval::getStart).collect(Collectors.toList());
        List<Integer> ends = relationshipIntervals.stream().map(TimeInterval::getEnd).collect(Collectors.toList());
        assertThat(starts, everyItem(greaterThanOrEqualTo(2016)));
        assertThat(ends, everyItem(lessThanOrEqualTo(2017)));
        assertThat(cPathInterval.getStart(), is(2016));
        assertThat(cPathInterval.getEnd(), is(2017));
    }

}