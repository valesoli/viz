package ar.edu.itba.population.models;

import ar.edu.itba.population.TimeInterval;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;

public class ValueNodeTest {

    @Test
    public void toMap() {
        TimeInterval interval = new TimeInterval(2010, 2015);
        ValueNode valueNode = new ValueNode(1, interval, "John");
        Map<String, Object> asMap = valueNode.toMap();
        assertEquals("John", asMap.get("value"));
        assertEquals(1, asMap.get("id"));
        assertEquals(Collections.singletonList(interval.toString()), asMap.get("interval"));
    }

}