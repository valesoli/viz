import './Popoto.css';
import * as d3 from 'd3';
import * as popoto from 'popoto';
import React, { Component } from 'react';

class Popoto extends Component {

    componentDidMount() {
        this.popotoConfig();
    }

    popotoConfig() {
        //Neo4j rest settings 
        popoto.rest.CYPHER_URL = "https://localhost:7473/db/data/transaction/commit";
        popoto.rest.AUTHORIZATION = "Basic " + btoa("neo4j:admin");
        
        // Define the list of label provider to customize the graph behavior:
        // Only two labels are used in Neo4j movie graph example: "Movie" and "Person"
        popoto.provider.node.Provider = {
            "Object": {
                "returnAttributes": ["title", "inteval"],
                "constraintAttribute": "title"
            },
            "Atribute": {
                "returnAttributes": ["title", "interval"],
                "constraintAttribute": "title",
                // Customize result display for Atribute nodes:
                "displayResults": function (pResultElmt) {
                    // Here D3.js mechanisms is used to generate HTML code.
                    // By default Popoto.js generates a <p> element for each result.
                    // pResultElmt parameter is the <p> element selected with D3.js
                    // So for "Atribute" result nodes two elements are generated:
                    // An <h3> element containing the person name
                    pResultElmt.append("h3")
                        .text(function (result) {
                            return result.attributes.title;
                        });
                    // A <span> element with the computed age from born attribute
                    pResultElmt.filter(function (result) {
                        // Filter on attribute having born attribute value
                        return result.attributes.interval;
                    }).append("span").text(function (result) {
                        return "Interval: " + (result.attributes.interval);
                    });
                }
            }
        };
        // Change the number of displayed results:
        popoto.result.RESULTS_PAGE_SIZE = 20;
        // Add a listener on returned result count to update count in page
        popoto.result.onTotalResultCount(function (count) {
            d3.select("#rescount").text(function (d) {
                return "(" + count + ")";
            })
        });
        // Add a listener on new relation added
        popoto.graph.on(popoto.graph.Events.GRAPH_NODE_RELATION_ADD, function (relations) {
            var newRelation = relations[0];
            // Collapse all expanded choose nodes first to avoid having value node in selection.
            popoto.graph.node.collapseAllNode();
            var linksToRemove = popoto.dataModel.links.filter(function (link) {
                // All other links starting from same source node except new one.
                return link !== newRelation && link.source === newRelation.source;
            });
            linksToRemove.forEach(function (link) {
                var willChangeResults = popoto.graph.node.removeNode(link.target);
                popoto.result.hasChanged = popoto.result.hasChanged || willChangeResults;
            });
            popoto.update();
        });
        // Start the generation using parameter as root label of the query.
        popoto.start("Object");
    }

    render() {

        return (
            <div>
                <section className="ppt-section-main">
                    <div className="ppt-section-header">
                        <span className="ppt-header-span">Neo4j Visualization with Popoto</span>
                    </div>

                    <div id="popoto-graph" className="ppt-div-graph">
                    </div>

                    <div id="popoto-cypher" className="ppt-container-query">
                    </div>

                    <div className="ppt-section-header">
                        RESULTS <span id="rescount" className="ppt-count"></span>
                    </div>

                    <div id="popoto-results" className="ppt-container-results">
                    </div>

                </section>
            </div>
        )
    }
}

export default Popoto;