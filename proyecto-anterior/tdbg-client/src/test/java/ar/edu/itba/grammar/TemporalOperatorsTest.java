package ar.edu.itba.grammar;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TemporalOperatorsTest {

    @Test
    public void testSnapshotYear() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tSNAPSHOT '2018'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.snapshot(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(friend_name:Value)\n" +
                "RETURN *',\n" +
                "'2018',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"friend_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`friend_name` as `friend_name`, row.`p2` as `p2`, row.`p1.Name` as `p1.Name`";

        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testSnapshotYearMonth() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tSNAPSHOT '1995-06'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.snapshot(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(friend_name:Value)\n" +
                "RETURN *',\n" +
                "'1995-06',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"friend_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`friend_name` as `friend_name`, row.`p2` as `p2`, row.`p1.Name` as `p1.Name`";

        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testSnapshotDate() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tSNAPSHOT '2016-10-12'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.snapshot(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(friend_name:Value)\n" +
                "RETURN *',\n" +
                "'2016-10-12',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"friend_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`friend_name` as `friend_name`, row.`p2` as `p2`, row.`p1.Name` as `p1.Name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testSnapshotDateTime() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tSNAPSHOT '2016-10-12 16:00'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.snapshot(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(friend_name:Value)\n" +
                "RETURN *',\n" +
                "'2016-10-12 16:00',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"friend_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`friend_name` as `friend_name`, row.`p2` as `p2`, row.`p1.Name` as `p1.Name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testSnapshotWildcard() {
        String query = "SELECT *\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tSNAPSHOT '2018'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.snapshot(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (internal_p0:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "RETURN *',\n" +
                "'2018',\n" +
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
    public void testBetweenYear() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tBETWEEN '2016' and '2018'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(friend_name:Value)\n" +
                "RETURN *',\n" +
                "'2016—2018',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"friend_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`friend_name` as `friend_name`, row.`p2` as `p2`, row.`p1.Name` as `p1.Name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testBetweenYearSkipAndLimit() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tBETWEEN '2016' and '2018'\n" +
                "SKIP 2\n" +
                "LIMIT 5";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(friend_name:Value)\n" +
                "RETURN *',\n" +
                "'2016—2018',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"friend_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`friend_name` as `friend_name`, row.`p2` as `p2`, row.`p1.Name` as `p1.Name`\n" +
                "SKIP 2\n" +
                "LIMIT 5";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testBetweenYearMonth() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tBETWEEN '2016-04' and '2018-02'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(friend_name:Value)\n" +
                "RETURN *',\n" +
                "'2016-04—2018-02',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"friend_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`friend_name` as `friend_name`, row.`p2` as `p2`, row.`p1.Name` as `p1.Name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testBetweenDate() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tBETWEEN '2015-08-10' and '2018-02-10'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(friend_name:Value)\n" +
                "RETURN *',\n" +
                "'2015-08-10—2018-02-10',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"friend_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`friend_name` as `friend_name`, row.`p2` as `p2`, row.`p1.Name` as `p1.Name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testBetweenDateTime() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tBETWEEN '2018-02-10 16:00' and '2018-02-10 17:00'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n1:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "OPTIONAL MATCH (p2)-->(internal_n0:Attribute {title: \\'Name\\'})-->(friend_name:Value)\n" +
                "RETURN *',\n" +
                "'2018-02-10 16:00—2018-02-10 17:00',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"friend_name\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"internal_v0\",\n" +
                "      alias: \"p1.Name\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`friend_name` as `friend_name`, row.`p2` as `p2`, row.`p1.Name` as `p1.Name`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testBetweenWildcard() {
        String query = "SELECT *\n" +
                "MATCH (p:Person) - [:Friend*2] -> (:Person)\n" +
                "\tWHERE p.Name = 'Cathy'\n" +
                "\tBETWEEN '2016' and '2018'\n";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.between(\n" +
                "'MATCH (p:Object {title: \\'Person\\'}) - [internal_f0:Friend*2] -> (internal_p0:Object {title: \\'Person\\'})\n" +
                "MATCH (p)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Cathy\\'\n" +
                "RETURN *',\n" +
                "'2016—2018',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p\",\n" +
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
                "}) YIELD row RETURN row.`p` as `p`, row.`internal_f0` as `internal_f0`, row.`internal_p0` as `internal_p0`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testTemporalEdgeSelect() {
        String query = "SELECT p1, f, p2\n" +
                "MATCH (p1:Person) - [f:Friend*]  -> (p2:Person)\n" +
                "WHERE p1.Name = 'Pauline Boutler'\n" +
                "BETWEEN '2010' and '2017'";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [f:Friend*]  -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Pauline Boutler\\'\n" +
                "RETURN *',\n" +
                "'2010—2017',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"f\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`f` as `f`, row.`p2` as `p2`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testTemporalEdgeFieldSelect() {
        String query = "SELECT p1, f.interval, p2\n" +
                "MATCH (p1:Person) - [f:Friend*]  -> (p2:Person)\n" +
                "WHERE p1.Name = 'Pauline Boutler'\n" +
                "BETWEEN '2010' and '2017'";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [f:Friend*]  -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v1:Value)\n" +
                "WHERE internal_v1.value = \\'Pauline Boutler\\'\n" +
                "RETURN *',\n" +
                "'2010—2017',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"f.interval\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`f.interval` as `f.interval`, row.`p2` as `p2`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test
    public void testTemporalEdgeFieldSelectWithAlias() {
        String query = "SELECT p1, f.interval as interval, p2\n" +
                "MATCH (p1:Person) - [f:Friend*]  -> (p2:Person)\n" +
                "WHERE p1.Name = 'Pauline Boutler'\n" +
                "BETWEEN '2010' and '2017'";

        String cypherQuery = new QueryCompiler().compile(query);

        final String expectedOutput = "CALL temporal.between(\n" +
                "'MATCH (p1:Object {title: \\'Person\\'}) - [f:Friend*]  -> (p2:Object {title: \\'Person\\'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: \\'Name\\'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = \\'Pauline Boutler\\'\n" +
                "RETURN *',\n" +
                "'2010—2017',\n" +
                "{\n" +
                "  ret: [\n" +
                "    {\n" +
                "      name: \"p1\",\n" +
                "      alias: \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"f.interval\",\n" +
                "      alias: \"interval\"\n" +
                "    },\n" +
                "    {\n" +
                "      name: \"p2\",\n" +
                "      alias: \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}) YIELD row RETURN row.`p1` as `p1`, row.`interval` as `interval`, row.`p2` as `p2`";
        assertEquals(expectedOutput, cypherQuery);
    }

    @Test(expected = ParseCancellationException.class)
    public void testSnapshotInvalidDate() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tSNAPSHOT '2016-02-30'\n";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testBetweenYearInvalidRange() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tBETWEEN '2016' and '2016'\n";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testBetweenYearMonthInvalidRange() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tBETWEEN '2016-10' and '2016-10'\n";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testBetweenDateInvalidRange() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tBETWEEN '2018-02-10' and '2018-02-10'\n";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testBetweenDateTimeInvalidRange() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tBETWEEN '2018-02-10 16:00' and '2018-02-10 16:00'\n";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void testBetweenGranularityInvalidRange() {
        String query = "SELECT p1, p2.Name as friend_name, p2, p1.Name\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "\tWHERE p1.Name = 'Cathy'\n" +
                "\tBETWEEN '2018-02-10' and '2018'\n";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void unsupportedFunctionCallAndBetween() {
        String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = latestArrivalPath((a1)-[:Flight]->(a2), '2018-09-20 16:00')\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n" +
                "BETWEEN '2015-02-10' and '2019-08-10'";

        new QueryCompiler().compile(query);
    }

    @Test(expected = ParseCancellationException.class)
    public void unsupportedFunctionCallAndSnapshot() {
        String query = "SELECT path\n" +
                "MATCH (c1:City)<-[:LocatedAt]-(a1:Airport), (c2:City)<-[:LocatedAt]-(a2:Airport), path = latestDeparturePath((a1)-[:Flight]->(a2), '2018-09-20 16:00')\n" +
                "WHERE c1.Name = 'Hiramport' AND c2.Name='Port Farahhaven'\n" +
                "SNAPshot '2018'";

        new QueryCompiler().compile(query);
    }
}
