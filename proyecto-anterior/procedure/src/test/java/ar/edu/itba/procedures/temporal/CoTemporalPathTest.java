package ar.edu.itba.procedures.temporal;

import ar.edu.itba.procedures.PeopleDatasetTest;
import ar.edu.itba.procedures.cotemporal.CoTemporalPathsWithPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.v1.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoTemporalPathTest extends PeopleDatasetTest {

    public CoTemporalPathTest() {
        super(CoTemporalPathsWithPath.class);
    }

    @Test
    void shouldReturnTheCoTemporalPaths() {
        try (Driver driver = GraphDatabase.driver(
                getEmbeddedDatabaseServer().boltURI(), getDriverConfig());
             Session session = driver.session()) {
            StatementResult result = session.run(""
                    + "MATCH (p1:Object {title: 'Person'}),(p1)-->(n1:Attribute {title: 'Name'})-->(v1:Value)," +
                      "(p2:Object {title: 'Person'})\n"
                    + "WHERE v1.value = 'Jamaal Jenkins'\n"
                    + "CALL coexisting.coTemporalPaths(p1, p2, 1, 1, {edgesLabel: \"Friend\"})\n"
                    + "YIELD path, interval\n"
                    + "RETURN path, interval");

            List<Record> resultList = result.list();
            assertThat(resultList)
                    .hasSize(4)
                    .flatExtracting(r -> {
                        Value path = r.get("path");
                        return path.asList();
                    })
                    .hasSize(8)
                    .extracting("title")
                    .containsOnly("Person");

            int currentYear = LocalDateTime.now().getYear();
            assertThat(resultList)
                    .flatExtracting(r -> {
                        Value interval = r.get("interval");
                        return interval.asList();
                    }).containsExactlyInAnyOrder(
                        "2009 — 2014",
                        "2017 — 2018",
                        "2017 — " + currentYear,
                        "2009 — 2015",
                        "2016 — " + currentYear
                    );
        }
    }

    @Test
    void existsShouldReturnBoolean() {
        try (Driver driver = GraphDatabase.driver(
                getEmbeddedDatabaseServer().boltURI(), getDriverConfig());
             Session session = driver.session()) {
            StatementResult result = session.run(""
                    + "MATCH (p1:Object {title: 'Person'}),(p1)-->(n1:Attribute {title: 'Name'})-->(v1:Value)," +
                    "(p2:Object {title: 'Person'})\n"
                    + "WHERE v1.value = 'Jamaal Jenkins'\n"
                    + "CALL coexisting.coTemporalPaths.exists(p1, p2, 1, 1, {edgesLabel: \"Friend\"})\n"
                    + "YIELD value\n"
                    + "RETURN value");

            List<Record> resultList = result.list();
            assertThat(resultList)
                    .hasSize(10)
                    .extracting(r -> {
                        Value exists = r.get("value");
                        return exists.asBoolean();
                    }).containsExactly(
                        true,
                        true,
                        false,
                        false,
                        true,
                        true,
                        false,
                        false,
                        false,
                        false
                    );
        }
    }
}
