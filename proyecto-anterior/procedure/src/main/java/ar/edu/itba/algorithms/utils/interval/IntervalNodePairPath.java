package ar.edu.itba.algorithms.utils.interval;

import java.util.HashSet;
import java.util.Set;

public class IntervalNodePairPath extends IntervalNodePair {

    private Long node;
    private IntervalSet interval;
    private IntervalNodePairPath previous;
    private Long length;
    private Set<Long> previousNodes = new HashSet<>();

    public IntervalNodePairPath(Long node, IntervalSet interval, Long length) {
        super(node, interval);
        this.length = length;
    }

    public void setPrevious(IntervalNodePairPath previous) {
        this.previous = previous;
    }

    public IntervalNodePairPath getPrevious() {
        return this.previous;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public boolean isInPath(Long node) {
        return previousNodes.contains(node);
    }

    public Set<Long> copyPreviousNodes() {
        return new HashSet<>(this.previousNodes);
    }

    public void deletePreviousNodes() {
        this.previousNodes = null;
    }

    public void setPreviousNodes(Set<Long> previousNodes) {
        this.previousNodes = previousNodes;
    }
}
