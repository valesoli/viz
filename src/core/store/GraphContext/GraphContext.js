import React, { createContext, useContext, useEffect, useReducer, useState } from 'react';
import { useQuery } from 'react-query';
import { ConnectionConfigContext } from 'core/store/ConnectionConfigContext/ConnectionConfigContext';
import { GraphReducer } from 'core/store/GraphContext/GraphReducer';
import { fetchGraph } from "core/services/graphBuildingService";
import { VisualConfigContext } from 'core/store/VisualConfigContext/VisualConfigContext';
import { RelationshipsContext } from 'core/store/RelationshipsContext/RelationshipsContext';
import { FiltersContext } from 'core/store/FiltersContext/FiltersContext';

export const GraphContext = createContext();

const GraphContextProvider = (props) => {
    const [ query, setQuery ] = useState("");
    const [ userQuery, setUserQuery ] = useState("");
    const [ variables, setVariables ] = useState();
    const { connectionConfig } = useContext(ConnectionConfigContext);
    const { visualConfig } = useContext(VisualConfigContext);
    const { relationshipsConfig } = useContext(RelationshipsContext);
    const { filters } = useContext(FiltersContext);

    const [ dateExtremes, setDateExtremes ] = useState(["1900","2000"])
    const [ interval, setInterval ] = useState([dateExtremes[0],dateExtremes[1]]);
    const  [ granularity, setGranularity ] = useState(3);

    const [queryFilters, setQueryFilters] = useState({});

    const [ pathTimes, setPathTimes] = useState([]);

    const [ graph, dispatch ] = useReducer(GraphReducer,
        {
            nodes: [
                { id: 1, label: "Node 1", title: "node 1 tootip text" },
                { id: 2, label: "Node 2", title: "node 2 tootip text" },
                { id: 3, label: "Node 3", title: "node 3 tootip text" },
                { id: 4, label: "Node 4", title: "node 4 tootip text" },
                { id: 5, label: "Node 5", title: "node 5 tootip text" }
            ],
            edges: [
                { from: 1, to: 2 },
                { from: 1, to: 3 },
                { from: 2, to: 4 },
                { from: 2, to: 5 }
            ],
            info: {
                success: true,
                description: '' 
            } 
        });

    const updateGraph = (data) => {
        if(data == null) return;
        if(data.info.success){
            dispatch({type:"UPDATE_GRAPH", graph:data});
            setPathTimes(data.pathTimes);
        } else {
            dispatch({type:"ERROR", error: data.info.description});
        }
    }

    useEffect( () => {
        async function fetchData(connectionConfig, visualConfig,relationshipsConfig, query, filters, interval, variables){
            let data = await fetchGraph("graph", connectionConfig, visualConfig, relationshipsConfig, query, filters, interval, variables);
            console.log(data);
            updateGraph(data);
        }
        fetchData(connectionConfig, visualConfig, relationshipsConfig, query, filters, interval, variables);
    }, [query]);

    // const { data, status } = useQuery(["graph", connectionConfig, visualConfig, relationshipsConfig, query, filters, interval, variables], fetchGraph, {
    //     onSuccess: updateGraph
    // });

    const setOneDateExtreme = (which, val) => {
        let newDateExtremes = dateExtremes;
        newDateExtremes[which] = val;
        if(which == 0){
            if(newDateExtremes[0] > interval[0]){
                setInterval([newDateExtremes[0], interval[1]]);
            } else if(newDateExtremes[0] > interval[1]) {
                setInterval([interval[0], newDateExtremes[1]]);
            }
        } else {
            if(newDateExtremes[1] < interval[1]){
                setInterval([interval[0], newDateExtremes[1]]);
            } else if(newDateExtremes[1] < interval[0]){
                setInterval([newDateExtremes[0], interval[1]]);
            }
        }
        setDateExtremes(newDateExtremes);
    }
    
    const startingQueryBuilder = (filters) => {
        //ToDo: Acomodar todo en un único loop
        let variables = [];
        let types = [];
        let values = [];
        let newQuery = '';
        let i =0;
        for (let [key, value] of Object.entries(filters)) {
            variables.push('a'+i);
            types.push(key);
            let auxValues =[];
            for(let val in value){
                auxValues.push(value[val].value);
            }
            values.push(auxValues);
            // console.log(key + ' ' + value); 
            i++;
        }
        let formattedTypes =[];
        for(let j=0; j< variables.length;j++){
            formattedTypes.push('('+variables[j]+':'+types[j]+')');
        }
        newQuery += 'SELECT ' + variables.toString() + '\n';
        newQuery += 'MATCH '+ formattedTypes.toString() + '\n';
        let whereClause = 'WHERE ';
        let atLeastOne = false;
        for(let i=0; i< values.length; i++){
            if(values[i].length != 0 && values[i] != 0){
                atLeastOne = true;
                let attributeName = visualConfig.nodeMainAttrs[types[i]];
                values[i].forEach(element => {
                    whereClause += variables[i]+'.'+attributeName + ' = \'' +element+ '\' or ';
                });
            }
        }
        if(atLeastOne){
            whereClause = whereClause.slice(0,-3);
            newQuery += whereClause;
        }
        setVariables(getVariables(newQuery));
        setUserQuery(newQuery);
        setQuery(newQuery);
    }

    const getVariables = (query) => {
        const regex = /\s?(\w+.?\w+|\w+\[\w+\])\s?=\s?('[\w\s-._]+'|\d+)/g;
        const regex2 = /(\w+).?\[?(\w+)\]?\s?=\s?('[\w\s-._]+'|\d+)/;
        const answer = query.match(regex);
        let match, object, attribute, value;
        let variables = [];
        if(answer !== null){
        for(let i=0; i<answer.length; i++){
            match = answer[i].match(regex2);
            object = match[1];
            attribute = match[2];
            value = match[3].replaceAll("'", "");
            variables.push([attribute, value]);
        }
        }
        return variables;
    }


    const setOneQueryFilter = (type, value) => {
        let newQueryFilters = queryFilters;
        if(value == undefined){
            newQueryFilters[type] = 0;
        } else {
            newQueryFilters[type] = value;
        }
        setQueryFilters(newQueryFilters);

        //Build Query
        startingQueryBuilder(queryFilters);
        // if(queryFilters.hasOwnProperty(type)){
        //     if(queryFilters[type].has(value.value)){
        //         queryFilters[type].delete(value.value);
        //     } else {
        //         queryFilters[type].add(value.value);
        //     }
        // } else {
        //     queryFilters[type] = new Set([value.value]);
        // }
    }

    return (  
        <GraphContext.Provider value={{ graph, dispatch, query, setQuery, userQuery, setUserQuery, variables, setVariables, dateExtremes, setOneDateExtreme, interval, setInterval, granularity, setGranularity, queryFilters, setOneQueryFilter, pathTimes}}>
            {props.children}
        </GraphContext.Provider>
    );
}
 
export default GraphContextProvider;