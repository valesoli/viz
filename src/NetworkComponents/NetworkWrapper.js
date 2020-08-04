import React from 'react';
import Network from './Network';
import { api_cypherQuery } from '../GraphService/graphQueryService';

//MockValues
const nodesMock = [];
const linksMock = [];
const attrsMock = [];

class NetworkWrapper extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            data: {
                nodes: nodesMock,
                links: linksMock
            },
            attrs: attrsMock
        }
        this.networkCallback = this.networkCallback.bind(this);
        this.infoCallback = this.infoCallback.bind(this);
    }

    componentDidMount(){
        api_cypherQuery("match (n) with collect([id(n),n]) as nodes match (m)-[r]->(o) with nodes, collect([[id(m),id(o)],r]) as edges return nodes, edges", this.networkCallback);
        api_cypherQuery("match (o:Object)-->(a:Attribute)-->(v:Value) return id(o), o.title, a.title, v.value order by id(o)", this.infoCallback);
        //Aca deberia setearle en el name del node, el value del atributo sacado del map
        //pero no se como hacer un mapa en esto de react. Osea el loop del networkCallback
        //va aca.        
    }
    
    nodeHoverTooltip(node){
        return `<div>     
            <b>${node.name}</b>
            </div>`;
    }

    networkCallback(response){
        let response_tuple = response.results[0].data[0].row;
        let nodes = [];
        let links = [];
        //No deberiamos loopear aca, sino en el componentDidMount para poder asignarle
        //los atributos al nodo. Aca solo guardamos los nodos como estan.
        response_tuple[0].forEach(e => {
            nodes.push({id: e[0], name: e[1].title || e[1].value})
        });
        response_tuple[1].forEach(e => {
            links.push({source: e[0][0], target: e[0][1]})
        });
        this.setState({
            data: {
                nodes: nodes,
                links: links
            }
        });
    }

    infoCallback(response){
        //El mapa deberia ser con key el id del nodo, y el value deberian ser cada
        //cada uno de los atributos del nodo. Es decir tendria [key,value] adentro,
        //donde key es el nombre del atributo, y value es el valor.
        
        /*
        let response_table = response.results[0].data;
        const attrs = response_table.map((attr) =>
            <Post
                key={attr.row[0]}
                id={attr.row[0]}
                title={attr.row[1]}
                attribute={attr.row[2]}
                value={attr.row[3]}
            />
            );
        this.setState({
            attrs: attrs
        });
        */
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
