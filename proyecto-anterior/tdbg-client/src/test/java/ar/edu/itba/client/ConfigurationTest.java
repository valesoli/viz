package ar.edu.itba.client;

import ar.edu.itba.population.PopulationModel;
import ar.edu.itba.population.models.airports.FlightModel;
import ar.edu.itba.population.models.socialnetwork.SocialNetwork;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ConfigurationTest {

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = Configuration.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfFileNotFound() {
        CommandLineOptions options = new CommandLineOptions("config.properties", true, PopulationModel.SOCIAL_NETWORK);
        Configuration.initialize(options);
    }

    @Test
    public void loadFromFile() {
        CommandLineOptions options = new CommandLineOptions("src/test/resources/test-config.properties", true, PopulationModel.SOCIAL_NETWORK);
        Configuration configuration = Configuration.initialize(options);
        assertEquals("db-user", configuration.getNeo4jUser());
        assertEquals("db-password", configuration.getNeo4jPassword());
        assertEquals("bolt://192.168.1.1:7777", configuration.getBoltUri());
        assertEquals(1234, configuration.getWebAppPort());
        assertTrue(configuration.getPopulation() instanceof SocialNetwork);
    }

    @Test
    public void loadFlights() {
        CommandLineOptions options = new CommandLineOptions("src/test/resources/test-config.properties", true, PopulationModel.FLIGHTS);
        Configuration configuration = Configuration.initialize(options);
        assertTrue(configuration.getPopulation() instanceof FlightModel);
    }

    @Test
    public void loadDefaults() {
        CommandLineOptions options = new CommandLineOptions(null, true, PopulationModel.SOCIAL_NETWORK);
        Configuration configuration = Configuration.initialize(options);
        assertEquals("neo4j", configuration.getNeo4jUser());
        assertEquals("admin", configuration.getNeo4jPassword());
        assertEquals("bolt://localhost:7687", configuration.getBoltUri());
        assertEquals(7000, configuration.getWebAppPort());
        assertTrue(configuration.getPopulation() instanceof SocialNetwork);
    }
}
