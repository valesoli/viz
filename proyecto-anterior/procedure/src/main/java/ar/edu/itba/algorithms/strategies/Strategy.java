package ar.edu.itba.algorithms.strategies;

import ar.edu.itba.algorithms.Condition;
import ar.edu.itba.algorithms.utils.interval.Interval;

import java.util.List;

public interface Strategy<T, K> {

    T getNext();

    void addToFrontier(T node);

    boolean isFinished();

    void setGoal(Condition<K> finishingCondition);

    T getSolution();

    void expandFrontier(List<Interval> intervalSet, T node, Long otherNodeId);

}
