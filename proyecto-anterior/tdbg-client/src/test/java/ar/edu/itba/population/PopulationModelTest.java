package ar.edu.itba.population;

import org.junit.Test;

import static org.junit.Assert.*;

public class PopulationModelTest {

    @Test
    public void fromString() {
        assertEquals(PopulationModel.SOCIAL_NETWORK, PopulationModel.fromString("S"));
        assertEquals(PopulationModel.FLIGHTS, PopulationModel.fromString("F"));
    }
}