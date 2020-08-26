import React from 'react';
import { Card } from "components/Card/Card.jsx";
import { neo4j_config } from "variables/ConnectionVariables.jsx";
import { api_cypherQuery } from '../../Services/GraphService/graphQueryService';
import { Table, DropdownButton, MenuItem} from "react-bootstrap";
import FormControl from 'react-bootstrap/lib/FormControl';
import Button from "components/CustomButton/CustomButton.jsx";
import {applyFilters} from '../../App.js';


class FilterModule extends React.Component{
    constructor(props){
        super(props);

        this.state = {
            nodeTypes: [],
            edgeTypes: [],
            nodeLmimit: 100,
            selectedNodeType: "All nodes",
            selectedEdgeType: "All edges"
        }

        this.nodeTypesCallback = this.nodeTypesCallback.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount(){
        api_cypherQuery("match (o:Object)-[e]-(:Object) return collect(distinct o.title), collect(distinct type(e))", this.nodeTypesCallback, neo4j_config);
    }

    nodeTypesCallback(response){
        var nodeTypes = response.results[0].data[0].row[0];
        var edgeTypes = response.results[0].data[0].row[1];
        this.setState({
            nodeTypes: nodeTypes,
            edgeTypes: edgeTypes
        });
    }

    createLegend(json) {
        var legend = [];
        for (var i = 0; i < json["names"].length; i++) {
          var type = "fa fa-circle text-" + json["types"][i];
          legend.push(<i className={type} key={i} />);
          legend.push(" ");
          legend.push(json["names"][i]);
        }
        return legend;
    }
    
    selectNodeType(value){
        this.setState({
            selectedNodeType: value
        });
    }

    selectEdgeType(value){
        this.setState({
            selectedEdgeType: value
        });
    }

    handleSubmit(){
        var whereNodes = "";
        var whereEdges = "";
        if(this.state.selectedNodeType != "All nodes"){
            whereNodes += "where n.title = \"" + this.state.selectedNodeType + "\"";
            whereEdges += "where m.title = \"" + this.state.selectedNodeType + "\" or o.title = \"" + this.state.selectedNodeType + "\"";
        }
        var query = `match (n:Object) ${whereNodes} with collect([id(n),n]) as nodes  match (m:Object)-[r]->(o:Object) ${whereEdges} with nodes, collect([[id(m),id(o)],type(r)]) as edges return nodes, edges`;
        applyFilters(query);
    }

    render(){
        return(
            // <form onSubmit={this.handleSubmit}>
                <Card
                    title="Filter Module"
                    // category="Focus on the information that matters"
                    stats={<Button bsStyle="info" pullRight fill type="submit" onClick={() => this.handleSubmit()}>{"Filter"}</Button>}
                    content={
                        <Table striped hover style={{marginBottom: "0px"}}>
                            <tbody>
                                <tr>
                                    <td>{"Node type"}</td>
                                    <td>
                                        <DropdownButton style={{width: "100%"}}
                                                        bsStyle={"primary"}
                                                        title={this.state.selectedNodeType}
                                                        id={`dropdown-basic`}>
                                                        {this.state.nodeTypes.map((prop, key) => {
                                                            return (
                                                                <MenuItem eventKey={key} onClick={() => this.selectNodeType(prop)}>{prop}</MenuItem>
                                                            );
                                                        })}
                                        </DropdownButton>
                                    </td>
                                </tr>
                                <tr>
                                    <td>{"Edge type"}</td>
                                    <td>
                                        <DropdownButton style={{width: "100%"}}
                                                        bsStyle={"primary"}
                                                        title={this.state.selectedEdgeType}
                                                        id={`dropdown-basic`}>
                                                        {this.state.edgeTypes.map((prop, key) => {
                                                            return (
                                                                <MenuItem eventKey={key} onClick={() => this.selectEdgeType(prop)}>{prop}</MenuItem>
                                                            );
                                                        })}
                                        </DropdownButton>
                                    </td>
                                </tr>
                                <tr>
                                    <td>{"Node limit"}</td>
                                    <td>
                                        <FormControl type="number" defaultValue={this.state.nodeLmimit}></FormControl>
                                    </td>
                                </tr>
                            </tbody>
                        
                        </Table>
                    }
                />
            // </form>
        );
    }
}

export default FilterModule;