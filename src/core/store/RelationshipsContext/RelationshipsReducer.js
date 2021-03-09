export const RelationshipsReducer = (state, action) => {
    switch(action.type){
        case 'CHANGE_MAP':
            return (
                {
                    map: action.config.map,
                    info: action.config.info
                }               
            );
        default:
            return state;
    }
}