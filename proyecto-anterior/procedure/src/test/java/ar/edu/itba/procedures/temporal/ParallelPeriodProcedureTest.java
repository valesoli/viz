package ar.edu.itba.procedures.temporal;

import ar.edu.itba.procedures.ParallelPeriodProcedure;
import ar.edu.itba.procedures.PeopleDatasetTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.v1.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParallelPeriodProcedureTest extends PeopleDatasetTest {

    public ParallelPeriodProcedureTest() {
        super(ParallelPeriodProcedure.class);
    }

    @Test
    void shouldReturnParallelFriends() {
        try (Driver driver = GraphDatabase.driver(
                getEmbeddedDatabaseServer().boltURI(), getDriverConfig());
             Session session = driver.session()) {
            StatementResult result = session.run(""
                    + " MATCH (n:Object {title: 'Person'})-->(m)-->(p:Value {value: 'Jamaal Jenkins'}),"
                    + " (n)-[r:LivedIn]->(:Object {title:'City'})-->(w)-->(:Value {value: 'Lake Silvaville'})"
                    + " CALL coexisting.parallel(n, r.interval[0], 'Friend')"
                    + " YIELD node, initialTime, endingTime"
                    + " RETURN node, initialTime, endingTime");

            assertThat(result.stream())
                    .hasSize(5)
                    .extracting(r -> {
                        Value node = r.get("node");
                        return node.asNode().id();
                    })
                    .containsExactlyInAnyOrder(
                            24L,
                            24L,
                            29L,
                            25L,
                            30L
                    );
        }
    }
}
