package ar.edu.itba.client;

import org.junit.Test;

import static ar.edu.itba.population.PopulationModel.FLIGHTS;
import static ar.edu.itba.population.PopulationModel.SOCIAL_NETWORK;
import static org.junit.Assert.*;

public class CliParserTest {

    @Test
    public void parseOptions() {
        String args = "--populate s";
        CommandLineOptions options = new CliParser().parseOptions(args.split(" "));
        assertTrue(options.isPopulateMode());
        assertEquals(SOCIAL_NETWORK, options.getPopulationModel());
    }

    @Test
    public void parseOptionsFlights() {
        String args = "--populate f";
        CommandLineOptions options = new CliParser().parseOptions(args.split(" "));
        assertTrue(options.isPopulateMode());
        assertEquals(FLIGHTS, options.getPopulationModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void requiredPopulationMode() {
        String args = "--populate";
        new CliParser().parseOptions(args.split(" "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidPopulationMode() {
        String args = "--populate invalid";
        new CliParser().parseOptions(args.split(" "));
    }


    @Test
    public void configurationFile() {
        String args = "-c /home/user/config.properties";
        CommandLineOptions options = new CliParser().parseOptions(args.split(" "));
        assertFalse(options.isPopulateMode());
        assertEquals("/home/user/config.properties", options.getConfigurationFile());
    }

    @Test(expected = IllegalArgumentException.class)
    public void configurationFile2() {
        String args = "-c ";
        new CliParser().parseOptions(args.split(" "));
    }
}