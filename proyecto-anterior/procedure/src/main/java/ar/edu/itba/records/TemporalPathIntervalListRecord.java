package ar.edu.itba.records;

import ar.edu.itba.algorithms.utils.interval.*;
import ar.edu.itba.records.utils.IntervalIntersectionAttributesSerializer;
import ar.edu.itba.records.utils.IntervalTree;
import ar.edu.itba.records.utils.NodeSerialization;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemporalPathIntervalListRecord {

    public final List<Map<String, Object>> path;
    public final List<List<String>> intervals;

    public TemporalPathIntervalListRecord(List<Map<String, Object>> path, List<List<String>> intervals) {
        this.path = path;
        this.intervals = intervals;
    }

    public List<Map<String, Object>> getPath() {
        return path;
    }

    public List<List<String>> getIntervals() {
        return intervals;
    }

    private static List<List<String>> getIntervalsFromPath(IntervalNodePairPath path, Granularity granularity) {
        List<IntervalTree> intervalTrees = new LinkedList<>();

        IntervalNodePairPath previousPath;
        IntervalSet currentSet = path.getIntervalSet();
        previousPath = path.getPrevious();
        if (currentSet == null || previousPath == null) {
            return new LinkedList<>();
        }
        currentSet.intersection(previousPath.getIntervalSet()).getIntervals().forEach(
                i -> intervalTrees.add(new IntervalTree(i))
        );
        path = previousPath;

        while (path != null) {
            currentSet = path.getIntervalSet();
            previousPath = path.getPrevious();
            if (currentSet != null && previousPath.getIntervalSet() != null) {
                IntervalSet intersection = currentSet.intersection(previousPath.getIntervalSet());
                intervalTrees.forEach(intervalTree -> intervalTree.getNextNodes(intersection));
            }
            path = previousPath;
        }

        Set<List<String>> result = new HashSet<>();
        intervalTrees.forEach(intervalTree -> result.addAll(intervalTree.getPaths(granularity)));
        return new LinkedList<>(result);
    }

    public static Stream<TemporalPathIntervalListRecord> getRecordsFromSolutionList(
            List<IntervalNodePairPath> paths, GraphDatabaseService db, Granularity granularity) {
        return paths.parallelStream().filter(Objects::nonNull).map(path -> {
            LinkedList<Node> nodes = new LinkedList<>();
            IntervalNodePairPath current = path;

            while (current != null) {
                nodes.addFirst(db.getNodeById(current.getNode()));
                current = current.getPrevious();
            }
            List<Map<String, Object>> serializedNodes = nodes.stream()
                    .map(node -> NodeSerialization.fromNode(node,
                            new IntervalIntersectionAttributesSerializer(path.getIntervalSet().getIntervals()), db))
                    .collect(Collectors.toList());

            return new TemporalPathIntervalListRecord(
                    serializedNodes,
                    getIntervalsFromPath(path, granularity)
            );
        });
    }
}
