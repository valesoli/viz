import React from 'react';
import { Table, DropdownButton, MenuItem} from "react-bootstrap";
import { Card } from "components/Card/Card.jsx";
import { thArray, tdArray } from "core/variables/Variables.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import MyColorPicker from "containers/CustomColorPicker/MyColorPicker";
import { visual_change } from "App";
import { neo4j_config } from "core/variables/ConnectionVariables.jsx";
import { api_cypherQuery } from 'core/services/graphQueryService';

class NodeConfigCard extends React.Component{  
    constructor(props){
        super(props);
        this.state = {
            nodes: tdArray,
            mainAttr: [{ key: "0", value:"Select"}],
            defaultAttr: [{ key: "0", value:"Select"}]
        }

        this.nodesCallback = this.nodesCallback.bind(this);
        this.confirmChanges = this.confirmChanges.bind(this);
        this.colorChange = this.colorChange.bind(this);
    }

    componentDidMount(){
        api_cypherQuery("match (o:Object)-->(a:Attribute)-->(v:Value) return o.title, collect(distinct a.title) order by o.title", this.nodesCallback, neo4j_config);
    }

    nodesCallback(response) {
        let response_table = response.results[0].data;        
        var nodes = [];
        var mainAttr = [];
        var defaultAttr = [];
        for(var i = 0; i < response_table.length; i++){
            tdArray[i].type = response_table[i].row[0];
            tdArray[i].color = this.props.visual.nodeColors[response_table[i].row[0]];
            tdArray[i].attribute = response_table[i].row[1][0];
            tdArray[i].default = response_table[i].row[1][0];
            tdArray[i].allAttrs = response_table[i].row[1];
            nodes.push(tdArray[i]);
            mainAttr.push({key: i, value: tdArray[i].attribute});
            defaultAttr.push({key: i, value: tdArray[i].default});
        }
        this.setState({
            nodes: nodes,
            mainAttr: mainAttr,
            defaultAttr: defaultAttr
        });
    }

    changeValueMain(key, value) {
        var found = false;
        var mainAttr = this.state.mainAttr;
        for(var i = 0; i < mainAttr.length; i++){
            if(mainAttr[i].key === key){
                mainAttr[i].value = value;
                found = true;
                break;
            }
        }
        if(!found){
            mainAttr.push({ key: key, value: value});
        }
        this.setState({mainAttr: mainAttr})
    }

    changeValueDefault(key, value) {
        var found = false;
        var defaultAttr = this.state.defaultAttr;
        for(var i = 0; i < defaultAttr.length; i++){
            if(defaultAttr[i].key === key){
                defaultAttr[i].value = value;
                found = true;
                break;
            }
        }
        if(!found){
            defaultAttr.push({ key: key, value: value});
        }
        this.setState({defaultAttr: defaultAttr})
    }

    confirmChanges(){
        let nodeColors={};
        this.state.nodes.forEach(element => {
            nodeColors[element.type] = element.color;
        });
        visual_change("nodeColors", nodeColors);
    }

    colorChange(type, color){
        this.state.nodes.forEach(element => {
            if(element.type === type){
                element.color = color.hex;
            }
        });
    }

    render(){
        return(
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
                            {this.state.nodes.map((prop, key) => {
                                return (
                                    <tr key={key}>
                                        <td key={key+"1"}>{prop.type}</td>
                                        <td key={key+"2"}>
                                            <MyColorPicker color={prop.color} myType={prop.type} parentChange={this.colorChange}/>
                                        </td>                                    
                                        <td key={key+"3"}>
                                            <DropdownButton style={{width: "100%"}}
                                                            bsStyle={"primary"}
                                                            title={this.state.mainAttr.map((val) => {                                                            
                                                                if(val.key === key)
                                                                    return val.value;
                                                            })}
                                                            id={`dropdown-basic`}>
                                                            {prop.allAttrs.map((prop, key2) => {
                                                                return (
                                                                <MenuItem key={key2} eventKey={key2} onClick={() => this.changeValueMain(key, prop)}>{prop}</MenuItem>
                                                                );
                                                            })}
                                            </DropdownButton>
                                        </td>
                                        <td key={key+"4"}>
                                            <DropdownButton style={{width: "100%"}}
                                                            bsStyle={"primary"}
                                                            title={this.state.defaultAttr.map((val) => {
                                                                if(val.key === key)
                                                                    return val.value;
                                                            })}
                                                            id={`dropdown-basic`}>
                                                            {prop.allAttrs.map((prop, key2) => {
                                                                return (
                                                                <MenuItem key={key2} eventKey={key2} onClick={() => this.changeValueDefault(key, prop)}>{prop}</MenuItem>
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
                        <Button bsStyle="info" pullRight fill onClick={this.confirmChanges}>
                            Confirm Changes
                        </Button>
                    </div>
                }
            />
        );
    }
}
export default NodeConfigCard;