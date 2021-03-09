package ar.edu.itba.population;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class DataFactoryTest {

    @Test
    public void shouldReturnFlightNumber() {
        DataFactory df = new DataFactory();
        String flightNumber = df.getFlightNumber();
        Matcher flightNumberMatcher = Pattern.compile("[A-Z]{2}\\d{4}").matcher(flightNumber);
        assertTrue(flightNumberMatcher.find());
    }

}
