package ar.edu.itba.procedures;

public class PeopleDatasetTest extends ProcedureTest {

    private static final String FIXTURE_FILENAME = "fixture.cypher";

    public PeopleDatasetTest(Class procedureClass) {
        super(procedureClass);
    }

    @Override
    public String getFixture() {
        return FIXTURE_FILENAME;
    }
}
