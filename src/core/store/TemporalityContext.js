import React, { createContext, useState } from 'react';

export const TemporalityContext = createContext();

const TemporalityContextProvider = (props) => {
    const [ minDate, setMinDate ] = useState(1900);
    const [ maxDate, setMaxDate ] = useState(2000);
    const  [ granularity, setGranularity ] = useState(1);
    const [ shouldHaveTextInput, setShouldHaveTextInput ] = useState(false);

    return (  
        <TemporalityContext.Provider value={{ minDate, maxDate, granularity, shouldHaveTextInput }}>
            {props.children}
        </TemporalityContext.Provider>
    );
}
 
export default TemporalityContextProvider;