import axios from 'axios';
import { path } from 'd3';

export const fetchGraph = async (key, connectionConfig, visualConfig, relationshipsConfig, inputQuery, filters, interval, variables) => {
    if(!connectionConfig.connected) return null;
    let nodesResponse = await tbdgQuery(inputQuery);
    const nodesProcessed = nodesCallback(nodesResponse, relationshipsConfig);
    if(nodesProcessed == 1){
        return {info:{success:false, description:nodesResponse.data.message}};
    } else if (nodesProcessed == 2){
        return {info:{success:false, description:"No results found"}};
    }
    let attributesResponse = await neoQuery(connectionConfig, nodesProcessed.query);
    const attributesProcessed = attributesCallback(attributesResponse, nodesProcessed.nodeIds);
    let edgesProcessed;
    // if(nodesProcessed.path){
    //     edgesProcessed = nodesProcessed.baseEdges;
    // }
    // else {
        let edgesResponse = await neoQuery(connectionConfig, attributesProcessed.query);
        edgesProcessed = edgesCallback(edgesResponse);
    // }

    let pathIntervals = hasPathIntervals(nodesResponse);
    let {graph, events} = buildNodes(nodesProcessed.baseNodes, edgesProcessed, attributesProcessed.attrs, visualConfig, filters, interval, nodesProcessed.path, nodesProcessed.pathOrders, variables);
    return {info: {success: true, description: "SUCCESS"}, nodes: graph.nodes, edges: graph.edges, pathTimes:pathIntervals};
}

const neoQuery = async (connectionConfig, query) =>{
    const config = {
        method: 'post',
        url: connectionConfig.url,
        headers: { 
            "Content-type": "application/json",
            "Authorization": "Basic "+ btoa(connectionConfig.user + ':' + connectionConfig.pass),
        },
        data: {
            "statements":[{"statement":query}]
        }
    }

    let response = await axios(config);
    return response;
}

export const tbdgQuery = async (query) =>{
    const config = {
        method: 'post',
        url: "http://localhost:7000/query",
        headers: { 
            "Content-type": "application/json",
            "Data-type": "json"
        },
        data: "query="+ encodeURIComponent(query)
    }

    let response = await axios(config);
    return response;
}
let objects = [];
const nodesCallback = (response, relationshipsConfig) => {
    let data = new Map();
    let baseEdges = [];
    let nodeIds = [];
    let e;
    
    let myPaths =[];
    let isPath = false;
    if(!response.data.success) return 1;
    if(response.data.data.length == 0) return 2;
    objects = Object.keys(response.data.data[0]);        
    if(response.data.data[0][objects[0]] == undefined || response.data.data[0][objects[0]].path == undefined){        
        response.data.data.forEach(elem => {
            for(e in elem){
                var key = elem[e].id;
                if(key !== undefined && !data.has(key)){                    
                    data.set(key, [elem[e], 0]);
                    nodeIds.push(key);
                }
            }
        });
    }
    else{
        isPath = true;
        let pathColor = 0;
        var nodes = response.data.data.sort(compare);
        for(let elem in nodes){
            for(let o in objects){
                let path = nodes[elem][objects[o]].path;
                myPaths[pathColor] = [];
                //let lastNode = null;
                for(let p in path){
                    var key = path[p].id;
                    myPaths[pathColor].push(key);
                    if(key !== undefined){
                        //if(!data.has(key)){
                            data.set(key, [path[p], pathColor]);
                            nodeIds.push(key);
                        //}                                     
                        // if(lastNode != null){
                        //     let relationship = [lastNode.title,path[p].title];
                        //     baseEdges.push([[lastNode.id, key], findRelationship(relationshipsConfig, relationship), undefined, pathColor]);
                        //     lastNode = path[p];
                        // }
                        // else
                        //     lastNode = path[p];
                    }
                }
                pathColor++;
            }
        }
    }

    let query = "match (o:Object)-->(a:Attribute)-->(v:Value) where o.id in [" + nodeIds + "] return o.id, o.title, a.title, v.value, v.interval order by o.id";
    return {
        query: query,
        path: isPath,
        baseNodes: data,
        baseEdges: baseEdges,
        nodeIds: nodeIds,
        pathOrders: myPaths
    }
}

const compare = (arrayA, arrayB) => {
    //console.log(objects);
    if(arrayA[objects[0]].path.length >= arrayB[objects[0]].path.length)
        return -1;
    else
        return 1;
}

const findRelationship = (relationshipsConfig, relationship) => {
    let result = undefined;
    for(let i=0; i< relationshipsConfig.map.length; i++){
        let item = relationshipsConfig.map[i][0];
        if(item[0] == relationship[0] && item[1] == relationship[1]){
            result = relationshipsConfig.map[i][1];
            break;
        }
    }
    return result;
}

const attributesCallback = (response, nodeIds) => {
    let response_table = response.data.results[0].data;
    
    const attrsHashmap = response_table.reduce((obj, item) => {
        if(obj[item.row[0]]){
            obj[item.row[0]].attributes.push([item.row[2], item.row[3], item.row[4]]);
        } else {
            let objr = {
                id : item.row[0],
                attributes : []
            }
            objr.attributes.push([item.row[2], item.row[3], item.row[4]])
            obj[item.row[0]] = objr;
        }
        return obj;
    }, {});
        
    let query = "match (n:Object)-[r]->(m:Object) where n.id in [" + nodeIds + "] and m.id in [" + nodeIds + "] return collect([[n.id,m.id], type(r), r.interval])";

    return {
        query: query,
        attrs: attrsHashmap
    }
}

const edgesCallback = (response) => {
    let edges = response.data.results[0].data[0].row[0];
    let baseEdges = edges;
    return baseEdges;
}

export const isInInterval = (stringInterval, numInterval) => {
    if(stringInterval == undefined)
        return true;
    for(let i=0; i<stringInterval.length; i++){
        let vals = stringInterval[i].split("—");
        let normalizedPropertyInterval = normalizeInterval(vals);
        let normalizedLimitInterval = normalizeInterval(numInterval);
        // vals[0] = parseInt(vals[0])
        // if((numInterval[0] <= vals[0] && vals[0] <= numInterval[1]) || (vals[0] <= numInterval[0] && (vals[1] == "Now" || parseInt(vals[1]) >= numInterval[0]))){
        //     return true;
        // }
        // vals[0] = parseInt(vals[0])
        if((normalizedLimitInterval[0] <= normalizedPropertyInterval[0] && normalizedPropertyInterval[0] <= normalizedLimitInterval[1]) || (normalizedPropertyInterval[0] <= normalizedLimitInterval[0] && (normalizedPropertyInterval[1] == "Now" || normalizedPropertyInterval[1] >= normalizedLimitInterval[0]))){
            return true;
        }
    }
    
    return false;
}

const completer = ["-01-01 00:00","-12-31 23:59"]

export const completeString = (original, minmax) => {
    let stringLength = original.length - 4;
    return original + completer[minmax].slice(stringLength)       
}

export const normalizeInterval = (interval) => {
    //Encontrar que formato tiene
    let minInterval = Date.parse(completeString(interval[0],0))/1000;
    let maxInterval = interval[1]
    if(maxInterval != "Now"){
        maxInterval = Date.parse(completeString(interval[1],1))/1000;
    }
    return [minInterval,maxInterval];

}
export const normalizeIntervalNumeric = (interval) => {
    //Encontrar que formato tiene
    let minInterval = Date.parse(completeString(interval[0].toString(),0));
    let maxInterval = Date.parse(completeString(interval[1].toString(),1));
    return [minInterval,maxInterval];

}

const getEdgeColor = (from, to, baseNodes) => {
    let fromColor = baseNodes.get(from)[1];
    let toColor = baseNodes.get(to)[1];
    if(fromColor != toColor){
        return fromColor < toColor ? fromColor : toColor;
    }     
    return fromColor;
}

function isInVariables(variables, attribute, id) {
    if(variables != null){
        for(let i=0; i<variables.length; i++){
            if(variables[i][0] == "id"){
                if(variables[i][1] == id)
                    return true;
            }
            if (variables[i][0] == attribute[0] && variables[i][1] == attribute[1])
                return true;
            
        }
    }    
    return false;
}

const buildNodes = (baseNodes, baseEdges, attrs, visualConfig, filters, interval, isPath, pathOrders, variables) => {
    let nodes = [];
    let edges = [];
    let nodeCount = 0;
    let insertedEdges = new Map();
    for(let e of baseNodes.entries()){
        if(nodeCount < filters.nodeLimit){
            if(filters.nodeTypes[0] == "All nodes" || filters.nodeTypes.indexOf(e[1][0].title) > -1){
                if(isInInterval(e[1].interval, interval)){
                    let color = isPath ? visualConfig.pathColors[e[1][1] % visualConfig.pathColors.length] : visualConfig.nodeColors[e[1][0].title];
                    let border = "#FFFFFF";
                    let borderWidth = 1;
                    if(isInVariables(variables, attrs[e[0]].attributes[0], e[0])){
                        border = "#000000";                                        
                        borderWidth = 5;
                    }
                    nodes.push({
                        id: e[0], 
                        title: attrs[e[0]] !== undefined ? attrs[e[0]].attributes[0][1] : e[1][0].title, 
                        group: e[1].title,
                        color: {
                            background: color,
                            border: border,
                            highlight: {border: "#2B7CE9", background: color}
                        },
                        borderWidth: borderWidth
                    });
                    nodeCount++;
                }
            }
        }
    }

    let restrictedEdges = new Set();
    if(isPath){
        for(let i = 0; i<pathOrders.length; i++){
            for(let j = 0; j< pathOrders[i].length-1; j++){
                restrictedEdges.add([pathOrders[i][j], pathOrders[i][j+1]].toString());
            }
        }
    }
    if(baseEdges !== undefined){
        let i = 0;
        baseEdges.forEach(e => {
            if(filters.edgeTypes[0] == "All edges" || filters.edgeTypes.indexOf(e[1]) > -1){
                if(e[2] == undefined || isInInterval(e[2], interval)){
                    if(!isPath){
                        let color = isPath ? visualConfig.pathColors[getEdgeColor(e[0][0], e[0][1], baseNodes) % visualConfig.pathColors.length] : visualConfig.edgeColors[e[1]];                    
                        // let key1 = e[0][0] * 10000 + e[0][1];
                        // let key2 = e[0][1] * 10000 + e[0][0];
                        // if((insertedEdges.has(key1) || insertedEdges.has(key2)) && isPath){
                        //     color = visualConfig.pathColors[getEdgeColor(e[0][0], e[0][1], baseNodes) % visualConfig.pathColors.length + 1];
                        // }
                        // else{
                        //     insertedEdges.set(key1, key1);
                        //     insertedEdges.set(key2, key2);
                        // }
                        edges.push({
                            id: i, 
                            from: e[0][0], 
                            to: e[0][1],
                            title: `Type: ${e[1]} \nInterval: ${e[2].join('\n')}`,
                            color: color,
                            arrows: 'to'
                        });
                        i++;                    
                    } else {
                        if(restrictedEdges.has([e[0][0],e[0][1]].toString())){
                            let color = isPath ? visualConfig.pathColors[getEdgeColor(e[0][0], e[0][1], baseNodes) % visualConfig.pathColors.length] : visualConfig.edgeColors[e[1]];                    
                            // let key1 = e[0][0] * 10000 + e[0][1];
                            // let key2 = e[0][1] * 10000 + e[0][0];
                            // if((insertedEdges.has(key1) || insertedEdges.has(key2)) && isPath){
                            //     color = visualConfig.pathColors[getEdgeColor(e[0][0], e[0][1], baseNodes) % visualConfig.pathColors.length + 1];
                            // }
                            // else{
                            //     insertedEdges.set(key1, key1);
                            //     insertedEdges.set(key2, key2);
                            // }
                            edges.push({
                                id: i, 
                                from: e[0][0], 
                                to: e[0][1],
                                title: `Type: ${e[1]} \nInterval: ${e[2].join('\n')}`,
                                color: color,
                                arrows: 'to'
                            });
                            i++;                    
                        }
                    }

                }
            }
        });
    }

    

    return {
        graph: {
            nodes: nodes,
            edges: edges
        },
        events : {
            // selectNode: (params) => onClickUpdateSelectionVis(params.nodes[0], this.props.con_config)
        }
    };
}

const hasPathIntervals = (response) => {
    if(response.data.data[0].hasOwnProperty('path')){
        if(response.data.data[0].path.hasOwnProperty('interval')){
            let intervals = [];
            for(let aux in response.data.data[0].path.interval){
                let auxVal = response.data.data[0].path.interval[aux];
                let vals = auxVal.split("—");
                intervals.push(vals);
            }
            return intervals;
        }
        return null;
    }
    return null;
}