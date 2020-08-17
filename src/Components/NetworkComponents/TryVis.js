import React from "react";
import ReactDOM from "react-dom";
import Graph from "react-graph-vis";

import { onClickUpdateSelectionVis } from "./NodeVisualizer";

class TryVis extends React.Component {
    constructor(props){
        super(props);
        let con_config = this.props.con_config;
        this.state = {
            con_config: con_config,
            graph: {
                nodes: [
                  { id: 1, label: "Node 1", title: "node 1 tootip text" },
                  { id: 2, label: "Node 2", title: "node 2 tootip text" },
                  { id: 3, label: "Node 3", title: "node 3 tootip text" },
                  { id: 4, label: "Node 4", title: "node 4 tootip text" },
                  { id: 5, label: "Node 5", title: "node 5 tootip text" }
                ],
                edges: [
                  { from: 1, to: 2 },
                  { from: 1, to: 3 },
                  { from: 2, to: 4 },
                  { from: 2, to: 5 }
                ]
            },
            options : {
                layout: {
                  hierarchical: true
                },
                edges: {
                  color: "#000000"
                },
                height: "500px"
            },
            events : {
                select: function(event) {
                    onClickUpdateSelectionVis(22, con_config);
                }
            }
        }
    }
   
    render(){
        return (
            <Graph
              graph={this.state.graph}
              options={this.state.options}
              events={this.state.events}
              getNetwork={network => {
                //  if you want access to vis.js network api you can set the state in a parent component using this property
              }}
            />
        );
    }
}
export default TryVis;