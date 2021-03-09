package ar.edu.itba.algorithms.utils;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EntitySerializerTest {

    @Mock
    Node node = mock(Node.class);

    @Test
    public void serialize() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("label", "Object");
        properties.put("interval", "[1988—Now]");

        when(node.getAllProperties()).thenReturn(properties);

        Map<String, Object> record = new HashMap<>();
        record.put("p", node);

        Map<String, Object> serialized = EntitySerializer.serialize(record);
        Map<String, Object> person = (Map<String, Object>) serialized.get("p");
        assertEquals("Object", person.get("label"));
        assertEquals("[1988—Now]", person.get("interval"));
    }
}