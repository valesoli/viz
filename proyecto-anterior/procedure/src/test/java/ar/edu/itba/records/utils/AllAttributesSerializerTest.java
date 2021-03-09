package ar.edu.itba.records.utils;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.neo4j.graphdb.Node;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AllAttributesSerializerTest {

    @Mock
    Node node = mock(Node.class);

    @Mock
    Node anotherNode = mock(Node.class);

    @Test
    void serializeSingleAttribute() {
        AllAttributesSerializer serializer = new AllAttributesSerializer();

        mockNode(node, "john", "2010-2017");
        Map<String, List<Node>> attributes = AttributeSerializerTestUtils.attributesFor("name", Collections.singletonList(node));

        Map<String, Object> result = serializer.serialize(attributes);
        assertEquals(1, result.size());
        List<Map<String, Object>> attributeList = (List<Map<String, Object>>) result.get("name");
        assertEquals("john", attributeList.get(0).get("value"));
        assertEquals("2010-2017", attributeList.get(0).get("interval"));
    }

    @Test
    void serializeTwoAttributes() {
        AllAttributesSerializer serializer = new AllAttributesSerializer();

        mockNode(node, "john", "2010-2017");
        Map<String, List<Node>> attributes = AttributeSerializerTestUtils.attributesFor("name", Collections.singletonList(node));

        mockNode(anotherNode, "32", "2010-2017");
        Map<String, List<Node>> attributes2 = AttributeSerializerTestUtils.attributesFor("age", Collections.singletonList(anotherNode));

        attributes.putAll(attributes2);

        Map<String, Object> result = serializer.serialize(attributes);
        assertEquals(2, result.size());
        List<Map<String, Object>> nameAttributeValues = (List<Map<String, Object>>) result.get("name");
        assertEquals("john", nameAttributeValues.get(0).get("value"));
        assertEquals("2010-2017", nameAttributeValues.get(0).get("interval"));

        List<Map<String, Object>> ageAttributeValues = (List<Map<String, Object>>) result.get("age");
        assertEquals("32", ageAttributeValues.get(0).get("value"));
        assertEquals("2010-2017", ageAttributeValues.get(0).get("interval"));
    }

    @Test
    void serializeSingleAttributeWithTwoValues() {
        AllAttributesSerializer serializer = new AllAttributesSerializer();

        mockNode(node, "john", "2010-2017");
        mockNode(anotherNode, "john paul", "2017-2019");
        Map<String, List<Node>> attributes = AttributeSerializerTestUtils.attributesFor("name", Arrays.asList(node, anotherNode));


        Map<String, Object> result = serializer.serialize(attributes);
        assertEquals(1, result.size());
        List<Map<String, Object>> attributeList = (List<Map<String, Object>>) result.get("name");
        assertEquals("john", attributeList.get(0).get("value"));
        assertEquals("2010-2017", attributeList.get(0).get("interval"));

        assertEquals("john paul", attributeList.get(1).get("value"));
        assertEquals("2017-2019", attributeList.get(1).get("interval"));
    }

    private void mockNode(Node node, String value, String interval){
        Map<String, Object> allProperties = new LinkedHashMap<>();
        allProperties.put("value", value);
        allProperties.put("interval", interval);
        when(node.getAllProperties()).thenReturn(allProperties);
    }
}