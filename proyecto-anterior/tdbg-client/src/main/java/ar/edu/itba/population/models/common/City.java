package ar.edu.itba.population.models.common;

import ar.edu.itba.population.TimeInterval;
import ar.edu.itba.population.models.ObjectNode;

public class City extends ObjectNode {

    public static final String TITLE = "City";

    public City(int id, TimeInterval interval) {
        super(id, interval);
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
