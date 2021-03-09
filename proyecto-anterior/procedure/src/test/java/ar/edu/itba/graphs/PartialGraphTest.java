package ar.edu.itba.graphs;

import ar.edu.itba.algorithms.utils.interval.Interval;
import ar.edu.itba.algorithms.utils.time.Date;
import ar.edu.itba.config.constants.DirectionType;
import ar.edu.itba.graph.impl.PartialGraph;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.graphdb.Node;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PartialGraphTest extends GraphTest {

    private PartialGraph graph;

    @BeforeAll
    void setGraph() {
        this.graph = new PartialGraph(
                this.db,
                "Airport",
                "Flight",
                "interval",
                "interval",
                DirectionType.OUTGOING,
                null,
                true
        );
    }

    @Test
    void testGetRelationshipsFromNode() {
        Node n = getAirportWithCityName("A");

        assert n != null;
        Long id = n.getId();
        List<Pair<List<Interval>, Long>> edges = graph.getRelationshipsFromNode(id);
        assertThat(edges)
                .extracting(Pair::getRight)
                .hasSize(3)
                .contains(
                        getAirportWithCityName("B").getId(),
                        getAirportWithCityName("C").getId(),
                        getAirportWithCityName("D").getId()
                );

        assertThat(edges)
                .flatExtracting(Pair::getLeft)
                .hasSize(4)
                .contains(
                        new Interval(Date.parse("2018-02-06"), Date.parse("2018-02-08")),
                        new Interval(Date.parse("2018-02-10"), Date.parse("2018-02-13")),
                        new Interval(Date.parse("2018-02-01"), Date.parse("2018-02-03")),
                        new Interval(Date.parse("2018-02-04"), Date.parse("2018-02-06"))
                );
    }

}
