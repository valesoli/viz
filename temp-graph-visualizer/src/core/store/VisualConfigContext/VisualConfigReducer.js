export const VisualConfigReducer = (state, action) => {
    switch(action.type){
        case 'CHANGE_NODES':
            return (
                {
                    nodeAvatars: state.nodeAvatars,
                    nodeColors: action.nodeColors,
                    edgeColors: state.edgeColors,
                    nodeMainAttrs: state.nodeMainAttrs,
                    nodeDefaultAttrs: state.nodeDefaultAttrs,
                    pathColors: state.pathColors
                }               
            );
        case 'CHANGE_EDGES':
            return (
                {
                    nodeAvatars: state.nodeAvatars,
                    nodeColors: state.nodeColors,
                    edgeColors: action.edgeColors,
                    nodeMainAttrs: state.nodeMainAttrs,
                    nodeDefaultAttrs: state.nodeDefaultAttrs,
                    pathColors: state.pathColors
                }               
            );
        case 'CHANGE_MAIN_ATTR':
            return (
                {
                    nodeAvatars: state.nodeAvatars,
                    nodeColors: state.nodeColors,
                    edgeColors: state.edgeColors,
                    nodeMainAttrs: action.nodeMainAttrs,
                    nodeDefaultAttrs: state.nodeDefaultAttrs,
                    pathColors: state.pathColors
                }               
            );
        case 'CHANGE_DEFAULT_ATTR':
            return (
                {
                    nodeAvatars: state.nodeAvatars,
                    nodeColors: state.nodeColors,
                    edgeColors: state.edgeColors, 
                    nodeMainAttrs: state.nodeMainAttrs,
                    nodeDefaultAttrs: action.nodeDefaultAttrs,
                    pathColors: state.pathColors
                }               
            );
        case 'CHANGE_ICON':
            return (
                {
                    nodeAvatars: action.nodeAvatars,
                    nodeColors: state.nodeColors,
                    edgeColors: state.edgeColors, 
                    nodeMainAttrs: state.nodeMainAttrs,
                    nodeDefaultAttrs: state.nodeDefaultAttrs,
                    pathColors: state.pathColors
                }               
            );
        default:
            return state;
    }
}