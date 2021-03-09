package ar.edu.itba.procedures;

import org.junit.jupiter.api.BeforeAll;
import org.neo4j.harness.TestServerBuilders;

public abstract class ProcedureTest extends EmbeddedNeo4jTest {

    private final Class procedureClass;

    public ProcedureTest(Class procedureClass) {
        this.procedureClass = procedureClass;
    }

    @BeforeAll
    void initializeNeo4j() {
        setEmbeddedDatabaseServer(
            buildServer(TestServerBuilders
                .newInProcessBuilder()
                .withProcedure(this.procedureClass))
        );
    }
}
