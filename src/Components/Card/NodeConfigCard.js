import React from 'react';
import {
    Grid,
    Table,
    DropdownButton, 
    MenuItem 
} from "react-bootstrap";
import { Card } from "components/Card/Card.jsx";
import { thArray, tdArray } from "variables/Variables.jsx";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import MyColorPicker from "components/CustomColorPicker/MyColorPicker";

import { visual_change } from "App";

class NodeConfigCard extends React.Component{
    constructor(props){
        super(props);
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
                        {tdArray.map((prop, key) => {
                            return (
                            <tr key={key}>
                                {prop.map((prop, key) => {
                                    if(prop == "Button"){
                                        return (<td key={key}><DropdownButton
                                                style={{width: "100%"}}
                                                bsStyle={"primary"}
                                                title={"logi"}
                                                key={1}
                                                id={`dropdown-basic`}
                                                >
                                                <MenuItem eventKey="1">Action</MenuItem>
                                                <MenuItem eventKey="2">Another action</MenuItem>
                                                <MenuItem eventKey="3" active>
                                                    Active Item
                                                </MenuItem>
                                                <MenuItem divider />
                                                <MenuItem eventKey="4">Separated link</MenuItem>
                                            </DropdownButton></td>
                                        );
                                    } else if(prop == "Color") {
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