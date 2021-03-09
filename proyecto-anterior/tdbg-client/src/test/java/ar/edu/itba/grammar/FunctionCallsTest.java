package ar.edu.itba.grammar;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FunctionCallsTest {

    @Test
    public void testEarliestPath() {
        String query = "SELECT path\nMATCH (n), (m), path = earliestPath((n)-[:Flight*]->(m))";
        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "MATCH (n),(m)\n" +
                "CALL consecutive.earliest(n,m,1,{edgesLabel:'Flight',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.earliest({path: internal_p0, interval: internal_i0}) as path_\n" +
                "UNWIND path_ as path\n" +
                "RETURN path";

        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testFastestPath() {
        final String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = fastestPath((a1)-[:Flight*]->(a2))\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = 'Hiramport' AND internal_v1.value='Port Farahhaven'\n" +
                "CALL consecutive.fastest(a1,a2,1,{edgesLabel:'Flight',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.fastest({path: internal_p0, interval: internal_i0}) as path_\n" +
                "UNWIND path_ as path\n" +
                "RETURN path";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testShortestPath() {
        final String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = shortestPath((a1)-[:Flight*]->(a2))\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = 'Hiramport' AND internal_v1.value='Port Farahhaven'\n" +
                "CALL consecutive.shortest(a1,a2,1,{edgesLabel:'Flight',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.shortest({path: internal_p0, interval: internal_i0}) as path_\n" +
                "UNWIND path_ as path\n" +
                "RETURN path";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testLatestDeparturePath() {
        final String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = latestDEPARTUREPath((a1)-[:Flight*]->(a2))\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = 'Hiramport' AND internal_v1.value='Port Farahhaven'\n" +
                "CALL consecutive.latestDeparture(a1,a2,1,{edgesLabel:'Flight',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.latestDeparture({path: internal_p0, interval: internal_i0}) as path_\n" +
                "UNWIND path_ as path\n" +
                "RETURN path";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testLatestArrivalPath() {
        final String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = latestARRIVALPath((a1)-[:Flight*]->(a2))\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = 'Hiramport' AND internal_v1.value='Port Farahhaven'\n" +
                "CALL consecutive.latestArrival(a1,a2,1,{edgesLabel:'Flight',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.latestArrival({path: internal_p0, interval: internal_i0}) as path_\n" +
                "UNWIND path_ as path\n" +
                "RETURN path";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testLatestDeparturePathYearThreshold() {
        final String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = latestDeparturePath((a1)-[:Flight*]->(a2), '2020')\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = 'Hiramport' AND internal_v1.value='Port Farahhaven'\n" +
                "CALL consecutive.latestDeparture(a1,a2,1,{edgesLabel:'Flight',between:'0001—2020',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.latestDeparture({path: internal_p0, interval: internal_i0}) as path_\n" +
                "UNWIND path_ as path\n" +
                "RETURN path";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testLatestDeparturePathYearMonthThreshold() {
        final String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = latestDeparturePath((a1)-[:Flight*]->(a2), '2020-02')\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = 'Hiramport' AND internal_v1.value='Port Farahhaven'\n" +
                "CALL consecutive.latestDeparture(a1,a2,1,{edgesLabel:'Flight',between:'0001-01—2020-02',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.latestDeparture({path: internal_p0, interval: internal_i0}) as path_\n" +
                "UNWIND path_ as path\n" +
                "RETURN path";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testLatestDeparturePathDateThreshold() {
        final String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = latestDeparturePath((a1)-[:Flight*]->(a2), '2018-12-20')\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = 'Hiramport' AND internal_v1.value='Port Farahhaven'\n" +
                "CALL consecutive.latestDeparture(a1,a2,1,{edgesLabel:'Flight',between:'0001-01-01—2018-12-20',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.latestDeparture({path: internal_p0, interval: internal_i0}) as path_\n" +
                "UNWIND path_ as path\n" +
                "RETURN path";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testLatestDeparturePathDateTimeThreshold() {
        final String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = latestDeparturePath((a1)-[:Flight*]->(a2), '2018-09-20 16:00')\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = 'Hiramport' AND internal_v1.value='Port Farahhaven'\n" +
                "CALL consecutive.latestDeparture(a1,a2,1,{edgesLabel:'Flight',between:'0001-01-01 00:00—2018-09-20 16:00',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.latestDeparture({path: internal_p0, interval: internal_i0}) as path_\n" +
                "UNWIND path_ as path\n" +
                "RETURN path";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void procedureConfigurationWithIncomingEdge() {
        final String query = "SELECT result\n" +
                "MATCH (n), (m), result = fastestpath((n)<-[:Flight*]-(m))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n),(m)\n" +
                "CALL consecutive.fastest(n,m,1,{edgesLabel:'Flight',direction:'incoming'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.fastest({path: internal_p0, interval: internal_i0}) as result_\n" +
                "UNWIND result_ as result\n" +
                "RETURN result";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void procedureConfigurationWithBothWaysEdge() {
        final String query = "SELECT result\n" +
                "MATCH (n), (m), result = earliestpath((n)-[:Flight*]-(m))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n),(m)\n" +
                "CALL consecutive.earliest(n,m,1,{edgesLabel:'Flight',direction:'both'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.earliest({path: internal_p0, interval: internal_i0}) as result_\n" +
                "UNWIND result_ as result\n" +
                "RETURN result";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void selectFunctionCallVariable() {
        final String query = "SELECT result.path, result.interval\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), result = fastestPath((a1)-[:Flight*]->(a2))\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v2:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v3:Value)\n" +
                "WHERE internal_v2.value = 'Hiramport' AND internal_v3.value='Port Farahhaven'\n" +
                "CALL consecutive.fastest(a1,a2,1,{edgesLabel:'Flight',direction:'outgoing'}) YIELD path as internal_p1, interval as internal_i1\n" +
                "WITH paths.intervals.fastest({path: internal_p1, interval: internal_i1}) as result_\n" +
                "UNWIND result_ as result\n" +
                "RETURN result.path, result.interval";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void selectFunctionCallVariableAttrAlias() {
        final String query = "SELECT result.path as path, result.interval as interval\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), result = fastestPath((a1)-[:Flight*]->(a2))\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = 'Hiramport' AND internal_v1.value='Port Farahhaven'\n" +
                "CALL consecutive.fastest(a1,a2,1,{edgesLabel:'Flight',direction:'outgoing'}) YIELD path as internal_p1, interval as internal_i1\n" +
                "WITH paths.intervals.fastest({path: internal_p1, interval: internal_i1}) as result_\n" +
                "UNWIND result_ as result\n" +
                "RETURN result.path as `path`, result.interval as `interval`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void selectFunctionCallMatchVariables() {
        final String query = "SELECT result, c1, c2, a1.Name, c1.Name as c1_Name, c2.Name as c2_Name\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), result = fastestPath((a1)-[:Flight*]->(a2))\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n1:Attribute {title: 'Name'})-->(c1_Name:Value)\n" +
                "MATCH (c2)-->(internal_n2:Attribute {title: 'Name'})-->(c2_Name:Value)\n" +
                "WHERE c1_Name.value = 'Hiramport' AND c2_Name.value='Port Farahhaven'\n" +
                "OPTIONAL MATCH (a1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "CALL consecutive.fastest(a1,a2,1,{edgesLabel:'Flight',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.fastest({path: internal_p0, interval: internal_i0}) as result_, internal_v0, c1, c1_Name, c2, c2_Name\n" +
                "UNWIND result_ as result\n" +
                "RETURN result, c1, c2, internal_v0 as `a1.Name`, c1_Name, c2_Name";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void selectFunctionWildCard() {
        final String query = "SELECT *\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), result = fastestPath((a1)-[:Flight*]->(a2))\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (c1:Object {title: 'City'})<-[internal_l0:LocatedAt]-(a1:Object {title: 'Airport'}),(c2:Object {title: 'City'})<-[internal_l1:LocatedAt]-(a2:Object {title: 'Airport'})\n" +
                "MATCH (c1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (c2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = 'Hiramport' AND internal_v1.value='Port Farahhaven'\n" +
                "CALL consecutive.fastest(a1,a2,1,{edgesLabel:'Flight',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH paths.intervals.fastest({path: internal_p0, interval: internal_i0}) as result_, a1, a2, c1, internal_l1, internal_l0, c2\n" +
                "UNWIND result_ as result\n" +
                "RETURN c1, internal_l0, a1, c2, internal_l1, a2, result";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathWithId() {
        final String query = "SELECT path, path.interval\n" +
                "\tMATCH (n:Person), (m:Person), path = cPath((n) - [:Friend*9] -> (m))\n" +
                "WHERE id(n) = 1 AND id(m) = 2\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'}),(m:Object {title: 'Person'})\n" +
                "WHERE id(n) = 1 AND id(m) = 2\n" +
                "CALL coexisting.coTemporalPaths(n,m,9,9,{edgesLabel:'Friend',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i1\n" +
                "WITH {path: internal_p0, interval: internal_i1} as path\n" +
                "RETURN path, path.interval";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathBool() {
        final String query = "SELECT n\n" +
                "\tMATCH (n:Person)\n" +
                "WHERE n.Name = 'Cathy' AND cPath((n) - [:Friend*2] -> (:Person))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "MATCH (n)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Cathy'\n" +
                "CALL coexisting.coTemporalPaths.exists(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD value as internal_i0\n" +
                "WITH internal_i0, n\n" +
                "WHERE internal_i0\n" +
                "RETURN n";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void pairCPathBool() {
        final String query = "SELECT n\n" +
                "\tMATCH (n:Person)\n" +
                "WHERE n.Name = 'Cathy' AND PaircPath((n) - [:Friend*2] -> (:Person))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "MATCH (n)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Cathy'\n" +
                "CALL coexisting.coTemporalPathsNodes.exists(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD value as internal_i0\n" +
                "WITH internal_i0, n\n" +
                "WHERE internal_i0\n" +
                "RETURN n";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathBoolReversed() {
        final String query = "SELECT n\n" +
                "\tMATCH (n:Person)\n" +
                "WHERE cPath((n) - [:Friend*2] -> (:Person)) AND n.Name = 'Cathy'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "MATCH (n)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Cathy'\n" +
                "CALL coexisting.coTemporalPaths.exists(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD value as internal_i0\n" +
                "WITH internal_i0, n\n" +
                "WHERE internal_i0\n" +
                "RETURN n";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void multipleBooleanFCall() {
        final String query = "SELECT n, m\n" +
                "\tMATCH (n:Person), (m:Person)\n" +
                "WHERE  n.Name = 'Cathy' AND cPath((n) - [:Friend*2] -> (:Person)) OR paircPath((m) - [:Friend*4] -> (:Person)) AND m.Name = 'Julia'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'}),(m:Object {title: 'Person'})\n" +
                "MATCH (m)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "MATCH (n)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Cathy' AND internal_v1.value = 'Julia'\n" +
                "CALL coexisting.coTemporalPaths.exists(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD value as internal_i0\n" +
                "CALL coexisting.coTemporalPathsNodes.exists(m,null,4,4,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD value as internal_i1\n" +
                "WITH internal_i0, internal_i1, m, n\n" +
                "WHERE internal_i0 OR internal_i1\n" +
                "RETURN n, m";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void onlycPathBoolInWhere() {
        final String query = "SELECT n\n" +
                "\tMATCH (n:Person)\n" +
                "WHERE cPath((n) - [:Friend*2] -> (:Person))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths.exists(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD value as internal_i0\n" +
                "WITH internal_i0, n\n" +
                "WHERE internal_i0\n" +
                "RETURN n";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionOneNodeTest() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as p\n" +
                "RETURN p";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionStructSelectAndIndexAccess() {
        final String query = "SELECT n.Name, p.interval as interval, p.path[0].attributes.Name as start_name\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "OPTIONAL MATCH (n)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "CALL coexisting.coTemporalPaths(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i1\n" +
                "WITH {path: internal_p0, interval: internal_i1} as p, internal_v0\n" +
                "RETURN internal_v0 as `n.Name`, p.interval as `interval`, p.path[0].attributes.Name as start_name";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionStructAccessors() {
        final String query = "SELECT head(p.path).attributes.Name as start, last(p.path).attributes.Name as end\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as p\n" +
                "RETURN head(p.path).attributes.Name as start, last(p.path).attributes.Name as end";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void pairCPathTest() {
        final String query = "SELECT p.path, p.interval\n" +
                "MATCH (n:Person),(n2:Person), p=pairCPath((n)-[:Friend*3]->(n2))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'}),(n2:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPathsNodes(n,n2,3,3,{edgesLabel:'Friend',direction:'outgoing'}) YIELD path as internal_p1, intervals as internal_i1\n" +
                "WITH {path: internal_p1, intervals: internal_i1} as p\n" +
                "RETURN p.path, p.interval";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void pairCPathWithSumAndSizeTest() {
        final String query = "SELECT sum(size(p.intervals)) as tot\n" +
                "MATCH (n:Person),(n2:Person), p=pairCPath((n)-[:Friend*3]->(n2))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'}),(n2:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPathsNodes(n,n2,3,3,{edgesLabel:'Friend',direction:'outgoing'}) YIELD path as internal_p0, intervals as internal_i0\n" +
                "WITH {path: internal_p0, intervals: internal_i0} as p\n" +
                "RETURN sum(size(p.intervals)) as tot";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void pairCPathWithSumAndSizeTestWithWS() {
        final String query = "SELECT sum ( size ( p.intervals ) ) as tot\n" +
                "MATCH (n:Person),(n2:Person), p=pairCPath((n)-[:Friend*3]->(n2))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'}),(n2:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPathsNodes(n,n2,3,3,{edgesLabel:'Friend',direction:'outgoing'}) YIELD path as internal_p0, intervals as internal_i0\n" +
                "WITH {path: internal_p0, intervals: internal_i0} as p\n" +
                "RETURN sum ( size ( p.intervals ) ) as tot";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void pairCPathRangeTest() {
        final String query = "SELECT p.path, p.interval\n" +
                "MATCH (n:Person),(n2:Person), p=pairCPath((n)-[:Friend*2..4]->(n2))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'}),(n2:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPathsNodes(n,n2,2,4,{edgesLabel:'Friend',direction:'outgoing'}) YIELD path as internal_p1, intervals as internal_i1\n" +
                "WITH {path: internal_p1, intervals: internal_i1} as p\n" +
                "RETURN p.path, p.interval";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionTwoNodeTestNoVariableLength() {
        final String query = "SELECT p\n" +
                "\tMATCH (p1:Person), (p2:Person), p = cPath((p1) - [:Friend] -> (p2))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1:Object {title: 'Person'}),(p2:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(p1,p2,1,1,{edgesLabel:'Friend',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as p\n" +
                "RETURN p";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionTwoNodeTestRangeLength() {
        final String query = "SELECT p\n" +
                "\tMATCH (p1:Person), (p2:Person), p = cPath((p1) - [:Friend*1..10] -> (p2))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1:Object {title: 'Person'}),(p2:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(p1,p2,1,10,{edgesLabel:'Friend',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as p\n" +
                "RETURN p";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionTwoNodeTestFromRangeLengthMissing() {
        final String query = "SELECT p\n" +
                "\tMATCH (p1:Person), (p2:Person), p = cPath((p1) - [:Friend*..10] -> (p2))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1:Object {title: 'Person'}),(p2:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(p1,p2,1,10,{edgesLabel:'Friend',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as p\n" +
                "RETURN p";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionOneNodeTestAttr() {
        final String query = "SELECT p.interval\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i1\n" +
                "WITH {path: internal_p0, interval: internal_i1} as p\n" +
                "RETURN p.interval";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionThirdPath() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person))\n" +
                "SKIP 2\n" +
                "LIMIT 1\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as p\n" +
                "RETURN p\n" +
                "SKIP 2\n" +
                "LIMIT 1";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionOneNodeTestWildcard() {
        final String query = "SELECT *\n" +
                "\tMATCH (p:Person), result = cPath((p) - [:Friend*2] -> (:Person))\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(p,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as result, p\n" +
                "RETURN p, result";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionOneNodeTestIntervalYear() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person), '2015', '2019')\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',between:'2015—2019',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as p\n" +
                "RETURN p";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionOneNodeTestIntervalYearMonth() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person), '2015-02', '2019-01')\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',between:'2015-02—2019-01',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as p\n" +
                "RETURN p";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionOneNodeTestIntervalDate() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person), '2018-09-20', '2018-09-21')\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',between:'2018-09-20—2018-09-21',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as p\n" +
                "RETURN p";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void cPathFunctionOneNodeTestIntervalDateTime() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person), '2018-09-20 16:00', '2018-09-20 19:00')\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (n:Object {title: 'Person'})\n" +
                "CALL coexisting.coTemporalPaths(n,null,2,2,{edgesLabel:'Friend',nodesLabel:'Person',between:'2018-09-20 16:00—2018-09-20 19:00',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "WITH {path: internal_p0, interval: internal_i0} as p\n" +
                "RETURN p";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void MultipleFunctionCalls() {
        final String query = "SELECT p1.Name, p2.Name\n" +
                "\tMATCH (p1:Person), (p2:Person), (p3:Person), (p4:Person), path1 = cPath((p1) - [:Friend*1..8] -> (p2), '2018', '2019'), path2 = cPath((p3) - [:Friend*4..10] -> (p4))\n" +
                "WHERE p1.Name = 'Camila' and p2.Name = 'John' and p3.Name = 'Carlos' and p4.Name = 'Romeo'\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1:Object {title: 'Person'}),(p2:Object {title: 'Person'}),(p3:Object {title: 'Person'}),(p4:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (p2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "MATCH (p3)-->(internal_n2:Attribute {title: 'Name'})-->(internal_v2:Value)\n" +
                "MATCH (p4)-->(internal_n3:Attribute {title: 'Name'})-->(internal_v3:Value)\n" +
                "WHERE internal_v0.value = 'Camila' and internal_v1.value = 'John' and internal_v2.value = 'Carlos' and internal_v3.value = 'Romeo'\n" +
                "CALL coexisting.coTemporalPaths(p1,p2,1,8,{edgesLabel:'Friend',between:'2018—2019',direction:'outgoing'}) YIELD path as internal_p0, interval as internal_i0\n" +
                "CALL coexisting.coTemporalPaths(p3,p4,4,10,{edgesLabel:'Friend',direction:'outgoing'}) YIELD path as internal_p1, interval as internal_i1\n" +
                "WITH {path: internal_p0, interval: internal_i0} as path1, {path: internal_p1, interval: internal_i1} as path2, internal_v0, internal_v1\n" +
                "RETURN internal_v0 as `p1.Name`, internal_v1 as `p2.Name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test(expected = ParseCancellationException.class)
    public void functionCallWithMoreThanTwoNodes() {
        String query = "SELECT path\nMATCH (n), (m), path = earliestPath((j)-->(k)-->(l)) " +
                "WHERE n.name = 'Hola' and m.name = 'Jorge'";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void functionCallWithUnknownVariables() {
        String query = "SELECT path\nMATCH (n), (m), path = earliestPath((j)-[:Flight*]->(k))";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testEarliestPathThreshold() {
        String query = "SELECT path\nMATCH (n), (m), path = earliestPath((n)-[:Flight*]->(m), '2018-09-20 16:00') " +
                "where n.name = 'Hola' and m.name = 'Jorge'";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testFastestPathThreshold() {
        final String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = fastestPath((a1)-[:Flight*]->(a2), '2018')\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testShortestPathThreshold() {
        final String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = shortestPath((a1)-[:Flight*]->(a2), '2018-09-20')\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testRepeatedFunctionVariableDefinition1() {
        final String query = "SELECT p\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (p:City)<-[:LocatedAt]-(a2:Airport), p = fastestPath((a1)-[:Flight*]->(a2))\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testRepeatedFunctionVariableDefinition2() {
        final String query = "SELECT p\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), p = fastestPath((a1)-[:Flight*]->(a2)), " +
                "\np = shortestPath((a1)-[:Flight]->(a2))\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void cPathFunctionOneNodeTestInvalidYearRange() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person), '2018', '2018')\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void cPathFunctionOneNodeTestInvalidYearMonthRange() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person), '2018-05', '2018-05')\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void cPathFunctionOneNodeTestInvalidDateRange() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person), '2018-05-05', '2018-05-05')\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void cPathFunctionOneNodeTestInvalidDateTimeRange() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person), '2018-09-20 16:00', '2018-09-20 16:00')\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void cPathFunctionOneNodeTestInvalidGranularity() {
        final String query = "SELECT p\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*2] -> (:Person), '2018-09-20 16:00', '2018')\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void cPathInitialNode() {
        final String query = "SELECT p.interval\n" +
                "\tMATCH (n:Person), p = cPath((:Person) - [:Friend*2] -> (n))\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void cPathVariableDefinition() {
        final String query = "SELECT *\n" +
                "\tMATCH p = cPath((p1:Person) - [:Friend*2] -> (:Person))\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void cPathInvalidRange() {
        final String query = "SELECT *\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*8..4] -> (:Person))\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void cPathAsterisk() {
        final String query = "SELECT *\n" +
                "\tMATCH (n:Person), p = cPath((n) - [:Friend*] -> (:Person))\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void flightsLabelsDefinition() {
        final String query = "SELECT path\nMATCH (n), path = earliestPath((n)-[:Flight*]->(:Airport))";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void flightsVariableDefinition() {
        final String query = "SELECT path\nMATCH (n), path = fastestPath((n)-[:Flight*]->(a:Airport))";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void flightsLimitedVariableLength() {
        final String query = "SELECT path\nMATCH (n),(m), path = earliestPath((n)-[:Flight*2..6]->(m))";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void flightsWithoutVariableLength() {
        final String query = "SELECT path\nMATCH (n),(m), path = latestDeparturePath((n)-[:Flight]->(m))";
        new QueryCompiler().compile(query);
    }
}
