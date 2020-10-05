export const GraphReducer = (state, action) => {
    switch(action.type){
        case 'UPDATE_NODES':
            return (
                {
                    nodes: action.nodes,
                    edges: state.edges
                }               
            );
            break;
        case 'UPDATE_EDGES':
            return (
                {
                    nodes: state.nodes,
                    edges: action.edges,
                    info: {
                       success: true,
                       description: 'SUCCESS' 
                   }
                }               
            );
            break;
        case 'UPDATE_GRAPH':
            return (
                {
                    nodes: action.graph.nodes,
                    edges: action.graph.edges,
                    info: {
                       success: true,
                       description: 'SUCCESS' 
                   }
                }               
            );
            break;
       case 'ERROR':
           return (
               {
                   nodes: state.nodes,
                   edges: state.edges,
                   info: {
                       success: false,
                       description: action.error
                   } 
               }
           );
           break; 
        default:
            return state;
    }
}