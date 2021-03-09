package ar.edu.itba.graphs;

import ar.edu.itba.procedures.FlightSmallDataSetTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;
import java.util.Map;

public class GraphTest extends FlightSmallDataSetTest {

    GraphDatabaseService db;
    Transaction tx;

    public GraphTest() {
        super(GraphTest.class);
    }

    @BeforeAll
    void setDb() {
        this.db = this.getEmbeddedDatabaseServer().graph();
    }

    @BeforeEach
    void startTx() {
        this.tx = db.beginTx();
    }

    @AfterEach
    void endTx() {
        this.tx.close();
    }

    Node getAirportWithCityName(String name) {
        Map<String,Object> params = new HashMap<>();
        params.put("name", name);
        Result result = this.db.execute(""
                        + "MATCH (n:Object {title: 'Airport'})-[:LocatedAt]->(:Object {title: 'City'})"
                        + "-->()-->(:Value {value: $name}) "
                        + "RETURN n",
                params
        );
        if (result.hasNext()) {
            return (Node) result.next().get("n");
        }
        return null;
    }

    Node getCity(String name) {
        Map<String,Object> params = new HashMap<>();
        params.put("name", name);
        Result result = this.db.execute(""
                        + "MATCH (n:Object {title: 'City'})"
                        + "-->()-->(:Value {value: $name}) "
                        + "RETURN n",
                params
        );
        if (result.hasNext()) {
            return (Node) result.next().get("n");
        }
        return null;
    }
}
