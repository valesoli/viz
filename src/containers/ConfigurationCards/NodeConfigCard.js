import React, { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { ConnectionConfigContext } from 'core/store/ConnectionConfigContext';
import { VisualConfigContext } from 'core/store/VisualConfigContext';
import { fetchNeoQuery } from 'core/services/configQueryServices';
import { Table, DropdownButton, MenuItem} from "react-bootstrap";
import { Card } from "components/Card/Card.jsx";
import { thArray, tdArray } from "core/variables/Variables.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import MyColorPicker from "containers/CustomColorPicker/MyColorPicker";
import Loader from 'react-loader-spinner';

const NodeConfigCard = (props) => {
    const { connectionConfig } = useContext(ConnectionConfigContext);
    const { visualConfig, dispatch } = useContext(VisualConfigContext);
    const [ nodeInfo, setNodeInfo ] = useState(null);

    const responseFormatter = (response) => {
        let response_table = response.data.results[0].data;        
        let newNodes = [];
        let newMainAttr = [];
        let newDefaultAttr = [];
        for(let i = 0; i < response_table.length; i++){
            tdArray[i].type = response_table[i].row[0];
            tdArray[i].color = visualConfig.nodeColors[response_table[i].row[0]];
            tdArray[i].attribute = response_table[i].row[1][0];
            tdArray[i].default = response_table[i].row[1][0];
            tdArray[i].allAttrs = response_table[i].row[1];
            newNodes.push(tdArray[i]);
            newMainAttr.push({key: i, value: tdArray[i].attribute});
            newDefaultAttr.push({key: i, value: tdArray[i].default});
        }
        setNodeInfo({nodes: newNodes, mainAttr: newMainAttr, defaultAttr: newDefaultAttr});
        return response;
    }

    const { data, status } = useQuery(["nodes", connectionConfig, 
                                    "match (o:Object)-->(a:Attribute)-->(v:Value) return o.title, collect(distinct a.title) order by o.title"
                                    ],  fetchNeoQuery, {
                                        onSuccess: responseFormatter
                                    });


    function changeValueMain(key, value){
        let found = false;
        let changingMainAttr = nodeInfo.mainAttr;
        for(let i = 0; i < changingMainAttr.length; i++){
            if(changingMainAttr[i].key === key){
                changingMainAttr[i].value = value;
                found = true;
                break;
            }
        }
        if(!found){
            changingMainAttr.push({ key: key, value: value});
        }
        nodeInfo.mainAttr = changingMainAttr;
    }

    function changeValueDefault(key, value) {
        let found = false;
        let changingDefaultAttr = nodeInfo.defaultAttr;
        for(let i = 0; i < changingDefaultAttr.length; i++){
            if(changingDefaultAttr[i].key === key){
                changingDefaultAttr[i].value = value;
                found = true;
                break;
            }
        }
        if(!found){
            changingDefaultAttr.push({ key: key, value: value});
        }
        nodeInfo.defaultAttr = changingDefaultAttr;
    }


    function colorChange(type, color){
        nodeInfo.nodes.forEach(element => {
            if(element.type === type){
                element.color = color.hex;
            }
        });
    }

    function confirmChanges(){
        let nodeColors={};
        nodeInfo.nodes.forEach(element => {
            nodeColors[element.type] = element.color;
        });
        dispatch({type: 'CHANGE_NODES', nodeColors: nodeColors});
    }

    return (
        <>
        {nodeInfo == null && (
            <div style={{textAlign: "center"}}>
                <Loader type="ThreeDots" color="#00BFFF" height={80} width={80} />
            </div>
        )}
        {nodeInfo != null && (
            <Card
                title="Nodes Configuration"
                ctTableResponsive
                content={
                    <Table striped hover style={{marginBottom: "0px"}}>
                        <thead>
                        <tr>
                            {thArray.map((prop, key) => {
                                return <th key={key}>{prop}</th>;
                            })}
                        </tr>
                        </thead>
                        <tbody>
                            {nodeInfo.nodes.map((prop, key) => {
                                return (
                                    <tr key={key}>
                                        <td key={key+"1"}>{prop.type}</td>
                                        <td key={key+"2"}>
                                            <MyColorPicker color={prop.color} myType={prop.type} parentChange={colorChange}/>
                                        </td>                                    
                                        <td key={key+"3"}>
                                            <DropdownButton style={{width: "100%"}}
                                                            bsStyle={"primary"}
                                                            title={nodeInfo.mainAttr.map((val) => {                                                            
                                                                if(val.key === key)
                                                                    return val.value;
                                                            })}
                                                            id={`dropdown-basic`}>
                                                            {prop.allAttrs.map((prop, key2) => {
                                                                return (
                                                                <MenuItem key={key2} eventKey={key2} onClick={() => changeValueMain(key, prop)}>{prop}</MenuItem>
                                                                );
                                                            })}
                                            </DropdownButton>
                                        </td>
                                        <td key={key+"4"}>
                                            <DropdownButton style={{width: "100%"}}
                                                            bsStyle={"primary"}
                                                            title={nodeInfo.defaultAttr.map((val) => {
                                                                if(val.key === key)
                                                                    return val.value;
                                                            })}
                                                            id={`dropdown-basic`}>
                                                            {prop.allAttrs.map((prop, key2) => {
                                                                return (
                                                                <MenuItem key={key2} eventKey={key2} onClick={() => changeValueDefault(key, prop)}>{prop}</MenuItem>
                                                                );
                                                            })}
                                            </DropdownButton>
                                        </td> 
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
    </>
    );
}
 
export default NodeConfigCard;