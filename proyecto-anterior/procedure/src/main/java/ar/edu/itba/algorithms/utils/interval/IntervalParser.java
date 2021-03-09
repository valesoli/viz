package ar.edu.itba.algorithms.utils.interval;

import org.apache.commons.lang3.tuple.Pair;
import org.neo4j.graphdb.Entity;

import java.util.LinkedList;
import java.util.List;

public class IntervalParser {

    private static final String NOW = "now";
    public static final String SEPARATOR = "—";
    public static final String WS_SEPARATOR = " — ";

    public static Interval fromString(String interval) {
        String[] limits = interval.split(SEPARATOR);

        Pair<Long, Granularity> earlyLimit = InstantParser.parse(limits[0]);
        Pair<Long, Granularity> lateLimit;
        if (limits[1].toLowerCase().equals(NOW)) {
            lateLimit = InstantParser.nowValue(earlyLimit.getRight());
        } else {
            lateLimit = InstantParser.parse(limits[1], true);
        }
        return new Interval(
                earlyLimit.getLeft(), lateLimit.getLeft(),
                earlyLimit.getRight());
    }

    public static Interval fromStringWithWhitespace(String interval) {
        String fixedString = interval.replace(WS_SEPARATOR, SEPARATOR);
        return IntervalParser.fromString(fixedString);
    }

    public static List<Interval> entityToIntervals(Entity entity) {
        if (!entity.hasProperty("interval")) {
            return new LinkedList<>();
        }
        String[] intervals = (String[]) entity.getProperty("interval");
        List<Interval> intervalList = new LinkedList<>();
        for (String interval : intervals) {
            intervalList.add(IntervalParser.fromString(interval));
        }
        return intervalList;
    }

    public static List<Interval> fromStringArrayToIntervals(String[] intervals) {
        List<Interval> intervalList = new LinkedList<>();
        for (String interval: intervals) {
            intervalList.add(fromString(interval));
        }
        return intervalList;
    }

}
