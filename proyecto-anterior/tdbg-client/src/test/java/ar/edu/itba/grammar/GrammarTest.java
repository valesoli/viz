package ar.edu.itba.grammar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GrammarTest {

    @Test
    public void testComments() {
        String query = "/*rñkgñ */\r\n" +
                "/*\n" +
                "gtt\n" +
                "gtgttgt\n" +
                "*/SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "/*p1.name = 'chris'   ddfrg huhuh \n" +
                "ed,le\n */" +
                "WHERE\n" +
                "p1.name = 'raul'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'name'})-->(internal_v0:Value)\n" +
                "WHERE internal_v0.value = 'raul'\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void testArithmetic() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE (p1.Salary=1000^2 +   (1.1266E-05+ .98) -1E4 - ( 5 * 1E018 / -1E4 ) + p1.Age) and p1.Age <= 50 and p2.Age <> 30 or p2.Age > 10 and p1.Threshold = - p2.Threshold\n";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_s0:Attribute {title: 'Salary'})-->(internal_v0:Value)\n" +
                "MATCH (p1)-->(internal_a0:Attribute {title: 'Age'})-->(internal_v1:Value)\n" +
                "MATCH (p1)-->(internal_t0:Attribute {title: 'Threshold'})-->(internal_v3:Value)\n" +
                "MATCH (p2)-->(internal_a1:Attribute {title: 'Age'})-->(internal_v2:Value)\n" +
                "MATCH (p2)-->(internal_t1:Attribute {title: 'Threshold'})-->(internal_v4:Value)\n" +
                "WHERE (internal_v0.value=1000^2 +   (1.1266E-05+ .98) -1E4 - ( 5 * 1E018 / -1E4 ) + internal_v1.value) and internal_v1.value <= 50 and internal_v2.value <> 30 or internal_v2.value > 10 and internal_v3.value = - internal_v4.value\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void testArithmeticAliases() {
        String query = "SELECT p1.Salary as salary1, p1.age as age1\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE (salary1=1000 +   (1.1266E-05+ .98) -1E4 - ( 5 * 1E018 / -1E4 ) + age1) and age1 <= 50 and p2.Age <> 30 or p2.Age > 10 and p1.Threshold = - p2.Threshold\n";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_s0:Attribute {title: 'Salary'})-->(salary1:Value)\n" +
                "MATCH (p1)-->(internal_a0:Attribute {title: 'age'})-->(age1:Value)\n" +
                "MATCH (p1)-->(internal_t0:Attribute {title: 'Threshold'})-->(internal_v1:Value)\n" +
                "MATCH (p2)-->(internal_a1:Attribute {title: 'Age'})-->(internal_v0:Value)\n" +
                "MATCH (p2)-->(internal_t1:Attribute {title: 'Threshold'})-->(internal_v2:Value)\n" +
                "WHERE (salary1.value=1000 +   (1.1266E-05+ .98) -1E4 - ( 5 * 1E018 / -1E4 ) + age1.value) and age1.value <= 50 and internal_v0.value <> 30 or internal_v0.value > 10 and internal_v1.value = - internal_v2.value\n" +
                "RETURN salary1, age1", cypherQuery);
    }

    @Test
    public void testArithmeticAttr() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person), (p3:Person)\n" +
                "WHERE p1.Age = p2.Age + p3.Age";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'}),(p3:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_a0:Attribute {title: 'Age'})-->(internal_v0:Value)\n" +
                "MATCH (p2)-->(internal_a1:Attribute {title: 'Age'})-->(internal_v1:Value)\n" +
                "MATCH (p3)-->(internal_a2:Attribute {title: 'Age'})-->(internal_v2:Value)\n" +
                "WHERE internal_v0.value = internal_v1.value + internal_v2.value\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void testArithmeticParenthesisCond() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE ( p1.Age > 10 and p2.Age > p1.Age ) OR (p1.Age < 10 and p2.Age < p1.Age)";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_a0:Attribute {title: 'Age'})-->(internal_v0:Value)\n" +
                "MATCH (p2)-->(internal_a1:Attribute {title: 'Age'})-->(internal_v1:Value)\n" +
                "WHERE ( internal_v0.value > 10 and internal_v1.value > internal_v0.value ) OR (internal_v0.value < 10 and internal_v1.value < internal_v0.value)\n" +
                "RETURN p1, p2", cypherQuery);
    }

    @Test
    public void testWhereValues() {
        String query = "SELECT p1, p2\n" +
                "MATCH (p1:Person) - [:Friend*2] -> (p2:Person)\n" +
                "WHERE p1.Birthdate = '1995' and p1.Salaray = p2.Salaray + 100 and p1.Name = 'John'\n" +
                "and p2.Birthdate = '1995-05' or p2.Birthdate = '1995-05-05' or p2.Birthdate = '1995-05-05 16:00'\n" +
                "or p1.Salary = 1000 or p1.Borthdate = '5/5/2010'";

        String cypherQuery = new QueryCompiler().compile(query);
        assertEquals("MATCH (p1:Object {title: 'Person'}) - [internal_f0:Friend*2] -> (p2:Object {title: 'Person'})\n" +
                "MATCH (p1)-->(internal_s2:Attribute {title: 'Salary'})-->(internal_v5:Value)\n" +
                "MATCH (p1)-->(internal_b0:Attribute {title: 'Birthdate'})-->(internal_v0:Value)\n" +
                "MATCH (p1)-->(internal_s0:Attribute {title: 'Salaray'})-->(internal_v1:Value)\n" +
                "MATCH (p1)-->(internal_n0:Attribute {title: 'Name'})-->(internal_v3:Value)\n" +
                "MATCH (p1)-->(internal_b2:Attribute {title: 'Borthdate'})-->(internal_v6:Value)\n" +
                "MATCH (p2)-->(internal_s1:Attribute {title: 'Salaray'})-->(internal_v2:Value)\n" +
                "MATCH (p2)-->(internal_b1:Attribute {title: 'Birthdate'})-->(internal_v4:Value)\n" +
                "WHERE internal_v0.value = '1995' and internal_v1.value = internal_v2.value + 100 and internal_v3.value = 'John'\n" +
                "and internal_v4.value = '1995-05' or internal_v4.value = '1995-05-05' or internal_v4.value = '1995-05-05 16:00'\n" +
                "or internal_v5.value = 1000 or internal_v6.value = '5/5/2010'\n" +
                "RETURN p1, p2", cypherQuery);
    }
}
