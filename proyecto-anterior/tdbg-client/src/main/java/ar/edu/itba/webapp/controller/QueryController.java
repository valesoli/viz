package ar.edu.itba.webapp.controller;

import ar.edu.itba.executor.NoDatabaseConnectionException;
import ar.edu.itba.executor.QueryExecutor;
import ar.edu.itba.grammar.QueryCompiler;
import ar.edu.itba.webapp.dto.Response;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.ServiceUnavailableResponse;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class QueryController {

    private final QueryExecutor queryExecutor;

    private final static Logger logger = LoggerFactory.getLogger(QueryController.class);

    public QueryController(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public void execute(Context ctx) {
        String query = ctx.formParam("query");
        String cypherQuery;
        Instant translationStart = Instant.now();
        try {
            logger.info("Executing query:\n{}", query);
            cypherQuery = new QueryCompiler().compile(query);
        }
        catch (Exception e) {
            logger.error("Error in parser. Exception: {}. Stacktrace: {}", e.getMessage(), e.getStackTrace());
            ctx.json(new Response(false, e.getMessage(), elapsedTime(translationStart), "0 ms"));
            return;
        }

        String translationTime = elapsedTime(translationStart);
        Instant executionStart = Instant.now();
        try {
            logger.info("Cypher translated query:\n{}",cypherQuery);
            Object result = queryExecutor.execute(cypherQuery);
            ctx.json(new Response(true, "Success.", result, translationTime, elapsedTime(executionStart)));
        }
        catch (ClientException e) {
            logger.error("Error executing query in neo4j. Exception: {}", e.getMessage());
            ctx.json(new Response(false, "There was an error while executing the cypher query: " + e.getMessage(),
                    translationTime, elapsedTime(executionStart))
            );
        }
        catch (NoDatabaseConnectionException e) {
            throw new ServiceUnavailableResponse("Database is offline");
        }
        catch (Exception e) {
            logger.error("Unexpected error. Exception: {}", e.getMessage());
            throw new InternalServerErrorResponse("There was an unexpected error while executing the query. Please check the logs.");
        }
    }

    private String elapsedTime(Instant start) {
        Instant finish = Instant.now();
        return Duration.between(start, finish).toMillis() + " ms";
    }
}
