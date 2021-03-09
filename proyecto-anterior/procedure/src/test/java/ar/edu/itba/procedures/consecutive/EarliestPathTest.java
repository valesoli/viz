package ar.edu.itba.procedures.consecutive;

import ar.edu.itba.procedures.FlightSmallDataSetTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.v1.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EarliestPathTest extends FlightSmallDataSetTest {

    public EarliestPathTest() {
        super(EarliestArrivalPathProcedure.class);
    }

    private StatementResult callQuery(Session session) {
        return session.run(""
                + "MATCH (n:Object {title: 'Airport'})-[:LocatedAt]->(:Object {title: 'City'})"
                + "-->()-->(:Value {value: 'A'}),"
                + " (m:Object {title: 'Airport'})-[:LocatedAt]->(:Object {title: 'City'})"
                + "-->()-->(:Value {value: 'C'})"
                + " CALL consecutive.earliest(n, m, 1, {direction:\"outgoing\", edgesLabel: \"Flight\"})"
                + " YIELD path, interval"
                + " RETURN path, interval");
    }

    @Test
    void shouldReturnTheEarliestArrivalPath() {
        try (Driver driver = GraphDatabase.driver(
                getEmbeddedDatabaseServer().boltURI(), getDriverConfig());
             Session session = driver.session()) {

            StatementResult result = callQuery(session);

            assertThat(result.stream())
                    .hasSize(1)
                    .flatExtracting(r -> {
                        Value path = r.get("path");
                        return path.asList();
                    })
                    .hasSize(3)
                    .extracting("title")
                    .containsOnly("Airport");
        }
    }

    @Test
    void shouldReturnTheCorrectInterval() {
        try (Driver driver = GraphDatabase.driver(
                getEmbeddedDatabaseServer().boltURI(), getDriverConfig());
             Session session = driver.session()) {

            StatementResult result = callQuery(session);

            assertThat(result.stream())
                    .hasSize(1)
                    .flatExtracting(r -> {
                        Value interval = r.get("interval");
                        return interval.asList();
                    })
                    .hasSize(1)
                    .containsOnly("2018-02-01 — 2018-02-06");
        }
    }
}
