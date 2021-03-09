package ar.edu.itba.records.utils;

import ar.edu.itba.algorithms.utils.interval.Interval;
import ar.edu.itba.algorithms.utils.interval.IntervalParser;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.neo4j.graphdb.Node;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IntervalIntersectionAttributesSerializerTest {

    @Mock
    Node node = mock(Node.class);

    @Mock
    Node ageNode = mock(Node.class);

    @Mock
    Node ignoredNode = mock(Node.class);

    @Test
    void serialize() {
        Interval interval = IntervalParser.fromString("2010—2017");
        IntervalIntersectionAttributesSerializer serializer = new IntervalIntersectionAttributesSerializer(Collections.singletonList(interval));

        mockNode("john", "2010—2017", node);
        mockNode("john paul", "2003—2009", ignoredNode);

        Map<String, List<Node>> attributes = AttributeSerializerTestUtils.attributesFor("name", Arrays.asList(node, ignoredNode));

        Map<String, Object> result = serializer.serialize(attributes);
        assertEquals(1, result.size());
        List<Map<String, Object>> attributeList = (List<Map<String, Object>>) result.get("name");
        assertEquals("john", attributeList.get(0).get("value"));
    }

    @Test
    void serializeTwoAttributes() {
        Interval interval = IntervalParser.fromString("2010—2017");
        IntervalIntersectionAttributesSerializer serializer = new IntervalIntersectionAttributesSerializer(Collections.singletonList(interval));

        mockNode("john", "2010—2017", node);
        Map<String, List<Node>> attributes = AttributeSerializerTestUtils.attributesFor("name", Collections.singletonList(node));

        mockNode("32", "2010—2017", ageNode);
        Map<String, List<Node>> attributes2 = AttributeSerializerTestUtils.attributesFor("age", Collections.singletonList(ageNode));

        attributes.putAll(attributes2);

        Map<String, Object> result = serializer.serialize(attributes);
        assertEquals(2, result.size());
        List<Map<String, Object>> nameAttributeValues = (List<Map<String, Object>>) result.get("name");
        assertEquals("john", nameAttributeValues.get(0).get("value"));
        assertEquals("2010 — 2017", nameAttributeValues.get(0).get("interval"));

        List<Map<String, Object>> ageAttributeValues = (List<Map<String, Object>>) result.get("age");
        assertEquals("32", ageAttributeValues.get(0).get("value"));
        assertEquals("2010 — 2017", ageAttributeValues.get(0).get("interval"));
    }

    @Test
    void serializeIgnored() {
        Interval interval = IntervalParser.fromString("2010—2017");
        IntervalIntersectionAttributesSerializer serializer = new IntervalIntersectionAttributesSerializer(Collections.singletonList(interval));

        mockNode("john paul", "2003—2009", ignoredNode);
        Map<String, List<Node>> attributes = AttributeSerializerTestUtils.attributesFor("name", Collections.singletonList(ignoredNode));

        Map<String, Object> result = serializer.serialize(attributes);
        assertEquals(0, result.size());
    }

    private void mockNode(String value, String interval, Node node) {
        Map<String, Object> allProperties = new LinkedHashMap<>();

        allProperties.put("value", value);
        final String INTERVAL_PROPERTY = "interval";
        allProperties.put(INTERVAL_PROPERTY, interval);

        when(node.getAllProperties()).thenReturn(allProperties);
        when(node.hasProperty(INTERVAL_PROPERTY)).thenReturn(true);
        when(node.getProperty(INTERVAL_PROPERTY)).thenReturn(new String[]{interval});
    }

}