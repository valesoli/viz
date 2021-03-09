package ar.edu.itba.procedures.temporal;

import ar.edu.itba.procedures.PeopleDatasetTest;
import ar.edu.itba.procedures.cotemporal.CoTemporalPathsWithNodes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.v1.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoTemporalPathNodesTest extends PeopleDatasetTest {

    public CoTemporalPathNodesTest() {
        super(CoTemporalPathsWithNodes.class);
    }

    private List<String> doNothing(Object r) {
        return (List<String>) r;
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
                    + "CALL coexisting.coTemporalPathsNodes(p1, p2, 1, 1, {edgesLabel: \"Friend\"})\n"
                    + "YIELD path, intervals\n"
                    + "RETURN path, intervals");

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
                        Value interval = r.get("intervals");
                        return interval.asList();
                    })
                    .flatExtracting(this::doNothing)
                    .containsExactlyInAnyOrder(
                        "2009 — 2014",
                        "2017 — 2018",
                        "2017 — " + currentYear,
                        "2009 — 2015",
                        "2016 — " + currentYear
                    );
        }
    }

    @Test
    void shouldReturnBooleanIfPathExists() {
        try (Driver driver = GraphDatabase.driver(
                getEmbeddedDatabaseServer().boltURI(), getDriverConfig());
             Session session = driver.session()) {
            StatementResult result = session.run(""
                    + "MATCH (p1:Object {title: 'Person'}),(p1)-->(n1:Attribute {title: 'Name'})-->(v1:Value)," +
                    "(p2:Object {title: 'Person'})\n"
                    + "WHERE v1.value = 'Jamaal Jenkins'\n"
                    + "CALL coexisting.coTemporalPathsNodes.exists(p1, p2, 1, 1, {edgesLabel: \"Friend\"})\n"
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
