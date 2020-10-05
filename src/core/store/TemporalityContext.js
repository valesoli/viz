import React, { createContext, useState } from 'react';

export const TemporalityContext = createContext();

//@deprecated
const TemporalityContextProvider = (props) => {
    const [ minDate, setMinDate ] = useState(1900);
    const [ maxDate, setMaxDate ] = useState(2000);
    const [ interval, setInterval ] = useState([minDate,maxDate]);
    const  [ granularity, setGranularity ] = useState(1);

    return (  
        <TemporalityContext.Provider value={{ minDate, maxDate, setMinDate, setMaxDate, interval, setInterval, granularity }}>
            {props.children}
        </TemporalityContext.Provider>
    );
}
 
export default TemporalityContextProvider;