package ar.edu.itba.procedures.temporal;

import ar.edu.itba.procedures.PeopleDatasetTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.v1.Value;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SnapshotProcedureTest extends PeopleDatasetTest {

    SnapshotProcedureTest() {
        super(SnapshotProcedure.class);
    }

    @Test
    void shouldReturnSnapshot() {

        final Integer snapshotTime = 2018;
        final String subQuery = "MATCH (p1:Object {title: 'Person'})-[e:Friend]-(p2:Object {title: 'Person'})," +
                "(p2)-->(a:Attribute {title: 'Name'})-->(v:Value),(p1)-->(n1:Attribute {title: 'Name'})-->(v1:Value)\n" +
                "WHERE v1.value = 'Jamaal Jenkins'\n" +
                "RETURN *";

        final String returns = "{ret: [ { name: 'v', alias: 'friend_name' } ]}";


        String query = queryCall(subQuery, snapshotTime, returns);
        runQuery(query, (result -> {
            assertThat(result.list())
                    .hasSize(3)
                    .extracting(r -> {
                        Value node = r.get("row").get("friend_name");
                        return node.get("value").asString();
                    })
                    .containsExactlyInAnyOrder(
                        "Marica Koepp",
                        "Edda Jaskolski",
                        "Roland Emard"
                    );
        }));
    }

    @Test
    void shouldNotReturnIntervals() {

        final Integer snapshotTime = 2018;
        final String subQuery = "MATCH (p1:Object {title: 'Person'})-[e:Friend]-(p2:Object {title: 'Person'})," +
                "(p1)-->(n1:Attribute {title: 'Name'})-->(v1:Value)\n" +
                "WHERE v1.value = 'Jamaal Jenkins'\n" +
                "RETURN *";

        final String returns = "{ret: [ { name: 'p2', alias: '' } ]}";

        final String query = queryCall(subQuery, snapshotTime, returns);

        runQuery(query, (result) -> {
            assertThat(result.list())
                    .hasSize(3)
                    .extracting(r -> {
                        Value node = r.get("row").get("p2");
                        return node.get("interval").isNull();
                    })
                    .containsOnly(true);
        });
    }

    @Test
    void shouldReturnSnapshotWithList() {

        final Integer snapshotTime = 2018;
        final String subQuery = "MATCH (p1:Object {title: 'Person'})-[e:Friend*2]-(p2:Object {title: 'Person'})," +
                "(p2)-->(a:Attribute {title: 'Name'})-->(v:Value),(p1)-->(n1:Attribute {title: 'Name'})-->(v1:Value)\n" +
                "WHERE v1.value = 'Jamaal Jenkins'\n" +
                "RETURN *";

        final String returns = "{ret: [ { name: 'v', alias: 'friend_name' }," +
                "{ name: 'e', alias: '', field: '' }] }";

        String query = queryCall(subQuery, snapshotTime, returns);

        runQuery(query, result -> {
            assertThat(result.list())
                    .hasSize(2)
                    .extracting(r -> {
                        Value node = r.get("row").get("friend_name");
                        return node.get("value").asString();
                    })
                    .containsExactlyInAnyOrder(
                            "Sanford Johnson",
                            "Shelby Jenkins I"
                    );
        });
    }

    private String queryCall(String subQuery, Integer snapshotTime, String returns) {
        return "CALL " +
                SnapshotProcedure.getName() +
                "(\"" +
                subQuery +
                "\", '" +
                snapshotTime +
                "'," +
                returns +
                ") YIELD row RETURN row;";
    }
}
