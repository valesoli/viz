import React from 'react';
import "assets/css/mycss.css";

import { Card } from "components/Card/Card.jsx";
import { Table, DropdownButton, MenuItem } from 'react-bootstrap';

class GranularityConfigCard extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            selected: "Years"
        }
    }
    selection(sel){
        this.setState({
            selected:sel
        })
    }
    render(){
        return(
            <div className='card'>
                <div className='card-block'>
                    <div className='row'>
                        <div className='col-md-9'>
                            <h4 className='title' style={{paddingLeft: "12px",paddingTop: "4px"}}>
                                Temporal Granularity
                            </h4>
                        </div>
                        <div className='col-md-3'>
                            <DropdownButton
                                style={{width: "100%"}}
                                bsStyle={"primary"}
                                title={this.state.selected}
                                key={1}
                                id={`dropdown-basic`}
                                >
                                <MenuItem eventKey="1" onSelect={()=> this.selection("Seconds")}>Seconds</MenuItem>
                                <MenuItem eventKey="2" onSelect={()=> this.selection("Minutes")}>Minutes</MenuItem>
                                <MenuItem eventKey="3" onSelect={()=> this.selection("Hours")}>Hours</MenuItem>
                                <MenuItem eventKey="4" onSelect={()=> this.selection("Years")}>Years</MenuItem>
                            </DropdownButton>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
export default GranularityConfigCard;