import React from 'react';
import Network from './Network';
import { api_cypherQuery } from '../GraphService/graphQueryService';
import { Container , Row, Col} from 'react-bootstrap';
import { CardActions } from '@material-ui/core';

//MockValues
const nodesMock = [];
const linksMock = [];
const attrsMock = [];

class TestPost extends React.Component{
    constructor(props){
        super(props);
    }
    
    render(){
        if(this.props.value[0] == null){
            return <div>Loading</div>;
        }
        return(
            <div>
                Node: {this.props.value[0].id}
                Attribute: {this.props.value[0].attributes}        
            </div>
        );
    }
}

class NetworkWrapper extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            data: {
                nodes: nodesMock,
                links: linksMock
            },
            base: {
                nodes: null,
                links: null
            },
            attrs: attrsMock,
            loading1: true,
            loading2: true
        }
        this.networkCallback = this.networkCallback.bind(this);
        this.infoCallback = this.infoCallback.bind(this);
        this.buildNodes = this.buildNodes.bind(this);
    }

    componentDidMount(){
        //api_cypherQuery("match (n) with collect([id(n),n]) as nodes match (m)-[r]->(o) with nodes, collect([[id(m),id(o)],r]) as edges return nodes, edges", this.networkCallback);
        api_cypherQuery("match (n:Object) with collect([id(n),n]) as nodes match (m:Object)-[r]->(o:Object) with nodes, collect([[id(m),id(o)],r]) as edges return nodes, edges", this.networkCallback);
        
        /*
        let nodes = [];
        let links = [];
        var map = this.state.attrs;
        this.state.data.nodes.forEach(e => {
            nodes.push({id: e[0], name: map[e[0]][1] || e[1].title})
        });
        this.state.data.links.forEach(e => {
            links.push({source: e[0][0], target: e[0][1]})
        });
        */
    }

    componentDidUpdate(){
        
    }

    buildNodes(){
        let nodes = [];
        let links = [];
        var attrs = this.state.attrs;

        this.state.base.nodes.forEach(e => {
            nodes.push({id: e[0], name: attrs[e[0]].attributes[0][1] || e[1].title})
        });
        this.state.base.links.forEach(e => {
            links.push({source: e[0][0], target: e[0][1]})
        });
        this.setState({
            data: {
                nodes: nodes,
                links: links
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
            <b>${link.source}</b>
            </div>`
    }

    networkCallback(response){
        /*
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
        });*/
        this.setState({
            base: {
                nodes: response.results[0].data[0].row[0],
                links: response.results[0].data[0].row[1]
            },
            loading1: false
        }, () => { api_cypherQuery("match (o:Object)-->(a:Attribute)-->(v:Value) return id(o), o.title, a.title, v.value order by id(o)", this.infoCallback); });
    }

    infoCallback(response){
        let response_map = new Map();
        let response_table = response.results[0].data;
        
        const attrsHashmap = response_table.reduce((obj, item) => {
            if(obj[item.row[0]]){
                obj[item.row[0]].attributes.push([item.row[2], item.row[3]]);
            } else {
                let objr = new Object();
                objr.id = item.row[0];
                //objr.title = attr.row[1];
                objr.attributes = [];
                objr.attributes.push([item.row[2], item.row[3]]);
                obj[item.row[0]] = objr;
            }
            return obj;
          }, {});
          
        const mergedAttrsArray = Object.values(attrsHashmap);
        this.setState({
            attrs: attrsHashmap,
            loading2: false
        }, this.buildNodes);
    }

    render(){        
        return(
            <Container>
                <Row>
                    <Network
                        nodesData={this.state.data.nodes}
                        linksData={this.state.data.links}
                        nodeHoverTooltip={this.nodeHoverTooltip}
                        edgeHoverTooltip={this.edgeHoverTooltip}
                    />
                </Row>
                {/* <Row>
                    <Col>
                        <TestPost value={this.state.attrs}></TestPost>
                    </Col>
                </Row> */}
            </Container>
        );
    }
}

export default NetworkWrapper;
