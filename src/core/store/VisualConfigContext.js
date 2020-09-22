import React, { createContext, useReducer } from 'react';
import { VisualConfigReducer } from './VisualConfigReducer';

export const VisualConfigContext = createContext();

const VisualConfigContextProvider = (props) => {
    const [ visualConfig, dispatch ] = useReducer(VisualConfigReducer,
        {
            nodeColors: {
                Person: "#33cccc", 
                City: "#f6ecd2", 
                Brand: "#ff9f88"
            },
            nodeAvatars: {
                Person: "pe-7s-users", 
                City: "pe-7s-world", 
                Brand: "pe-7s-cart"
            },
            edgeColors: {
                LivedIn: "#33cccc", 
                Friend: "#f6ecd2", 
                Fan: "#ff9f88"
            }
        })
    return (  
        <VisualConfigContext.Provider value={{ visualConfig, dispatch }}>
            {props.children}
        </VisualConfigContext.Provider>
    );
}
 
export default VisualConfigContextProvider;