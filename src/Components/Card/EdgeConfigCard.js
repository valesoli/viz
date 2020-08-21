import React from 'react';
import {
    Grid,
    Table
} from "react-bootstrap";
import { Card } from "components/Card/Card.jsx";
import { theArray, tdeArray, colors } from "variables/Variables.jsx";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import MyColorPicker from "components/CustomColorPicker/MyColorPicker";
import { neo4j_config } from "variables/ConnectionVariables.jsx";
import { api_cypherQuery } from '../../Services/GraphService/graphQueryService';
import { visual_change } from 'App';


class EdgeConfigCard extends React.Component{
    constructor(props){
        super(props);

        this.state = {
            edges: tdeArray
        }

        this.edgesCallback = this.edgesCallback.bind(this);
        this.confirmChanges = this.confirmChanges.bind(this);
        this.receiveColor = this.receiveColor.bind(this);
    }

    componentDidMount(){
        api_cypherQuery("match (:Object)-[r]->(:Object) return collect(distinct type(r))", this.edgesCallback, neo4j_config);
    }

    edgesCallback(response){
        let response_table = response.results[0].data[0].row[0];
        var edges = [];
        for(var i = 0; i < response_table.length; i++){
            tdeArray[i].type = response_table[i];
            tdeArray[i].color = this.props.visual.edgeColors[response_table[i]];
            edges.push(tdeArray[i]);
        }
        this.setState({
            edges: edges
        });
    }

    receiveColor(type, color){
        this.state.edges.forEach(element => {
            if(element.type == type){
                element.color = color.hex;
            }
        });
    }

    confirmChanges(){
        let edgeColors={};
        this.state.edges.forEach(element => {
            edgeColors[element.type] = element.color;
        });
        visual_change("edgeColors", edgeColors);
    }
    
    render(){
        return(
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
                        {this.state.edges.map((prop, key) => {
                            return (
                            <tr key={key}>
                                <td key={key+"1"}>{prop.type}</td>
                                <td key={key+"2"}>{<MyColorPicker color={prop.color} myType={prop.type} parentChange={this.receiveColor}/>}</td>
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
export default EdgeConfigCard;