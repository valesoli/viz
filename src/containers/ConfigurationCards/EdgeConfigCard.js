import React, { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { ConnectionConfigContext } from 'core/store/ConnectionConfigContext';
import { VisualConfigContext } from 'core/store/VisualConfigContext';
import { fetchNeoQuery } from 'core/services/configQueryServices';
import { Table } from "react-bootstrap";
import { Card } from "components/Card/Card.jsx";
import { theArray, tdeArray } from "core/variables/Variables.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import MyColorPicker from "containers/CustomColorPicker/MyColorPicker";


const EdgeConfigCard = (props) => {
    const { connectionConfig } = useContext(ConnectionConfigContext);
    const { visualConfig, dispatch } = useContext(VisualConfigContext);
    const [edges, setEdges] = useState(null);
    const responseFormatter = (response) => {
        let response_table = response.data.results[0].data[0].row[0];
        let newEdges = [];
        for(var i = 0; i < response_table.length; i++){
            tdeArray[i].type = response_table[i];
            tdeArray[i].color = visualConfig.edgeColors[response_table[i]];
            newEdges.push(tdeArray[i]);
        }
        setEdges(newEdges);
        return newEdges;
    }
    
    const { data , status } = useQuery(['edges', connectionConfig, 
                                    "match (:Object)-[r]->(:Object) return collect(distinct type(r))",
                                    ], fetchNeoQuery,{
                                        onSuccess: responseFormatter
                                    });

    function receiveColor(type, color){
        edges.forEach(element => {
            if(element.type === type){
                element.color = color.hex;
            }
        });
    }

    function confirmChanges(){
        let edgesColors={};
        edges.forEach(element => {
            edgesColors[element.type] = element.color;
        });
        dispatch({type: 'CHANGE_EDGES', edgeColors: edgesColors});
    }

    return (
        <div>
        {edges == null && (
            <div>WAITING PA</div>
        )}
        {edges != null && (
            <Card
                title="Edges Configuration"
                ctTableResponsive
                content={
                    <Table striped hover>
                        <thead>
                        <tr>
                            {theArray.map((prop, key) => {
                                return <th key={key}>{prop}</th>;
                            })}
                        </tr>
                        </thead>
                        <tbody>
                        {edges.map((prop, key) => {
                            return (
                            <tr key={key}>
                                <td key={key+"1"}>{prop.type}</td>
                                <td key={key+"2"}>{<MyColorPicker color={prop.color} myType={prop.type} parentChange={receiveColor}/>}</td>
                            </tr>
                            );
                        })}
                        </tbody>
                    </Table>
                }
                legend={
                    <div className="pull-right">
                        <Button bsStyle="info" pullRight fill onClick={confirmChanges}>
                            Confirm Changes
                        </Button>
                    </div>
                }
            />
        )}
        </div>
    );
}

export default EdgeConfigCard;