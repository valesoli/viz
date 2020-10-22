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
                Friend: "#f6b202", 
                Fan: "#ff9f88"
            },
            pathColors: ['#e41a1c','#377eb8','#4daf4a','#984ea3','#ff7f00','#ffff33']
        })
    return (  
        <VisualConfigContext.Provider value={{ visualConfig, dispatch }}>
            {props.children}
        </VisualConfigContext.Provider>
    );
}
 
export default VisualConfigContextProvider;