package ar.edu.itba.population.models;

import ar.edu.itba.population.models.cpath.CPathGenerationConfiguration;
import org.junit.Test;

import static org.junit.Assert.*;

public class CPathGenerationConfigurationTest {

    @Test(expected = IllegalArgumentException.class)
    public void invalidArguments() {
        new CPathGenerationConfiguration(-3, 4);
    }

    @Test
    public void nodesToGenerate() {
        CPathGenerationConfiguration config = new CPathGenerationConfiguration(2, 3);
        assertEquals(8, config.nodesToGenerate());
    }
}