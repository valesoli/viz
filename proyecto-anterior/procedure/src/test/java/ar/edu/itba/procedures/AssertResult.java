package ar.edu.itba.procedures;

import org.neo4j.driver.v1.StatementResult;

public interface AssertResult {
    void execute(StatementResult result);
}
