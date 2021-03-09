package ar.edu.itba.grammar;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NonTemporalTest {

    @Test
    public void testSelectAllNodes() {
        String query = "SELECT n\n" +
                "MATCH (n)";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (n)\n" +
                "RETURN n", cypherQuery);
    }

    @Test
    public void testSelectAllNodesWithWhere() {
        String query = "SELECT n\n" +
                "MATCH (n)\n" +
                "WHERE n.Name = 'John'\n";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (n)\n" +
                "MATCH (n)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'John'\n" +
                "RETURN n", cypherQuery);
    }

    @Test
    public void testNonTemporalWithMatch() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void testSelectCase() {
        String query = "select p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void simpleSelectWildcard() {
        String query = "SELECT *\n" +
                "MATCH (n)";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (n)\n" +
                "RETURN n", cypherQuery);
    }

    @Test
    public void simpleSelectWildcardPaths() {
        String query = "SELECT *\n" +
                "MATCH (p1) --> (p2:Person), (p1:Person)\n" +
                "WHERE p1.Name = 'John'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1) --> (p2:Object {title: 'Person'}),(p1:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'John'\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void selectWildcard() {
        String query = "SELECT *\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "RETURN p1, internal_f0, p2", cypherQuery);
    }

    @Test
    public void selectEdge() {
        String query = "SELECT e, e.interval as interval\n" +
                "MATCH (p1:Person) - [e:Friend*2] -> (p2:Person)\n";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [e:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "RETURN e, e.interval as `interval`", cypherQuery);
    }

    @Test
    public void selectEdgeAndEdgeWhere() {
        String query = "SELECT e\n" +
                "MATCH (p1:Person) - [e:Friend*2] -> (p2:Person)\n" +
                "WHERE e.Property = 'Test'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [e:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "WHERE e.Property = 'Test'\n" +
                "RETURN e", cypherQuery);
    }


    @Test
    public void selectImplicitWildcard() {
        String query = "SELECT *\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (:Person)";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (internal_p0:Object {title: 'Person'})\n" +
                "RETURN p1, internal_f0, internal_p0", cypherQuery);
    }

    @Test
    public void selectWildcardWithWhere() {
        String query = "SELECT *\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Matias'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Matias'\n" +
                "RETURN p1, internal_f0, p2", cypherQuery);
    }

    @Test
    public void selectImplicitWildcardWithWhere() {
        String query = "SELECT *\n" +
                "MATCH (p:Person) - [:Friend*2] -> (:Person) <- [:Friend] - (:Person)\n" +
                "WHERE p.Name = 'Matias'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (internal_p0:Object {title: 'Person'}) <- [internal_f1:Friend] - (internal_p1:Object {title: 'Person'})\n" +
                "MATCH (p)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Matias'\n" +
                "RETURN p, internal_f0, internal_p0, internal_f1, internal_p1", cypherQuery);
    }

    @Test
    public void selectWithMatchAndWhere() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Cathy'\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void selectWithMatchAndIntPropertyInWhere() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1[id] = 110";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "WHERE p1.id = 110\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void selectWithMatchAndStringPropertyInWhere() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1[title] = 'testing'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "WHERE p1.title = 'testing'\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void selectWithMatchAndPropertyInSelect() {
        String query = "SELECT p1[id] as fake_id\n" +
                "MATCH (p1:Person)\n";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'})\n" +
                "RETURN p1.id as fake_id", cypherQuery);
    }

    @Test
    public void selectWithMatchRangeAndWhere() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*1..2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*1..2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Cathy'\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void selectWithMatchRangeWithoutFirstNumberAndWhere() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*..2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*..2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Cathy'\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void selectWithMatchAndWhereVarCompare() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = p2.Name";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (p2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v0.value = internal_v1.value\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void testNonTemporalWithAliases() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Cathy'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: 'Name'})-->(friend_name:Value)\n" +
                "RETURN p1, friend_name, p2, internal_v0 as `p1.Name`", cypherQuery);
    }

    @Test
    public void testNonTemporalWithAliasesAvoidExpand() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy' AND p2.Name = 'John'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (p2)-->(internal_n0:Attribute {title: 'Name'})-->(friend_name:Value)\n" +
                "WHERE internal_v0.value = 'Cathy' AND friend_name.value = 'John'\n" +
                "RETURN p1, friend_name, p2, internal_v0 as `p1.Name`", cypherQuery);
    }

    @Test
    public void testNonTemporalWithAliasesInWhere() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy' and friend_name = 'Mary'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (p2)-->(internal_n0:Attribute {title: 'Name'})-->(friend_name:Value)\n" +
                "WHERE internal_v0.value = 'Cathy' and friend_name.value = 'Mary'\n" +
                "RETURN p1, friend_name, p2, internal_v0 as `p1.Name`", cypherQuery);
    }

    @Test
    public void testLimitSelectAllNodes() {
        String query = "SELECT n\n" +
                "MATCH (n)\n" +
                "LIMIT 5";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (n)\n" +
                "RETURN n\n" +
                "LIMIT 5", cypherQuery);
    }

    @Test
    public void testSkipSelectAllNodes() {
        String query = "SELECT n\n" +
                "MATCH (n)\n" +
                "SKIP 5";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (n)\n" +
                "RETURN n\n" +
                "SKIP 5", cypherQuery);
    }

    @Test
    public void testSkipAndLimitSelectAllNodes() {
        String query = "SELECT n\n" +
                "MATCH (n)\n" +
                "SKIP 5\n" +
                "LIMIT 2";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (n)\n" +
                "RETURN n\n" +
                "SKIP 5\n" +
                "LIMIT 2", cypherQuery);
    }

    @Test
    public void testIdCallInWhere() {
        String query = "SELECT n.Name\n" +
                "MATCH (n:Person)\n" +
                "WHERE id(n) = 10";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (n:Object {title: 'Person'})\n" +
                "WHERE id(n) = 10\n" +
                "OPTIONAL MATCH (n)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "RETURN internal_v0 as `n.Name`", cypherQuery);
    }

    @Test
    public void testIdCallInSelect() {
        String query = "SELECT id(n) as true_id\n" +
                "MATCH (n:Person)\n";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (n:Object {title: 'Person'})\n" +
                "RETURN id(n) as true_id", cypherQuery);
    }

    @Test(expected = ParseCancellationException.class)
    public void testInvalidSelectNode() {
        String query = "SELECT p1, p3\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy'";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testInvalidSelectAttr() {
        String query = "SELECT p1, p3.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy'";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testInvalidWhereAttr() {
        String query = "SELECT p1\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p3.Name = 'Cathy'";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testRepeatedAlias() {
        String query = "SELECT p1.Name as name, p2.Name as name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy'";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testMissingAlias() {
        String query = "SELECT p1.Name as name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE person_name = 'Cathy'";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testRepeatedNodeSelection() {
        String query = "SELECT p1, p1\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy'";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testRepeatedAttrSelection() {
        String query = "SELECT p1.Name, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Cathy'";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testRepeatedAliasInMatch() {
        String query = "SELECT p1.Name as name\n" +
                "MATCH (name:Person)\n" +
                "WHERE name = 'Cathy'";
        new QueryCompiler().compile(query);
    }
}
