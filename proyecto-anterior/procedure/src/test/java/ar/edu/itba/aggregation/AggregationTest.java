package ar.edu.itba.aggregation;

import ar.edu.itba.procedures.EmbeddedNeo4jTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.v1.*;
import org.neo4j.harness.TestServerBuilders;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AggregationTest extends EmbeddedNeo4jTest {

    private String getQuery(String function) {
        final String EARLIEST_PATH = "{path: [1,2,3], interval: [\"0000 — 0100\"]}";
        final String SHORTEST_PATH = "{path: [1,2], interval: [\"1900 — 2000\"]}";
        final String LATEST_DEPARTURE_PATH = "{path: [1,2,3,4,5], interval: [\"2021 — 2023\"]}";
        final String LATEST_ARRIVAL_PATH = "{path: [1,2,3,4,5,6], interval: [\"2020 — 2030\"]}";
        final String FASTEST_PATH = "{path: [1,2,3.4], interval: [\"2019 — 2020\"]}";
        return "UNWIND ["
                + EARLIEST_PATH + ","
                + SHORTEST_PATH + ","
                + LATEST_ARRIVAL_PATH + ","
                + LATEST_DEPARTURE_PATH + ","
                + FASTEST_PATH + "]"
                + " AS path RETURN " + function + "(path) AS result";
    }

    @BeforeAll
    void initializeNeo4j() {
        setEmbeddedDatabaseServer(
                buildEmptyServer(TestServerBuilders
                        .newInProcessBuilder()
                        .withAggregationFunction(EarliestAggregationFunction.class)
                        .withAggregationFunction(LatestArrivalAggregationFunction.class)
                        .withAggregationFunction(LatestDepartureAggregationFunction.class)
                        .withAggregationFunction(FastestAggregationFunction.class)
                        .withAggregationFunction(ShortestAggregationFunction.class))
        );
    }

    @Override
    public String getFixture() {
        return null;
    }

    @Test
    void shouldReturnTheEarliestPath() {
        this.runQuery(this.getQuery(EarliestAggregationFunction.name), result -> {
            assertThat(result.stream())
                    .hasSize(1)
                    .flatExtracting(r -> {
                        Value listResult = r.get("result");
                        return listResult.asList();
                    })
                    .flatExtracting("interval")
                    .containsOnly("0000 — 0100");
        });
    }

    @Test
    void shouldReturnTheShortestPath() {
        this.runQuery(this.getQuery(ShortestAggregationFunction.name), result -> {
            assertThat(result.stream())
                    .hasSize(1)
                    .flatExtracting(r -> {
                        Value listResult = r.get("result");
                        return listResult.asList();
                    })
                    .extracting(r -> {
                        Map<String, Object> map = (Map<String, Object>) r;
                        List<Long> path = (List<Long>) map.get("path");
                        return path.size();
                    })
                    .containsOnly(2);
        });
    }

    @Test
    void shouldReturnTheFastestPath() {
        this.runQuery(this.getQuery(FastestAggregationFunction.name), result -> {
            assertThat(result.stream())
                    .hasSize(1)
                    .flatExtracting(r -> {
                        Value listResult = r.get("result");
                        return listResult.asList();
                    })
                    .flatExtracting("interval")
                    .containsOnly("2019 — 2020");
        });
    }

    @Test
    void shouldReturnTheLatestDeparturePath() {
        this.runQuery(this.getQuery(LatestDepartureAggregationFunction.name), result -> {
            assertThat(result.stream())
                    .hasSize(1)
                    .flatExtracting(r -> {
                        Value listResult = r.get("result");
                        return listResult.asList();
                    })
                    .flatExtracting("interval")
                    .containsOnly("2021 — 2023");
        });
    }

    @Test
    void shouldReturnTheLatestArrivalPath() {
        this.runQuery(this.getQuery(LatestArrivalAggregationFunction.name), result -> {
            assertThat(result.stream())
                    .hasSize(1)
                    .flatExtracting(r -> {
                        Value listResult = r.get("result");
                        return listResult.asList();
                    })
                    .flatExtracting("interval")
                    .containsOnly("2020 — 2030");
        });
    }
}
