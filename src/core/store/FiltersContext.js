import React, { createContext, useReducer } from 'react';
import { FiltersReducer } from './FiltersReducer';

export const FiltersContext = createContext();

const FiltersContextProvider = (props) => {
    const [filters, dispatch] = useReducer(FiltersReducer, {
        nodeTypes: ["All nodes"],
        edgeTypes: ["All edges"],
        nodeLimit: 100,
        info: {
            success: true,
            description: '' 
        }
    });

    return (  
        <FiltersContext.Provider value={{ filters, dispatch}}>
            {props.children}
        </FiltersContext.Provider>
    );
}

export default FiltersContextProvider;