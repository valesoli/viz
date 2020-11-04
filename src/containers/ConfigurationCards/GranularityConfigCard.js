import React, { useState, useContext, useEffect } from 'react';
import "assets/css/mycss.css";

import { DropdownButton, MenuItem } from 'react-bootstrap';
import { GraphContext } from 'core/store/GraphContext/GraphContext';

import { completeString } from 'core/services/graphBuildingService';

const GranularityConfigCard = () => {
    const selectionValues = ["Hours", "Days", "Months", "Years"];
    const { granularity, setGranularity, setOneDateExtreme, dateExtremes } = useContext(GraphContext);
    const [ selected, setSelected ] = useState(selectionValues[granularity]);
    const [ localExtremes, setLocalExtremes ] = useState([]);
    
    useEffect(() => {
        setLocalExtremes(dateExtremes);
    }, [])

    const select = (val) => {
        setSelected(selectionValues[val]);
        setGranularity(val);
    }

    const updateMinMax = (value, minmax) => {
        let complete = completeString(value, minmax);
        if(complete.length == 16){
            setOneDateExtreme(minmax, complete);
        }
    }
        
    return (
        <div className='card'>
            <div className='card-block'>
                <div className='row'>
                    <div className='col-md-3'>
                        <h4 className='title' style={{paddingLeft: "12px",paddingTop: "4px"}}>
                            Temporal Granularity
                        </h4>
                    </div>
                    <div className='col-md-3'>
                        <h4 className='title' style={{paddingLeft: "12px",paddingTop: "2px"}}>
                        Min: 
                        <input type="text" placeholder={localExtremes[0]} style={{width:"75%"}} onChange={(e) => updateMinMax(e.target.value, 0)}>
                        
                        </input>
                        </h4>
                    </div>
                    <div className='col-md-3'>
                        <h4 className='title' style={{paddingLeft: "12px",paddingTop: "2px"}}>
                                Max: 
                        <input type="text" placeholder={localExtremes[1]} style={{width:"75%"}} onChange={(e) => updateMinMax(e.target.value, 1)}>

                            </input>
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
                            <MenuItem eventKey="1" onSelect={()=> select(0)}>Hour</MenuItem>
                            <MenuItem eventKey="2" onSelect={()=> select(1)}>Days</MenuItem>
                            <MenuItem eventKey="3" onSelect={()=> select(2)}>Months</MenuItem>
                            <MenuItem eventKey="4" onSelect={()=> select(3)}>Years</MenuItem>
                        </DropdownButton>
                    </div>
                </div>
            </div>
        </div>
    );
}
 
export default GranularityConfigCard;