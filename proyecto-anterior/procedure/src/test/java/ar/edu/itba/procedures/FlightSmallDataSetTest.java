package ar.edu.itba.procedures;

public class FlightSmallDataSetTest extends ProcedureTest {

    private static final String FIXTURE_FILENAME = "flights-small.cypher";

    public FlightSmallDataSetTest(Class procedureClass) {
        super(procedureClass);
    }

    @Override
    public String getFixture() {
        return FIXTURE_FILENAME;
    }
}
