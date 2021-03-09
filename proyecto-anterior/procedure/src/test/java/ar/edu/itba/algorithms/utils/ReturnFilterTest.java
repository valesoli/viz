package ar.edu.itba.algorithms.utils;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReturnFilterTest {

    private final Map<String, Object> node = new HashMap<>();

    private final Map<String, Object> anotherNode = new HashMap<>();

    private final Map<String, Object> edge = new HashMap<>();

    @Test
    void applyOnlyWithName() {
        final String VARIABLE = "p1";
        Map<String, Object> field = field(VARIABLE, "");
        Map<String, Object> returns = createReturns(field);

        ReturnFilter returnFilter = new ReturnFilter(returns);

        Map<String, Object> record = new HashMap<>();
        record.put(VARIABLE, node);

        Map<String, Object> filtered = returnFilter.apply(record);
        assertEquals(node, filtered.get(VARIABLE));
    }

    @Test
    void variablesShouldBeReturnInOriginalOrder() {
        final String variable = "variable2";
        final String anotherVariable = "variable1";
        Map<String, Object> field = field(variable, "");
        Map<String, Object> field2 = field(anotherVariable, "");
        Map<String, Object> returns = createReturns(field, field2);

        ReturnFilter returnFilter = new ReturnFilter(returns);

        Map<String, Object> record = new HashMap<>();
        record.put(anotherVariable, anotherNode);
        record.put(variable, node);

        Map<String, Object> filtered = returnFilter.apply(record);
        assertEquals(Arrays.asList("variable2", "variable1"), new ArrayList<>(filtered.keySet()));
        assertEquals(node, filtered.get(variable));
        assertEquals(anotherNode, filtered.get(anotherVariable));
    }

    @Test
    void applyWithAlias() {
        String variable = "p1";
        String alias = "pepe";
        Map<String, Object> field = field(variable, alias);
        Map<String, Object> returns = createReturns(field);

        ReturnFilter returnFilter = new ReturnFilter(returns);

        Map<String, Object> record = new HashMap<>();
        record.put(variable, node);

        Map<String, Object> filtered = returnFilter.apply(record);
        assertEquals(node, filtered.get(alias));
    }

    @Test
    void applyWithEdge() {
        final String VARIABLE = "e";
        Map<String, Object> field = field(VARIABLE, "");
        Map<String, Object> returns = createReturns(field);

        ReturnFilter returnFilter = new ReturnFilter(returns);

        Map<String, Object> record = new HashMap<>();
        record.put(VARIABLE, edge);

        Map<String, Object> filtered = returnFilter.apply(record);
        assertEquals(edge, filtered.get(VARIABLE));
    }

    @Test
    void applyWithListOfEdges() {
        final String VARIABLE = "e";
        Map<String, Object> field = field(VARIABLE, "");
        Map<String, Object> returns = createReturns(field);

        ReturnFilter returnFilter = new ReturnFilter(returns);

        Map<String, Object> record = new HashMap<>();
        List<Map<String, Object>> edges = new ArrayList<>();
        edges.add(edge);
        record.put(VARIABLE, edges);

        Map<String, Object> filtered = returnFilter.apply(record);
        assertEquals(edges, filtered.get(VARIABLE));
    }

    @SafeVarargs
    private final Map<String, Object> createReturns(Map<String, Object>... fields) {
        List<Map<String, Object>> list = new ArrayList<>(Arrays.asList(fields));
        Map<String, Object> returns = new HashMap<>();
        returns.put("ret", list);
        return returns;
    }

    private Map<String, Object> field(String name, String alias) {
        Map<String, Object> fieldObject = new HashMap<>();
        fieldObject.put("name", name);
        fieldObject.put("alias", alias);
        return fieldObject;
    }
}