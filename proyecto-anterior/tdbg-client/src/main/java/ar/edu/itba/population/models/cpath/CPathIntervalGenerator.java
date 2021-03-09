package ar.edu.itba.population.models.cpath;

import ar.edu.itba.population.TimeInterval;
import ar.edu.itba.util.Pair;
import ar.edu.itba.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CPathIntervalGenerator {

    private static final double NOISE_FACTOR = (double) 1/3;

    /**
     * Generates the CPath relationship intervals for a list of nodes
     * @param objectNodeIntervals list of object nodes
     * @param subInterval interval in which the relationships must be included
     * @return the relationship intervals and the CPath interval
     */
    public Pair<List<TimeInterval>, TimeInterval> generate(List<TimeInterval> objectNodeIntervals, TimeInterval subInterval) {
        List<TimeInterval> relationshipIntervals = new ArrayList<>();

        int start = subInterval.getStart();
        int end = subInterval.getEnd();
        int noise = (int) Math.floor(NOISE_FACTOR * (end - start));

        int maxStart = start;
        int minEnd = end;
        for (int i = 0; i < objectNodeIntervals.size() - 1; i++) {

            int startDelta = getDelta(noise);
            int endDelta = getDelta(noise);

            int newStart = start + startDelta;
            int newEnd = end - endDelta;

            if (newStart >= newEnd) {
                newStart = start;
                newEnd = end;
            }

            if (newStart > maxStart)
                maxStart = newStart;

            if (newEnd < minEnd)
                minEnd = newEnd;

            TimeInterval relationshipInterval = new TimeInterval(newStart, newEnd);
            relationshipIntervals.add(relationshipInterval);
        }
        return new Pair<>(relationshipIntervals, new TimeInterval(maxStart, minEnd));
    }

    private int getDelta(int noise) {
        return Utils.randomInteger(0, noise + 1);
    }

}
