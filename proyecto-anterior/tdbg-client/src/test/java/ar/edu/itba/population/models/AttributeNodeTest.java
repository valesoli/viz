package ar.edu.itba.population.models;

import ar.edu.itba.population.TimeInterval;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;

public class AttributeNodeTest {

    @Test
    public void toMap() {
        TimeInterval interval = new TimeInterval(2010, 2015);
        AttributeNode attributeNode = new AttributeNode(1, interval, "Name");
        Map<String, Object> asMap = attributeNode.toMap();
        assertEquals("Name", asMap.get("title"));
        assertEquals(1, asMap.get("id"));
        assertEquals(Collections.singletonList(interval.toString()), asMap.get("interval"));
    }
}
