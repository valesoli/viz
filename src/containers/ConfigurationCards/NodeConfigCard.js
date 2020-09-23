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

const NodeConfigCard = (props) => {
    const { connectionConfig } = useContext(ConnectionConfigContext);
    const { visualConfig, dispatch } = useContext(VisualConfigContext);
    const [nodes, setNodes] = useState(tdArray);
    const [mainAttr, setMainAttr] = useState({key:"0", value:"Select"});
    const [defaultAttr, setDefaultAttr] = useState({key:"0", value:"Select"});

    const nodesTypesCallback = (response) => {
        let response_table = response.data.results[0].data;        
        var newNodes = [];
        var newMainAttr = [];
        var newDefaultAttr = [];
        for(var i = 0; i < response_table.length; i++){
            tdArray[i].type = response_table[i].row[0];
            tdArray[i].color = visualConfig.nodeColors[response_table[i].row[0]];
            tdArray[i].attribute = response_table[i].row[1][0];
            tdArray[i].default = response_table[i].row[1][0];
            tdArray[i].allAttrs = response_table[i].row[1];
            newNodes.push(tdArray[i]);
            newMainAttr.push({key: i, value: tdArray[i].attribute});
            newDefaultAttr.push({key: i, value: tdArray[i].default});
        }
        setNodes(newNodes);
        setMainAttr(newMainAttr);
        setDefaultAttr(newDefaultAttr);
    }

    const { data, status } = useQuery(['nodes', connectionConfig, "match (o:Object)-->(a:Attribute)-->(v:Value) return o.title, collect(distinct a.title) order by o.title"], fetchNeoQuery, {
        onSuccess: nodesTypesCallback 
    });


    function changeValueMain(key, value){
        var found = false;
        var changingMainAttr = mainAttr;
        for(var i = 0; i < changingMainAttr.length; i++){
            if(changingMainAttr[i].key === key){
                changingMainAttr[i].value = value;
                found = true;
                break;
            }
        }
        if(!found){
            changingMainAttr.push({ key: key, value: value});
        }
        setMainAttr(changingMainAttr);
    }

    function changeValueDefault(key, value) {
        var found = false;
        var changingDefaultAttr = defaultAttr;
        for(var i = 0; i < changingDefaultAttr.length; i++){
            if(changingDefaultAttr[i].key === key){
                changingDefaultAttr[i].value = value;
                found = true;
                break;
            }
        }
        if(!found){
            changingDefaultAttr.push({ key: key, value: value});
        }
        setDefaultAttr(changingDefaultAttr); 
    }


    function colorChange(type, color){
        nodes.forEach(element => {
            if(element.type === type){
                element.color = color.hex;
            }
        });
    }

    function confirmChanges(){
        let nodeColors={};
        nodes.forEach(element => {
            nodeColors[element.type] = element.color;
        });
        dispatch({type: 'CHANGE_NODES', nodeColors: nodeColors});
    }

    return (
        <>
        {status === 'loading' && (
            <div>WAITING</div>
        )}
        {status === 'success' && (
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
                            {nodes.map((prop, key) => {
                                return (
                                    <tr key={key}>
                                        <td key={key+"1"}>{prop.type}</td>
                                        <td key={key+"2"}>
                                            <MyColorPicker color={prop.color} myType={prop.type} parentChange={colorChange}/>
                                        </td>                                    
                                        <td key={key+"3"}>
                                            <DropdownButton style={{width: "100%"}}
                                                            bsStyle={"primary"}
                                                            title={mainAttr.map((val) => {                                                            
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
                                                            title={defaultAttr.map((val) => {
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