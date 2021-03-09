package ar.edu.itba.procedures.temporal;

import ar.edu.itba.procedures.PeopleDatasetTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Value;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BetweenProcedureTest extends PeopleDatasetTest {

    BetweenProcedureTest() {
        super(BetweenProcedure.class);
    }

    @Test
    void shouldReturnBetween() {

        String INTERVAL = "'2012—2014'";
        final String subQuery = "MATCH (p1:Object {title: 'Person'})-[e:Friend]-(p2:Object {title: 'Person'})," +
                "(p2)-->(a:Attribute {title: 'Name'})-->(v:Value),(p1)-->(n1:Attribute {title: 'Name'})-->(v1:Value)\n" +
                "WHERE v1.value = 'Jamaal Jenkins'\n" +
                "RETURN *";

        final String returns = "{ret: [ { name: 'v', alias: 'friend_name', field: 'value' } ]}";

        String query = buildQuery(subQuery, INTERVAL, returns);

        runQuery(query, result -> {
                    List<Record> resultAsList = result.list();
                    assertThat(resultAsList)
                            .hasSize(2)
                            .extracting(r -> {
                                Value node = r.get("row").get("friend_name");
                                return node.get("value").asString();
                            })
                            .containsExactlyInAnyOrder(
                                    "Marica Koepp",
                                    "Felipe Howe"
                            );
                }
        );
    }

    private String buildQuery(String subQuery, String INTERVAL, String returns) {
        return " CALL " + BetweenProcedure.getName() +"(\"" + subQuery + "\", " + INTERVAL + ", " + returns + ")" +
                " YIELD row RETURN row;";
    }

    @Test
    void shouldReturnBetweenWithList() {

        String INTERVAL = "'2012—2014'";
        final String subQuery = "MATCH (p1:Object {title: 'Person'})-[e:Friend*..2]-(p2:Object {title: 'Person'})," +
                "(p2)-->(a:Attribute {title: 'Name'})-->(v:Value),(p1)-->(n1:Attribute {title: 'Name'})-->(v1:Value)" +
                "WHERE v1.value = 'Jamaal Jenkins'" +
                "RETURN *";

        final String returns = "{ ret: [ { name: 'v', alias: 'friend_name', field: 'value' }, " +
                "{ name: 'e', alias: '', field: '' }] }";

        final String query = buildQuery(subQuery, INTERVAL, returns);

        runQuery(query, result -> {
           List<Record> resultAsList = result.list();
            assertThat(resultAsList)
                    .hasSize(4)
                    .extracting(r -> {
                        Value node = r.get("row").get("friend_name");
                        return node.get("value").asString();
                    })
                    .containsExactlyInAnyOrder(
                            "Marica Koepp",
                            "Felipe Howe",
                            "Edda Jaskolski",
                            "Miss Hayden Spinka"
                    );
        });
    }
}
