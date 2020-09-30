export const FiltersReducer = (state, action) => {
    switch(action.type){
        case 'UPDATE_NODETYPES':
            return (
                {
                    nodeTypes: action.nodeTypes,
                    edgeTypes: state.edgeTypes,
                    nodeLimit: state.nodeLimit
                }               
            );
        case 'UPDATE_EDGETYPES':
            return (
                {
                    nodeTypes: state.nodeTypes,
                    edgeTypes: action.edgeTypes,
                    nodeLimit: state.nodeLimit,
                    info: {
                       success: true,
                       description: 'SUCCESS' 
                   }
                }               
            );
        case 'UPDATE_NODELIMIT':
            return (
                {
                    nodeTypes: state.nodeTypes,
                    edgeTypes: state.edgeTypes,
                    nodeLimit: action.nodeLimit,
                    info: {
                       success: true,
                       description: 'SUCCESS' 
                   }
                }               
            );
        case 'UPDATE_FILTERS':
            return (
                {
                    nodeTypes: action.filters.nodeTypes,
                    edgeTypes: action.filters.edgeTypes,
                    nodeLimit: action.filters.nodeLimit,
                    info: {
                       success: true,
                       description: 'SUCCESS' 
                   }
                }               
            );
       case 'ERROR':
           return (
               {
                    nodeTypes: state.nodeTypes,
                    edgeTypes: state.edgeTypes,
                    nodeLimit: state.nodeLimit,
                    info: {
                       success: false,
                       description: action.error
                   } 
               }
           );
        default:
            return state;
    }
}