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
            nodeMainAttrs: {},
            nodeDefaultAttrs: {},
            pathColors: ['#a6cee3','#1f78b4','#b2df8a','#33a02c','#fb9a99','#e31a1c','#fdbf6f','#ff7f00','#cab2d6','#6a3d9a','#ffff99','#b15928'],
        })
    return (  
        <VisualConfigContext.Provider value={{ visualConfig, dispatch }}>
            {props.children}
        </VisualConfigContext.Provider>
    );
}
 
export default VisualConfigContextProvider;