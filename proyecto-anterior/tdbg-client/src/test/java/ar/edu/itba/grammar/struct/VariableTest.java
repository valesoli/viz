package ar.edu.itba.grammar.struct;

import org.junit.Test;

import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void initialize() {
        String aliasName = "name";
        Variable variable = new Variable(aliasName);
        assertEquals(aliasName, variable.getName());
    }

    @Test
    public void hasAttribute() {
        Variable a = new Variable("name");
        assertFalse(a.hasAttribute("1234"));
    }

    @Test
    public void addAttribute() {
        Variable a = new Variable("name");
        String attributeName = "attr";
        Attribute attribute = new Attribute(a, attributeName, "2", "3", true);
        a.addAttribute(attribute);

        assertTrue(a.hasAttribute(attributeName));
        assertEquals(attribute, a.getAttribute(attributeName));
        assertNull(a.getAttribute("attr2"));
    }
}
