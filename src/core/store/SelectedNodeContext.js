import React, { createContext, useState } from 'react';

export const SelectedNodeContext = createContext();

const SelectedNodeContextProvider = (props) => {
    const [ selectedNodeId, setSelectedNodeId ] = useState(0);

    return (  
        <SelectedNodeContext.Provider value={{ selectedNodeId, setSelectedNodeId }}>
            {props.children}
        </SelectedNodeContext.Provider>
    );
}
 
export default SelectedNodeContextProvider;