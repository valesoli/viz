import { DataSet, Network } from 'vis-network/standalone';
import React, { Component, createRef } from "react";

import { onClickUpdateSelectionVis } from "./NodeVisualizer";
import { EventController, CompletionEvent, ClickEdgeEvent, ClickNodeEvent, ErrorEvent } from './events';


const options = {
    nodes: {
        shape: "dot"
    },
    physics: {
        forceAtlas2Based: {
            gravitationalConstant: -26,
            centralGravity: 0.005,
            springLength: 230,
            springConstant: 0.18,
        },
        maxVelocity: 146,
        solver: "forceAtlas2Based",
        timestep: 0.35,
        stabilization: { iterations: 150 },
        },
    height: '500px'
};
// initialize your network!


export class MyVis extends Component {
  constructor() {
    super();
    this.network = {};
    this.appRef = createRef();
    this.call = onClickUpdateSelectionVis;
  }

  componentDidMount() {
    this.network = new Network(this.appRef.current, this.props.data, options);
    let myVis = this;
    this.network.on('click', function (params) {
        if (params.nodes.length > 0) {
            let nodeId = this.getNodeAt(params.pointer.DOM);
            myVis.call(22, myVis.props.con_config);
        } else if (params.edges.length > 0) {
            let edgeId = this.getEdgeAt(params.pointer.DOM);
            myVis.call(22, myVis.props.con_config);
        }
    });
  }

  render() {
    return (
      <div ref={this.appRef} />
    );
  }
}