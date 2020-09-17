import React from 'react';
import { Card } from "components/Card/Card.jsx";
import { api_cypherQuery } from '../../Services/GraphService/graphQueryService';
import{
    legendNodes
} from "../../variables/Variables.jsx";
import { Network, DataSet } from "vis-network/standalone";
import {onClickUpdateSelectionVis} from './NodeVisualizer';

//MockValues
const nodesMock = [];
const linksMock = [];
const attrsMock = [];

class NetworkVis extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            data: {
                nodes: nodesMock,
                edges: linksMock
            },
            base: {
                nodes: null,
                links: null
            },
            attrs: attrsMock
        }
        this.networkCallback = this.networkCallback.bind(this);
        this.infoCallback = this.infoCallback.bind(this);
        this.buildNodes = this.buildNodes.bind(this);
        this.containerRef = React.createRef(null);
    }

    componentDidMount(){
        api_cypherQuery("match (n:Object) with collect([id(n),n]) as nodes match (m:Object)-[r]->(o:Object) with nodes, collect([[id(m),id(o)],type(r)]) as edges return nodes, edges", this.networkCallback, this.props.con_config);
    }

    componentDidUpdate(){
        if(this.containerRef.current){
            var container = document.getElementById(this.containerRef.current.id);
        
            var options = {
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

            this.network = new Network(container, this.state.data, options);
            this.network.on("selectNode", (params) => {
                onClickUpdateSelectionVis(params.nodes[0], this.props.con_config);
            });
        }   
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
                    color: {
                        inherit: 'to'
                    },
                    arrows: 'to'
                })
        });
        var nodesForVis = new DataSet(nodes);
        var linksForVis = new DataSet(links);
        this.setState({
            data: {
                nodes: nodesForVis,
                edges: linksForVis
            }
        });
    }
    
    nodeHoverTooltip(node){
        return `<div>     
            <b>${node.name}</b>
            </div>`;
    }

    edgeHoverTooltip(link){
        return `<div>     
            <b>${link.relation}</b>
            </div>`
    }

    networkCallback(response){
        this.setState({
            base: {
                nodes: response.results[0].data[0].row[0],
                links: response.results[0].data[0].row[1]
            }
        }, () => { api_cypherQuery("match (o:Object)-->(a:Attribute)-->(v:Value) return id(o), o.title, a.title, v.value order by id(o)", this.infoCallback, this.props.con_config); });
    }

    infoCallback(response){
        let response_table = response.results[0].data;
        
        const attrsHashmap = response_table.reduce((obj, item) => {
            if(obj[item.row[0]]){
                obj[item.row[0]].attributes.push([item.row[2], item.row[3]]);
            } else {
                let objr = new Object();
                objr.id = item.row[0];
                objr.attributes = [];
                objr.attributes.push([item.row[2], item.row[3]]);
                obj[item.row[0]] = objr;
            }
            return obj;
          }, {});
          
        const mergedAttrsArray = Object.values(attrsHashmap);
        this.setState({
            attrs: attrsHashmap
        }, this.buildNodes);
    }

    createLegend(json) {
        var legend = [];
        for (var i = 0; i < json["names"].length; i++) {
          var type = "fa fa-circle";
          var color = json["colors"][i];
          legend.push(<i className={type} style={{color: color}} key={i} />);
          legend.push(" ");
          legend.push(json["names"][i]);
        }
        return legend;
    }

    render(){   
        return(
            <div id="containerRef" ref={this.containerRef} />
        );
    }
}

export default NetworkVis;
