import React, { createContext, useReducer } from 'react';
import { RelationshipsReducer } from './RelationshipsReducer';

export const RelationshipsContext = createContext();

const RelationshipsContextProvider = (props) => {
    const [ relationshipsConfig, dispatch2 ] = useReducer(RelationshipsReducer, {
        map: [],
        info: {
            success: true,
            description: '' 
        }
    });

    return (  
        <RelationshipsContext.Provider value={{ relationshipsConfig, dispatch2 }}>
            {props.children}
        </RelationshipsContext.Provider>
    );
}
 
export default RelationshipsContextProvider;