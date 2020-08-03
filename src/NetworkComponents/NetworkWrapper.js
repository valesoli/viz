import React from 'react';
import Network from './Network';
import { api_cypherQuery } from '../GraphService/graphQueryService';

//MockValues
const nodesMock = [{ id: 1 }, { id: 2 }, { id: 3 }, { id: 4 }, { id: 5 }];
const linksMock = [
  { source: 1, target: 2 },
  { source: 1, target: 3 },
  { source: 1, target: 4 },
  { source: 2, target: 4 },
  { source: 3, target: 4 },
  { source: 4, target: 5 }
];

class NetworkWrapper extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            data: {
                nodes: nodesMock,
                links: linksMock
            }
        }
        this.networkCallback = this.networkCallback.bind(this);
    }

    componentDidMount(){
        api_cypherQuery("match (n) with collect(id(n)) as nodes match (m)-[r]->(o) with nodes, collect([id(m),id(o)]) as edges return nodes, edges", this.networkCallback);
    }
    
    nodeHoverTooltip(node){
        return `<div>     
            <b>${node.name}</b>
            </div>`;
    }

    networkCallback(response){
        let response_tuple = response.results[0].data[0].row
        let nodes = [];
        let links = [];
        response_tuple[0].forEach(e => {
            nodes.push({id: e, name: e})
        });
        response_tuple[1].forEach(e => {
            links.push({source: e[0], target: e[1]})
        });
        this.setState({
            data: {
                nodes: nodes,
                links: links
            }
        });
        //this.updateForceSimulation();
    }

    render(){
        return(
            <Network
                nodesData={this.state.data.nodes}
                linksData={this.state.data.links}
                nodeHoverTooltip={this.nodeHoverTooltip}
            />
        );
    }
}

export default NetworkWrapper;
