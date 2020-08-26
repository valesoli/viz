import React from "react";
import ReactDOM from "react-dom";
import Graph from "react-graph-vis";

import { Button } from 'react-bootstrap';

import { api_cypherQuery } from '../../Services/GraphService/graphQueryService';
import { onClickUpdateSelectionVis } from "./NodeVisualizer";
import { timeHours } from "d3";

class GraphContainer extends React.Component {
    constructor(props){
        super(props);
        let con_config = this.props.con_config;
        let query = this.props.query;
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
            },
            events : {
                select: (event) => onClickUpdateSelectionVis(22, con_config)
            },
            query: query
        }

        //Binding
        this.networkCallback = this.networkCallback.bind(this);
        this.infoCallback = this.infoCallback.bind(this);
        this.buildNodes = this.buildNodes.bind(this);
    }
    
    componentDidMount(){
        api_cypherQuery(this.state.query, this.networkCallback, this.props.con_config);
    }

    componentWillReceiveProps(props){
        api_cypherQuery(props.query, this.networkCallback, this.props.con_config);
    }

    networkCallback(response){
        this.setState({
            base: {
                nodes: response.results[0].data[0].row[0],
                links: response.results[0].data[0].row[1]
            }
        }, () => { api_cypherQuery("match (o:Object)-->(a:Attribute)-->(v:Value) return id(o), o.title, a.title, v.value, v.interval order by id(o)", this.infoCallback, this.props.con_config); });
    }

    infoCallback(response){
        let response_table = response.results[0].data;
        
        const attrsHashmap = response_table.reduce((obj, item) => {
            if(obj[item.row[0]]){
                obj[item.row[0]].attributes.push([item.row[2], item.row[3], item.row[4]]);
            } else {
                let objr = new Object();
                objr.id = item.row[0];
                objr.attributes = [];
                objr.attributes.push([item.row[2], item.row[3], item.row[4]]);
                obj[item.row[0]] = objr;
            }
            return obj;
          }, {});
          
        const mergedAttrsArray = Object.values(attrsHashmap);
        this.setState({
            attrs: attrsHashmap
        }, this.buildNodes);
    }

    buildNodes(){
        let nodes = [];
        let links = [];
        var attrs = this.state.attrs;

        this.state.base.nodes.forEach(e => {
            nodes.push(
                {
                    id: e[0], 
                    title: attrs[e[0]].attributes[0][1] || e[1].title, 
                    group: e[1].title,
                    color: this.props.visual.nodeColors[e[1].title]
                })
        });
        this.state.base.links.forEach(e => {
            links.push(
                {
                    from: e[0][0], 
                    to: e[0][1],
                    title: e[1],
                    color: this.props.visual.edgeColors[e[1]],
                    arrows: 'to'
                })
        });

        this.setState({
            graph: {
                nodes: nodes,
                edges: links
            },
            events : {
                select: (params) => onClickUpdateSelectionVis(params.nodes[0], this.props.con_config)
            }
        });
    }
   
    render(){
        return (
            <Graph
                key={this.state.events}
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
export default GraphContainer;