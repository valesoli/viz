package ar.edu.itba.procedures;

public class FlightDatasetTest extends ProcedureTest {

    private static final String FIXTURE_FILENAME = "flights.cypher";

    public FlightDatasetTest(Class procedureClass) {
        super(procedureClass);
    }

    @Override
    public String getFixture() {
        return FIXTURE_FILENAME;
    }
}
