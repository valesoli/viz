package ar.edu.itba.procedures;

import org.neo4j.driver.v1.*;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class EmbeddedNeo4jTest {

    private final Config driverConfig = Config.build().withoutEncryption().toConfig();

    private File fixture;

    private ServerControls embeddedDatabaseServer;

    public EmbeddedNeo4jTest() {

    }

    protected List<String> statements(){

        List<String> statements = new ArrayList<>();

        try {
            Scanner sc = new Scanner(this.fixture).useDelimiter(";");

            while (sc.hasNext()){
                statements.add(sc.next());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Remove last empty statement
        statements.remove(statements.size() - 1);

        return statements;
    }

    public abstract String getFixture();

    public void runQuery(String query, AssertResult assertResult) {
        try (Driver driver = GraphDatabase.driver(
                this.embeddedDatabaseServer.boltURI(), driverConfig);
             Session session = driver.session()) {
            StatementResult result = session.run(query);
            assertResult.execute(result);
        }
    }

    protected Config getDriverConfig() {
        return driverConfig;
    }

    public ServerControls buildServer(TestServerBuilder testServerBuilders) {
        ClassLoader classLoader = getClass().getClassLoader();
        this.fixture = new File(classLoader.getResource(this.getFixture()).getFile());
        return testServerBuilders
            .withFixture(db -> {
                statements().forEach(db::execute);
                return null;
            })
            .newServer();
    }

    public ServerControls buildEmptyServer(TestServerBuilder testServerBuilders) {
        return testServerBuilders
                .newServer();
    }

    protected ServerControls getEmbeddedDatabaseServer() {
        return embeddedDatabaseServer;
    }

    protected void setEmbeddedDatabaseServer(ServerControls embeddedDatabaseServer) {
        this.embeddedDatabaseServer = embeddedDatabaseServer;
    }
}
