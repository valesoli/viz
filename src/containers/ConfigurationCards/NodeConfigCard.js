import React, { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import { ConnectionConfigContext } from 'core/store/ConnectionConfigContext/ConnectionConfigContext';
import { VisualConfigContext } from 'core/store/VisualConfigContext/VisualConfigContext';
import { fetchNeoQuery } from 'core/services/configQueryServices';
import { Table, DropdownButton, MenuItem, Dropdown} from "react-bootstrap";
import { Card } from "components/Card/Card.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import MyColorPicker from "containers/CustomColorPicker/MyColorPicker";
import Loader from 'react-loader-spinner';

const NodeConfigCard = (props) => {
    const defaultColors = ['#e41a1c','#377eb8','#4daf4a','#984ea3','#ff7f00','#ffff33'];    
    const { connectionConfig } = useContext(ConnectionConfigContext);
    const { visualConfig, dispatch } = useContext(VisualConfigContext);
    const [ nodeInfo, setNodeInfo ] = useState(null);

    const responseFormatter = (response) => {
        let response_table = response.data.results[0].data;        
        let newNodes = [];
        let newMainAttr = [];
        let newDefaultAttr = [];
        let nodeColors = {};
        for(let i = 0; i < response_table.length; i++){
            let td = {type: '', color: '', attribute: '', default: '', allAttrs: ''};
            td.type = response_table[i].row[0];
            td.color = visualConfig.nodeColors[response_table[i].row[0]]?visualConfig.nodeColors[response_table[i].row[0]]:defaultColors[i];
            td.attribute = response_table[i].row[1][0];
            td.default = response_table[i].row[1][0];
            td.allAttrs = response_table[i].row[1];
            nodeColors[td.type] = td.color;
            newNodes.push(td);
            newMainAttr.push({key: i, value: td.attribute});
            newDefaultAttr.push({key: i, value: td.default});
        }
        setNodeInfo({nodes: newNodes, mainAttr: newMainAttr, defaultAttr: newDefaultAttr});
        dispatch({type: 'CHANGE_NODES', nodeColors: nodeColors});
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
        let nodeMainAttrs={};
        nodeInfo.nodes.forEach(element => {
            nodeMainAttrs[element.type] = element.mainAttr;
        });
        dispatch({type: 'CHANGE_MAIN_ATTR', nodeMainAttrs: nodeMainAttrs}); 
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
        let nodeDefaultAttrs={};
        nodeInfo.nodes.forEach(element => {
            nodeDefaultAttrs[element.type] = element.defaultAttr;
        });
        dispatch({type: 'CHANGE_DEFAULT_ATTR', nodeDefaultAttrs: nodeDefaultAttrs});
    }


    function colorChange(type, color){
        nodeInfo.nodes.forEach(element => {
            if(element.type === type){
                element.color = color.hex;
            }
        });
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
                title="Nodes Settings"
                ctTableResponsive
                content={
                    <Table striped hover style={{marginBottom: "0px"}}>
                        <thead>
                        <tr>
                                <th key={1}>TYPE</th>
                                <th key={2}>COLOR</th>
                                <th key={3}>MAIN ATTRIBUTE</th>
                                <th key={4}>DEFAULT</th>
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
                                                            title={nodeInfo.mainAttr[key].value}
                                                            id={`dropdown-basic`}>
                                                            {prop.allAttrs.map((prop, key2) => {
                                                                return (
                                                                <MenuItem key={key2} eventKey={key2} onClick={() => {changeValueMain(key, prop);}}>{prop}</MenuItem>
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
            />

        )}
    </>
    );
}
 
export default NodeConfigCard;