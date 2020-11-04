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
            pathColors: ["#3366cc","#dc3912","#ff9900","#109618","#990099","#0099c6","#dd4477","#66aa00","#b82e2e","#316395","#3366cc","#994499","#22aa99","#aaaa11","#6633cc","#e67300","#8b0707","#651067","#329262","#5574a6","#3b3eac","#b77322","#16d620","#b91383","#f4359e","#9c5935","#a9c413","#2a778d","#668d1c","#bea413","#0c5922","#743411"],
        })
    return (  
        <VisualConfigContext.Provider value={{ visualConfig, dispatch }}>
            {props.children}
        </VisualConfigContext.Provider>
    );
}
 
export default VisualConfigContextProvider;