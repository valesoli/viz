export const GraphReducer = (state, action) => {
    switch(action.type){
        case 'UPDATE_NODES':
            return (
                {
                    nodes: action.nodes,
                    edges: state.edges
                }               
            );
        case 'UPDATE_EDGES':
            return (
                {
                    nodes: state.nodes,
                    edges: action.edges
                }               
            );
        case 'UPDATE_GRAPH':
            return (
                {
                    nodes: action.graph.nodes,
                    edges: action.graph.edges
                }               
            );
        default:
            return state;
    }
}