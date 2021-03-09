package ar.edu.itba.population.models;

import ar.edu.itba.population.TimeInterval;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Node {

    private final TimeInterval interval;
    private final int id;

    public Node (int id, TimeInterval interval) {
        this.id = id;
        this.interval = interval;
    }

    public int getId(){
        return id;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public abstract String getLabel();

    public Map<String, Object> toMap(){
        List<String> intervals = new LinkedList<>();
        intervals.add(interval.toString());
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("interval", intervals);
        return map;
    }
}
