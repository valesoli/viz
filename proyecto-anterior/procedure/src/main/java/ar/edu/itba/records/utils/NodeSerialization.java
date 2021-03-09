package ar.edu.itba.records.utils;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class NodeSerialization {

    public static Map<String, Object> fromNode(Node node, AttributesSerializer attributesSerializer,
                                               GraphDatabaseService db) {
        Map<String, Object> serialization = new LinkedHashMap<>(node.getAllProperties());
        Result result = getNodeAttributes(node.getId(), db);
        serialization.put("attributes", serializeAttributes(result, attributesSerializer));
        return serialization;
    }

    private static Result getNodeAttributes(Long nodeId, GraphDatabaseService db) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", nodeId);
        return db.execute("MATCH (n)-->(a:Attribute)-->(v:Value) " +
                "WHERE id(n) = $id RETURN a.title as attribute, v as value", params);
    }

    private static Map<String, Object> serializeAttributes(Result result, AttributesSerializer attributesSerializer) {
        AttributeValueVisitor m = new AttributeValueVisitor();
        try {
            result.accept(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attributesSerializer.serialize(m.getAttributes());
    }
}
