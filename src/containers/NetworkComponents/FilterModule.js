import React, { useState } from 'react';
import { Card } from "components/Card/Card.jsx";
import { neo4j_config } from "core/variables/ConnectionVariables.jsx";
import { api_cypherQuery } from 'core/services/graphQueryService';
import { Table, DropdownButton, MenuItem} from "react-bootstrap";
import FormControl from 'react-bootstrap/lib/FormControl';
import Button from "components/CustomButton/CustomButton.jsx";
import {applyFilters} from '../../App.js';

const FilterModule = (props) => {
    const [ nodeTypes, setNodeTypes ] = useState([]);
    const [ edgeTypes, setEdgeTypes ] = useState([]);
    const [ nodeLimit, setNodeLimit ] = useState(100);
    const [ selectedNodeType, setSelectedNodeType ] = useState('All nodes');
    const [ selectedEdgeType,  setSelectedEdgeType ] = useState('All edges');

    // Acá cargaba los valores, pero ahora en vez de hacerlo así deberíamos sacarlos de algún contexto
    // api_cypherQuery("match (o:Object)-[e]-(:Object) return collect(distinct o.title), collect(distinct type(e))", this.nodeTypesCallback, neo4j_config);

    const nodeTypesCallback = (response) => {
        let newNodeTypes = response.results[0].data[0].row[0];
        let newEdgeTypes = response.results[0].data[0].row[1];
        setNodeTypes(newNodeTypes);
        setEdgeTypes(newEdgeTypes);
    }

    const createLegend = (json) => {
        var legend = [];
        for (var i = 0; i < json["names"].length; i++) {
          var type = "fa fa-circle text-" + json["types"][i];
          legend.push(<i className={type} key={i} />);
          legend.push(" ");
          legend.push(json["names"][i]);
        }
        return legend;
    }
    
    const selectNodeType = (value) => {
        setSelectedNodeType(value);
    }

    const selectEdgeType = (value) => {
        setSelectedEdgeType(value);
    }

    const handleSubmit = () => {
        var whereNodes = "";
        var whereEdges = "";
        if(selectedNodeType !== "All nodes"){
            whereNodes += "where n.title = \"" + selectedNodeType + "\"";
            whereEdges += "where (m.title = \"" + selectedNodeType + "\" or o.title = \"" + selectedNodeType + "\")";
            if(selectedEdgeType !== "All edges"){
                whereEdges += " and type(r) = \"" + selectedEdgeType + "\"";
            }
        }
        else{
            if(selectedEdgeType !== "All edges"){
                whereEdges += "where type(r) = \"" + selectedEdgeType + "\"";
            }
        }
        let nodeLimitString = "limit " + nodeLimit;
        let query = `match (n:Object) ${whereNodes} with n as allnodes ${nodeLimitString} with collect([id(allnodes),allnodes]) as nodes  match (m:Object)-[r]->(o:Object) ${whereEdges} with nodes, collect([[id(m),id(o)],type(r)]) as edges return nodes, edges`;
        applyFilters(query);
    }

    return (
        <Card
            title="Filter Module"
            // category="Focus on the information that matters"
            stats={<Button bsStyle="info" pullRight fill type="submit" onClick={() => handleSubmit}>{"Filter"}</Button>}
            content={
                <Table striped hover style={{marginBottom: "0px"}}>
                    <tbody>
                        <tr>
                            <td>{"Node type"}</td>
                            <td>
                                <DropdownButton style={{width: "100%"}}
                                                bsStyle={"primary"}
                                                title={selectedNodeType}
                                                id={`dropdown-basic`}>
                                    <MenuItem eventKey={100} onClick={() => setSelectedNodeType("All nodes")}>{"All nodes"}</MenuItem>
                                    {nodeTypes.map((prop, key) => {
                                        return (
                                            <MenuItem key={key} eventKey={key} onClick={() => setSelectedNodeType(prop)}>{prop}</MenuItem>
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
                                                title={selectedEdgeType}
                                                id={`dropdown-basic`}>
                                    <MenuItem eventKey={100} onClick={() => setSelectedEdgeType("All edges")}>{"All edges"}</MenuItem>
                                    {edgeTypes.map((prop, key) => {
                                        return (
                                            <MenuItem key={key}  eventKey={key} onClick={() => setSelectedEdgeType(prop)}>{prop}</MenuItem>
                                        );
                                    })}
                                </DropdownButton>
                            </td>
                        </tr>
                        <tr>
                            <td>{"Node limit"}</td>
                            <td>
                                <FormControl type="number" defaultValue={nodeLimit} ></FormControl>
                            </td>
                        </tr>
                    </tbody>
                
                </Table>
            }
        />
    );
}
 
export default FilterModule;