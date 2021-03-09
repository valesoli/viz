package ar.edu.itba.records.utils;

import org.neo4j.graphdb.Node;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributeSerializerTestUtils {
    public static Map<String, List<Node>> attributesFor(String attributeName, List<Node> nodes) {
        Map<String, List<Node>> attributes = new LinkedHashMap<>();
        attributes.put(attributeName, nodes);
        return attributes;
    }
}
