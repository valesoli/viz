import React from 'react';
import { Grid, Table, DropdownButton, MenuItem} from "react-bootstrap";
import { Card } from "components/Card/Card.jsx";
import { thArray, tdArray, colors } from "variables/Variables.jsx";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import MyColorPicker from "components/CustomColorPicker/MyColorPicker";
import { visual_change } from "App";
import { neo4j_config } from "variables/ConnectionVariables.jsx";
import { api_cypherQuery } from '../../Services/GraphService/graphQueryService';
import { color } from 'd3';

class NodeConfigCard extends React.Component{  
    constructor(props){
        super(props);
        this.state = {
            nodes: tdArray,
            mainAttr: [{ key: "0", value:"Select"}],
            defaultAttr: [{ key: "0", value:"Select"}]
        }

        this.nodesCallback = this.nodesCallback.bind(this);
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
            tdArray[i].color = colors[i];
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
            if(mainAttr[i].key == key){
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
            if(defaultAttr[i].key == key){
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
                                console.log(prop.color);
                                return (
                                <tr key={key}>
                                    <td key={key+"1"}>{prop.type}</td>
                                    <td key={key+"2"}>{<MyColorPicker color={prop.color}/>}</td>                                    
                                    <td key={key+"3"}>
                                        <DropdownButton style={{width: "100%"}}
                                                        bsStyle={"primary"}
                                                        title={this.state.mainAttr.map((val) => {                                                            
                                                            if(val.key == key)
                                                                return val.value;
                                                        })}
                                                        id={`dropdown-basic`}>
                                                        {prop.allAttrs.map((prop, key2) => {
                                                            return (
                                                            <MenuItem eventKey={key2} onClick={() => this.changeValueMain(key, prop)}>{prop}</MenuItem>
                                                            );
                                                        })}
                                        </DropdownButton>
                                    </td>
                                    <td key={key+"4"}>
                                        <DropdownButton style={{width: "100%"}}
                                                        bsStyle={"primary"}
                                                        title={this.state.defaultAttr.map((val) => {
                                                            if(val.key == key)
                                                                return val.value;
                                                        })}
                                                        id={`dropdown-basic`}>
                                                        {prop.allAttrs.map((prop, key2) => {
                                                            return (
                                                            <MenuItem eventKey={key2} onClick={() => this.changeValueDefault(key, prop)}>{prop}</MenuItem>
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
                        <Button bsStyle="info" pullRight fill type="submit" onClick={visual_change}>
                            Confirm Changes
                        </Button>
                    </div>
                }
            />
        );
    }
}
export default NodeConfigCard;