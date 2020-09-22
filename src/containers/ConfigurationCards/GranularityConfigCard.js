import React, { useState } from 'react';
import "assets/css/mycss.css";

import { DropdownButton, MenuItem } from 'react-bootstrap';

const GranularityConfigCard = () => {
    const [ selected, setSelected ] = useState('Years');

    return (
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
                            title={selected}
                            key={1}
                            id={`dropdown-basic`}
                            >
                            <MenuItem eventKey="1" onSelect={()=> setSelected("Seconds")}>Seconds</MenuItem>
                            <MenuItem eventKey="2" onSelect={()=> setSelected("Minutes")}>Minutes</MenuItem>
                            <MenuItem eventKey="3" onSelect={()=> setSelected("Hours")}>Hours</MenuItem>
                            <MenuItem eventKey="4" onSelect={()=> setSelected("Years")}>Years</MenuItem>
                        </DropdownButton>
                    </div>
                </div>
            </div>
        </div>
    );
}
 
export default GranularityConfigCard;