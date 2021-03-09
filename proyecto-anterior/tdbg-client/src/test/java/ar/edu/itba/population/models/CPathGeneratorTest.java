package ar.edu.itba.population.models;

import ar.edu.itba.population.TimeInterval;
import ar.edu.itba.population.TimeIntervalListWithStats;
import ar.edu.itba.population.models.cpath.CPathGenerator;
import ar.edu.itba.population.models.cpath.CPathIntervalGenerator;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class CPathGeneratorTest {

    @Mock
    private final CPathIntervalGenerator intervalGenerator = mock(CPathIntervalGenerator.class);

    @Test
    public void generate() {

        List<TimeInterval> objectNodeIntervals = Arrays.asList(
            new TimeInterval(1990, 2019),
            new TimeInterval(1991, 2020),
            new TimeInterval(1992, 2020),
            new TimeInterval(1993, 2018)
        );

        CPathGenerator cPathGenerator = new CPathGenerator(intervalGenerator);

        TimeIntervalListWithStats ils = new TimeIntervalListWithStats(objectNodeIntervals, 1993, 2018);

        cPathGenerator.generate(ils);
        verify(intervalGenerator).generate(eq(objectNodeIntervals),
                argThat((TimeInterval t) -> t.getStart() >= 1993 && t.getEnd() <= 2018));
    }

}