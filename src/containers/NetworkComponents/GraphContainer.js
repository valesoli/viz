import React, { useContext } from "react";
import Graph from "react-graph-vis";
import { GraphContext } from "core/store/GraphContext";
import { ConnectionConfigContext } from "core/store/ConnectionConfigContext";
import { VisualConfigContext } from "core/store/VisualConfigContext";
import { SelectedNodeContext } from "core/store/SelectedNodeContext";
import { Jumbotron } from "react-bootstrap";

const GraphContainer = (props) => {
    const { setSelectedNodeId } = useContext(SelectedNodeContext);
    // I need some way to store the nodes and edges
    // and only have access to what i want to show
    const { graph } = useContext(GraphContext);
    const options = {
        nodes: {
            shape: "dot"
        },
        physics: {
            forceAtlas2Based: {
                gravitationalConstant: -26,
                centralGravity: 0.005,
                springLength: 230,
                springConstant: 0.18,
            },
            maxVelocity: 146,
            solver: "forceAtlas2Based",
            timestep: 0.35,
            stabilization: { iterations: 150 },
            },
        height: '500px'
    }
    // Here we have the function that must be on each node. We have to think if its
    // something that will be constant or that will change along the work
    const events = {
        selectNode: (params) => setSelectedNodeId(params.nodes[0])
    }
    return (
        <>
            {graph.info.success ? 
                <Graph
                    key={events}
                    graph={graph}
                    options={options}
                    events={events}
                    getNetwork={network => {
                    //  if you want access to vis.js network api you can set the state in a parent component using this property
                    }}
                />
            :
                <div className="callout">
                    <h4 className="text-info">Error!</h4>
                    <p>
                        {graph.info.description}
                    </p>
                </div>
            }
        </>
    );
}
 
export default GraphContainer;

// class GraphContainer extends React.Component {
//     constructor(props){
//         super(props);
//         let con_config = this.props.con_config;
//         let query = this.props.query;
//         this.state = {
//             con_config: con_config,
//             graph: {
//                 nodes: [
                  
//                 ],
//                 edges: [
                  
//                 ]
//             },
//             events : {
//                 // selectNode: (event) => onClickUpdateSelectionVis(22, con_config)
//             },
//             query: query
//         }

//         //Binding
//         this.nodesCallback = this.nodesCallback.bind(this);
//         this.edgesCallback = this.edgesCallback.bind(this);
//         this.attributesCallback = this.attributesCallback.bind(this);
//         this.buildNodes = this.buildNodes.bind(this);
//     }
    
//     componentDidMount(){
//         api_tbdgQuery(this.state.query, this.nodesCallback);
//     }

//     componentWillReceiveProps(props){
//         api_tbdgQuery(props.query, this.nodesCallback);
//     }

//     nodesCallback(response){
//         var data = new Map();
//         var x;
//         response.data.forEach(elem => {
//             for(x in elem){
//                 var key = elem[x].id;
//                 if(key !== undefined && !data.has(key))
//                     data.set(key, elem[x]);
//             }
//         });
        
//         var nodeIds = [];
//         for(let id of data.keys()){
//             nodeIds.push(id);
//         }

//         var query = "match (o:Object)-->(a:Attribute)-->(v:Value) where o.id in [" + nodeIds  + "] return o.id, o.title, a.title, v.value, v.interval order by o.id";

//         this.setState({
//             baseNodes: data,
//             nodeIds: nodeIds
//         }, () => { api_cypherQuery(query, this.attributesCallback, this.props.con_config); });
//     }

//     attributesCallback(response){
//         let response_table = response.results[0].data;
        
//         const attrsHashmap = response_table.reduce((obj, item) => {
//             if(obj[item.row[0]]){
//                 obj[item.row[0]].attributes.push([item.row[2], item.row[3], item.row[4]]);
//             } else {
//                 let objr = {
//                     id : item.row[0],
//                     attributes : []
//                 }
//                 objr.attributes.push([item.row[2], item.row[3], item.row[4]])
//                 obj[item.row[0]] = objr;
//             }
//             return obj;
//           }, {});
          
//         var query = "match (n:Object)-[r]->(m:Object) where n.id in [" + this.state.nodeIds + "] and m.id in [" + this.state.nodeIds + "] return collect([[n.id,m.id], type(r)])";

//         this.setState({
//             attrs: attrsHashmap
//         }, api_cypherQuery(query, this.edgesCallback, this.props.con_config));
//     }

//     edgesCallback(response){
//         var edges = response.results[0].data[0].row[0];
//         //console.log(response.results[0].data);        
//         this.setState({
//             baseEdges: edges
//         }, this.buildNodes);
//     }

//     buildNodes(){
//         let nodes = [];
//         let edges = [];
//         var attrs = this.state.attrs;
        
//         var baseNodes = this.state.baseNodes;
//         for(let e of baseNodes.entries()){
//             nodes.push({
//                 id: e[0], 
//                 title: attrs[e[0]] !== undefined ? attrs[e[0]].attributes[0][1] : e[1].title, 
//                 group: e[1].title,
//                 color: this.props.visual.nodeColors[e[1].title]
//             });
//         }
//         console.log(nodes);

//         if(this.state.baseEdges !== undefined){
//             this.state.baseEdges.forEach(e => {
//                 edges.push(
//                     {
//                         from: e[0][0], 
//                         to: e[0][1],
//                         title: e[1],
//                         color: this.props.visual.edgeColors[e[1]],
//                         arrows: 'to'
//                     })
//             });
//         }

//         this.setState({
//             graph: {
//                 nodes: nodes,
//                 edges: edges
//             },
//             events : {
//                 // selectNode: (params) => onClickUpdateSelectionVis(params.nodes[0], this.props.con_config)
//             }
//         });
//     }
   
//     render(){
//         return (
//             <Graph
//                 key={this.state.events}
//               graph={this.state.graph}
//               options={this.state.options}
//               events={this.state.events}
//               getNetwork={network => {
//                 //  if you want access to vis.js network api you can set the state in a parent component using this property
//               }}
//             />
//         );
//     }
// }
// export default GraphContainer;