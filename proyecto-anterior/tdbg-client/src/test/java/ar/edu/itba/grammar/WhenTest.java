package ar.edu.itba.grammar;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WhenTest {

    @Test
    public void whenSimple() {
        final String query = "SELECT p1.Name\n" +
                "MATCH (p1:Person) - [:LivedIn] -> (:City)\n" +
                "WHEN\n" +
                "\tMATCH (p2:Person) - [:Fan] -> (:Brand)\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p2:Object {title: 'Person'}) - [internal_f0:Fan] -> (internal_b0:Object {title: 'Brand'})\n" +
                "WITH DISTINCT internal_f0.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_l0:LivedIn] -> (internal_c0:Object {title: \\'City\\'})\n" +
                "OPTIONAL MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1.Name` as `p1.Name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenSimpleSkipAndLimit() {
        final String query = "SELECT p1.Name\n" +
                "MATCH (p1:Person) - [:LivedIn] -> (:City)\n" +
                "WHEN\n" +
                "\tMATCH (p2:Person) - [:Fan] -> (:Brand)\n" +
                "SKIP 1\n" +
                "LIMIT 1\n";
        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p2:Object {title: 'Person'}) - [internal_f0:Fan] -> (internal_b0:Object {title: 'Brand'})\n" +
                "WITH DISTINCT internal_f0.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_l0:LivedIn] -> (internal_c0:Object {title: \\'City\\'})\n" +
                "OPTIONAL MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1.Name` as `p1.Name`\n" +
                "SKIP 1\n" +
                "LIMIT 1";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenSimpleWithoutEdgeVariable() {
        final String query = "SELECT p2\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHEN \n" +
                "MATCH (p1) - [:LivedIn] -> (c:City)\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1) - [internal_l0:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend] -> (p2:Object {title: 'Person'})\n" +
                "WITH DISTINCT internal_l0.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (p2:Object {title: \\'Person\\'})\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p2` as `p2`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithWhereInMainQuery() {
        final String query = "SELECT p2\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City)\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1) - [e:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Mary'\n" +
                "WITH DISTINCT e.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Mary\\'\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p2` as `p2`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithWhereInMainQueryAndSelectWildcard() {
        final String query = "SELECT *\n" +
                "MATCH (p1:Person) - [:Friend] -> (:Person)\n" +
                "WHERE p1.Name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City)\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1) - [e:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend] -> (internal_p0:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Mary'\n" +
                "WITH DISTINCT e.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (internal_p0:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Mary\\'\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_f0\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_p0\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`internal_f0` as `internal_f0`, row.`internal_p0` as `internal_p0`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithWhereInMainQueryAndSelectWildcardWithoutEdgeVariable() {
        final String query = "SELECT *\n" +
                "MATCH (p1:Person) - [:Friend] -> (:Person)\n" +
                "WHERE p1.Name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p1) - [:LivedIn] -> (c:City)\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1) - [internal_l0:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend] -> (internal_p0:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Mary'\n" +
                "WITH DISTINCT internal_l0.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (internal_p0:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Mary\\'\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_f0\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_p0\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`internal_f0` as `internal_f0`, row.`internal_p0` as `internal_p0`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithWhereDoubleAttrVarInMainQuery() {
        final String query = "SELECT p2\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Mary' AND p1.Age = 20\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City)\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1) - [e:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_a0:Attribute {title: 'Age'})-->(internal_v1:Value)\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'Mary' AND internal_v1.value = 20\n" +
                "WITH DISTINCT e.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_a0:Attribute {title: \\'Age\\'})-->(internal_v1:Value)\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Mary\\' AND internal_v1.value = 20\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p2` as `p2`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithWhereAliasInMainQuery() {
        final String query = "SELECT p1.Name as name, p2.Name\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City)\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1) - [e:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(name:Value)\n" +
                "WHERE name.value = 'Mary'\n" +
                "WITH DISTINCT e.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(name:Value)\n" +
                "WHERE name.value = \\'Mary\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p2.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`name` as `name`, row.`p2.Name` as `p2.Name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithWhere() {
        final String query = "SELECT p2\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City)\n" +
                "WHERE c.Name = 'Kansas'\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1) - [e:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (c)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend] -> (p2:Object {title: 'Person'})\n" +
                "WHERE internal_v0.value = 'Kansas'\n" +
                "WITH DISTINCT e.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (p2:Object {title: \\'Person\\'})\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p2` as `p2`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithWhereInBothQueries() {
        final String query = "SELECT p2\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City)\n" +
                "WHERE c.Name = 'Brussels'\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1) - [e:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (c)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v1.value = 'Brussels' AND internal_v0.value = 'Mary'\n" +
                "WITH DISTINCT e.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Mary\\'\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p2` as `p2`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithSelectAliases() {
        final String query = "SELECT p2.Name as friend_name\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City)\n" +
                "WHERE c.Name = 'Brussels'\n";

        String cypherQuery = new QueryCompiler().compile(query);
        final String expectedOutput = "MATCH (p1) - [e:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (c)-->(internal_n2:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v1.value = 'Brussels' AND internal_v0.value = 'Mary'\n" +
                "WITH DISTINCT e.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Mary\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(friend_name:Value)\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"friend_name\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`friend_name` as `friend_name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithOuterObjectAttributeReferenceInWhere() {
        final String query = "SELECT c.Name as city_name, b1.Name as brand_name\n" +
                "MATCH (p1:Person) - [:LivedIn] -> (c:City), (p1) - [:Fan] -> (b1:Brand)\n" +
                "WHERE p1.Name = 'Cathy Van Bourne'\n" +
                "WHEN \n" +
                "MATCH (p2:Person) - [e:Fan] -> (b2:Brand)\n" +
                "WHERE p2.Name = 'Sandra Carter' and b1.Name = b2.Name\n";

        String cypherQuery = new QueryCompiler().compile(query);

        String expectedOutput = "MATCH (p2:Object {title: 'Person'}) - [e:Fan] -> (b2:Object {title: 'Brand'})\n" +
                "MATCH (p2)-->(internal_n3:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "MATCH (b2)-->(internal_n4:Attribute {title: 'Name'})-->(internal_v2:Value)\n" +
                "MATCH (p1) - [internal_f0:Fan] -> (b1:Object {title: 'Brand'})\n" +
                "MATCH (b1)-->(internal_n1:Attribute {title: 'Name'})-->(brand_name:Value)\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_l0:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (p1)-->(internal_n2:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v1.value = 'Sandra Carter' and brand_name.value = internal_v2.value AND internal_v0.value = 'Cathy Van Bourne'\n" +
                "WITH DISTINCT e.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_l0:LivedIn] -> (c:Object {title: \\'City\\'}),(p1) - [internal_f0:Fan] -> (b1:Object {title: \\'Brand\\'})\n" +
                "MATCH (p1)-->(internal_n2:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy Van Bourne\\'\n" +
                "OPTIONAL MATCH (c)-->(internal_n0:Attribute {title: \\'Name\\'})-->(city_name:Value)\n" +
                "OPTIONAL MATCH (b1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(brand_name:Value)\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"city_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"brand_name\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`city_name` as `city_name`, row.`brand_name` as `brand_name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithOuterObjectAttributeReferenceInWhereForced() {
        final String query = "SELECT c.Name as city_name, b1.Name as brand_name\n" +
                "MATCH (p1:Person) - [:LivedIn] -> (c:City),\n" +
                "(p1) - [:Fan] -> (b1:Brand)\n" +
                "WHERE p1.Name = 'Cathy Van Bourne' AND b1.Name='LG'\n" +
                "WHEN\n" +
                "  MATCH (p2:Person) - [f:Fan] -> (b2:Brand)\n" +
                "  WHERE p2.Name = 'Sandra Carter' and b2.Name ='LG'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        String expectedOutput = "MATCH (p2:Object {title: 'Person'}) - [f:Fan] -> (b2:Object {title: 'Brand'})\n" +
                "MATCH (p2)-->(internal_n3:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "MATCH (b2)-->(internal_n4:Attribute {title: 'Name'})-->(internal_v2:Value)\n" +
                "WHERE internal_v1.value = 'Sandra Carter' and internal_v2.value ='LG'\n" +
                "WITH DISTINCT f.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_l0:LivedIn] -> (c:Object {title: \\'City\\'}),(p1) - [internal_f0:Fan] -> (b1:Object {title: \\'Brand\\'})\n" +
                "MATCH (p1)-->(internal_n2:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "MATCH (b1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(brand_name:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy Van Bourne\\' AND brand_name.value=\\'LG\\'\n" +
                "OPTIONAL MATCH (c)-->(internal_n0:Attribute {title: \\'Name\\'})-->(city_name:Value)\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"city_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"brand_name\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`city_name` as `city_name`, row.`brand_name` as `brand_name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithEdgeAttributeReferenceInWhere() {
        final String query = "SELECT p.Name\n" +
                "MATCH (p:Person) - [:Friend] -> (:Person)\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City)\n" +
                "WHERE c.Name = 'Kansas' and e.Property = 100\n";

        String cypherQuery = new QueryCompiler().compile(query);

        String expectedOutput = "MATCH (p1) - [e:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (c)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v1:Value)\n" +
                "WHERE internal_v1.value = 'Kansas' and e.Property = 100\n" +
                "WITH DISTINCT e.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (internal_p0:Object {title: \\'Person\\'})\n" +
                "OPTIONAL MATCH (p)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p.Name` as `p.Name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithReferenceAliasInWhen() {
        final String query = "SELECT p1.Name as person_name\n" +
                "MATCH (p1:Person) - [:LivedIn] -> (c:City)\n" +
                "WHERE person_name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p1) - [:Friend] -> (p2:Person)\n" +
                "WHERE p2.Name = person_name\n";

        String cypherQuery = new QueryCompiler().compile(query);

        String expectedOutput = "MATCH (p1) - [internal_f0:Friend] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p2)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_l0:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(person_name:Value)\n" +
                "WHERE internal_v0.value = person_name.value AND person_name.value = 'Mary'\n" +
                "WITH DISTINCT internal_f0.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_l0:LivedIn] -> (c:Object {title: \\'City\\'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(person_name:Value)\n" +
                "WHERE person_name.value = \\'Mary\\'\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"person_name\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`person_name` as `person_name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void whenWithAliasInWhen() {
        final String query = "SELECT p2.Name as person_name\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE person_name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p1) - [:LivedIn] -> (c:City)\n" +
                "WHERE c.Name = person_name\n";

        String cypherQuery = new QueryCompiler().compile(query);

        String expectedOutput = "MATCH (p1) - [internal_l0:LivedIn] -> (c:Object {title: 'City'})\n" +
                "MATCH (c)-->(internal_n1:Attribute {title: 'Name'})-->(internal_v0:Value)\n" +
                "MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p2)-->(internal_n0:Attribute {title: 'Name'})-->(person_name:Value)\n" +
                "WHERE internal_v0.value = person_name.value AND person_name.value = 'Mary'\n" +
                "WITH DISTINCT internal_l0.interval as intervals\n" +
                "UNWIND intervals as interval\n" +
                "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(person_name:Value)\n" +
                "WHERE person_name.value = \\'Mary\\'\n" +
                "RETURN *',\n" +
                "interval,\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"person_name\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`person_name` as `person_name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test(expected = ParseCancellationException.class)
    public void testInvalidWhen() {
        String query = "SELECT p2.Name as friend_name\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City)\n" +
                "WHERE c.Name = 'Brussels' AND p3.Name = 'fail'\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testInvalidWhenOuterSelect() {
        String query = "SELECT p2.Name as friend_name\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Mary' AND c.Name = 'fail'\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City)\n" +
                "WHERE c.Name = 'Brussels'\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testInvalidWhenPath1() {
        String query = "SELECT p2.Name as friend_name\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p1) - [e:LivedIn] -> (c:City), (p1) - [:Fan] -> (p3:Person)\n" +
                "WHERE c.Name = 'Brussels'\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testInvalidWhenPath2() {
        String query = "SELECT p2.Name as friend_name\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p3:Person) - [:Fan] -> (p1) - [e:LivedIn] -> (c:City)\n" +
                "WHERE c.Name = 'Brussels'\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testInvalidWhenPath3() {
        String query = "SELECT p2.Name as friend_name\n" +
                "MATCH (p1:Person) - [:Friend] -> (p2:Person)\n" +
                "WHERE p1.Name = 'Mary'\n" +
                "WHEN \n" +
                "MATCH (p3:Person)\n" +
                "WHERE p3.Name = 'Brian'\n";
        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void unsupportedFunctionCallAndWhen() {
        String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = latestPath((a1)-[:Flight]->(a2), date, 20-09-2018 16:00)\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n" +
                "WHEN\n" +
                "MATCH (c1)<-[:LocatedAt]-(a3:Airport)\n" +
                "WHERE a1.Name = 'Kennedy'\n";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void whenWithOuterEdgeAttributeReferenceInWhere() {
        final String query = "SELECT p.Name\n" +
                "MATCH (p:Person) - [e1:Friend] -> (:Person)\n" +
                "WHEN \n" +
                "MATCH (p1) - [e2:LivedIn] -> (c:City)\n" +
                "WHERE c.Name = 'Kansas' and e1.Property = e2.Property\n";

        new QueryCompiler().compile(query);
    }
}
