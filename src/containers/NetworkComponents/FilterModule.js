import React, { useContext, useState } from 'react';
import { Card } from "components/Card/Card.jsx";
import { Table, DropdownButton, MenuItem} from "react-bootstrap";
import FormControl from 'react-bootstrap/lib/FormControl';
import { FiltersContext } from "core/store/FiltersContext/FiltersContext";
import { ConnectionConfigContext } from "core/store/ConnectionConfigContext/ConnectionConfigContext";
import { fetchNeoQuery } from 'core/services/configQueryServices';
import { useQuery } from 'react-query';

const FilterModule = (props) => {
    const { filters, dispatch } = useContext(FiltersContext);
    const { connectionConfig } = useContext(ConnectionConfigContext);
    const [ nodeTypes, setNodeTypes ] = useState([]);
    const [ edgeTypes, setEdgeTypes ] = useState([]);
    const [ test, setTest ] = useState("test");

    const nodeTypesResponseFormatter = (response) => {
        let response_table = response.data.results[0].data;        
        let nodeTypes = ["All nodes"];
        for(let i = 0; i < response_table.length; i++){
            nodeTypes.push(response_table[i].row[0]);
        }
        setNodeTypes(nodeTypes);
        return response;
    }

    const edgeTypesResponseFormatter = (response) => {
        let response_table = response.data.results[0].data;        
        let edgeTypes = ["All edges"];
        for(let i = 0; i < response_table.length; i++){
            edgeTypes.push(response_table[i].row[0]);
        }
        setEdgeTypes(edgeTypes);
        return response;
    }

    const { data, status } = useQuery(["nodeTypes", connectionConfig, "match (o:Object) return distinct o.title"],  fetchNeoQuery, {
        onSuccess: nodeTypesResponseFormatter
    });

    const { data2, status2 } = useQuery(["edgeTypes", connectionConfig, "match (:Object)-[r]->(:Object) return distinct type(r)"],  fetchNeoQuery, {
        onSuccess: edgeTypesResponseFormatter
    });

    const handleSubmit = () => {
        dispatch({type:"UPDATE_FILTERS", filters:filters});
    }

    const setSelectedNodeType = (value) => {
        filters.nodeTypes = [value];
        dispatch({type:"UPDATE_NODETYPES", nodeTypes:filters.nodeTypes});
    }

    const setSelectedEdgeType = (value) => {
        filters.edgeTypes = [value];
        dispatch({type:"UPDATE_EDGETYPES", edgeTypes:filters.edgeTypes});
    }

    const setSelectedNodeLimit = (event) => {
        filters.nodeLimit = event.target.value;
        dispatch({type:"UPDATE_NODELIMIT", nodeLimit:filters.nodeLimit});
    }

    return (
        <Card
            title="Filter Module"
            //category="Focus on the information that matters"
            //stats={<Button bsStyle="info" pullRight fill type="submit" onClick={() => handleSubmit()}>{"Filter"}</Button>}
            content={
                <Table striped hover style={{marginBottom: "0px"}}>
                    <tbody>
                        <tr>
                            <td>{"Node type"}</td>
                            <td>
                                <DropdownButton style={{width: "100%"}}
                                                bsStyle={"primary"}
                                                title={filters.nodeTypes[0]}
                                                id={`dropdown-basic`}>
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
                                                title={filters.edgeTypes[0]}
                                                id={`dropdown-basic`}>
                                    {edgeTypes.map((prop, key) => {
                                        return (
                                            <MenuItem key={key} eventKey={key} onClick={() => setSelectedEdgeType(prop)}>{prop}</MenuItem>
                                        );
                                    })}
                                </DropdownButton>
                            </td>
                        </tr>
                        <tr>
                            <td>{"Node limit"}</td>
                            <td>
                                <FormControl type="number" defaultValue={filters.nodeLimit} onChange={(event) => setSelectedNodeLimit(event)}></FormControl>
                            </td>
                        </tr>
                    </tbody>
                
                </Table>
            }
        />
    );
}
 
export default FilterModule;