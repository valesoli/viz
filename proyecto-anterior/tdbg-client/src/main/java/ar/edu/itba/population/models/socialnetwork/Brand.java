package ar.edu.itba.population.models.socialnetwork;

import ar.edu.itba.population.TimeInterval;
import ar.edu.itba.population.models.ObjectNode;

public class Brand extends ObjectNode {

    public static final String TITLE = "Brand";

    public Brand(int id, TimeInterval interval) {
        super(id, interval);
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
