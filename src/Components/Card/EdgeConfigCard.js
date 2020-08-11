import React from 'react';
import {
    Grid,
    Table
} from "react-bootstrap";
import { Card } from "components/Card/Card.jsx";
import { theArray, tdeArray } from "variables/Variables.jsx";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import MyColorPicker from "components/CustomColorPicker/MyColorPicker";

class EdgeConfigCard extends React.Component{
    constructor(props){
        super(props);
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
                    {tdeArray.map((prop, key) => {
                        return (
                        <tr key={key}>
                            {prop.map((prop, key) => {
                                if(prop == "Color") {
                                    return(<td key={key}>{<MyColorPicker/>}</td>)
                                } else {
                                    return <td key={key}>{prop}</td>;
                                }
                            })}
                        </tr>
                        );
                    })}
                    </tbody>
                </Table>
                }
            />
        );
    }
}
export default EdgeConfigCard;