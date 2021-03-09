package ar.edu.itba.algorithms.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SnapshotIntervalFilterTest {

    @Test
    void filter() {
        Map<String, Object> record = new HashMap<>();

        Map<String, Object> properties = new HashMap<>();
        properties.put("label", "Object");
        properties.put("interval", "[1988—Now]");

        record.put("p", properties);

        Map<String, Object> filtered = SnapshotIntervalFilter.filter(record);
        Map<String, Object> filteredNode = (Map<String, Object>) filtered.get("p");
        assertEquals("Object", filteredNode.get("label"));
        assertNull(filteredNode.get("interval"));
    }
}