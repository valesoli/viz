package ar.edu.itba.webapp.controller;

import ar.edu.itba.executor.NoDatabaseConnectionException;
import ar.edu.itba.executor.QueryExecutor;
import ar.edu.itba.webapp.dto.Response;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.ServiceUnavailableResponse;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.exceptions.ClientException;

import java.util.*;

import static org.mockito.Mockito.*;

public class QueryControllerTest {

    private Context ctx = mock(Context.class);
    private QueryExecutor queryExecutor = mock(QueryExecutor.class);
    private QueryController queryController;
    private final String validQuery = "SELECT n MATCH (n)";
    private final String translatedQuery = "MATCH (n)\nRETURN n";

    @Before
    public void beforeAll(){
        queryController = new QueryController(queryExecutor);
        reset(ctx);
        reset(queryExecutor);
    }

    @Test
    public void executeValidQuery() throws NoDatabaseConnectionException {
        when(ctx.formParam("query")).thenReturn(validQuery);
        when(queryExecutor.execute(translatedQuery)).thenReturn(response());
        queryController.execute(ctx);
        verify(ctx).json(argThat(Response::isSuccess));
    }

    @Test
    public void executeInvalidQuery() {
        String invalidQuery = "SELECT n";
        when(ctx.formParam("query")).thenReturn(invalidQuery);
        queryController.execute(ctx);
        verify(ctx).json(argThat((Response r) -> !r.isSuccess()));
    }

    @Test(expected = ServiceUnavailableResponse.class)
    public void databaseOffline() throws NoDatabaseConnectionException {
        when(ctx.formParam("query")).thenReturn(validQuery);
        when(queryExecutor.execute(translatedQuery)).thenThrow(new NoDatabaseConnectionException());
        queryController.execute(ctx);
    }

    @Test
    public void neo4jError() throws NoDatabaseConnectionException {
        when(ctx.formParam("query")).thenReturn(validQuery);
        when(queryExecutor.execute(translatedQuery)).thenThrow(new ClientException("Neo4j error"));
        queryController.execute(ctx);
        verify(ctx).json(argThat((Response r) -> !r.isSuccess()));
    }

    @Test(expected = InternalServerErrorResponse.class)
    public void internalServerError() throws NoDatabaseConnectionException {
        when(ctx.formParam("query")).thenReturn(validQuery);
        when(queryExecutor.execute(translatedQuery)).thenThrow(new RuntimeException());
        queryController.execute(ctx);
    }

    private List<Map<String, Object>> response(){
        Map<String, Object> response = new HashMap<>();
        response.put("column", "value");
        return Collections.singletonList(response);
    }
}
