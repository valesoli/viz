import axios from 'axios';

export const fetchGraph = async (key, connectionConfig, visualConfig, inputQuery, filters) => {
    if(!connectionConfig.connected) return null;
    let nodesResponse = await tbdgQuery(inputQuery);
    const nodesProcessed = nodesCallback(nodesResponse);
    if(nodesProcessed == 1){
        return {info:{success:false, description:nodesResponse.data.message}};
    } else if (nodesProcessed == 2){
        return {info:{success:false, description:"No results found"}};
    }
    let attributesResponse = await neoQuery(connectionConfig, nodesProcessed.query);
    const attributesProcessed = attributesCallback(attributesResponse, nodesProcessed.nodeIds);
    let edgesResponse = await neoQuery(connectionConfig, attributesProcessed.query);
    const edgesProcessed = edgesCallback(edgesResponse);


    let {graph, events} = buildNodes(nodesProcessed.baseNodes, edgesProcessed, attributesProcessed.attrs, visualConfig, filters);
    return {info: {success: true, description: "SUCCESS"}, nodes: graph.nodes, edges: graph.edges};
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

const tbdgQuery = async (query) =>{
    const config = {
        method: 'post',
        url: "http://localhost:7000/query",
        headers: { 
            "Content-type": "application/json",
            "Data-type": "json"
        },
        data: "query="+ encodeURI(query)
    }

    let response = await axios(config);
    return response;
}

const nodesCallback = (response) => {
    let data = new Map();
    let x;
    if(!response.data.success) return 1;
    if(response.data.data.length == 0) return 2;
    response.data.data.forEach(elem => {
        for(x in elem){
            var key = elem[x].id;
            if(key !== undefined && !data.has(key))
                data.set(key, elem[x]);
        }
    });
    
    let nodeIds = [];
    for(let id of data.keys()){
        nodeIds.push(id);
    }

    let query = "match (o:Object)-->(a:Attribute)-->(v:Value) where o.id in [" + nodeIds  + "] return o.id, o.title, a.title, v.value, v.interval order by o.id";
    return {
        query: query,
        baseNodes: data,
        nodeIds: nodeIds
    }
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
        
    let query = "match (n:Object)-[r]->(m:Object) where n.id in [" + nodeIds + "] and m.id in [" + nodeIds + "] return collect([[n.id,m.id], type(r)])";

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

const buildNodes = (baseNodes, baseEdges, attrs, visualConfig, filters) => {
    let nodes = [];
    let edges = [];
    let nodeCount = 0;
    for(let e of baseNodes.entries()){
        if(nodeCount < filters.nodeLimit){
            if(filters.nodeTypes[0] == "All nodes" || filters.nodeTypes.indexOf(e[1].title) > -1){
                nodes.push({
                    id: e[0], 
                    title: attrs[e[0]] !== undefined ? attrs[e[0]].attributes[0][1] : e[1].title, 
                    group: e[1].title,
                    color: visualConfig.nodeColors[e[1].title]
                });
                nodeCount++;
            }
        }
    }

    if(baseEdges !== undefined){
        baseEdges.forEach(e => {
            if(filters.edgeTypes[0] == "All edges" || filters.edgeTypes.indexOf(e[1]) > -1){
                edges.push({
                    from: e[0][0], 
                    to: e[0][1],
                    title: e[1],
                    color: visualConfig.edgeColors[e[1]],
                    arrows: 'to'
                });
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