export const VisualConfigReducer = (state, action) => {
    switch(action.type){
        case 'CHANGE_NODES':
            return (
                {
                    nodeAvatars: state.nodeAvatars,
                    nodeColors: action.nodeColors,
                    edgeColors: state.edgeColors 
                }               
            );
        case 'CHANGE_EDGES':
            return (
                {
                    nodeAvatars: state.nodeAvatars,
                    nodeColors: state.nodeColors,
                    edgeColors: action.edgeColors 
                }               
            );
        default:
            return state;
    }
}