export const ConnectionConfigReducer = (state, action) => {
    switch(action.type){
        case 'CHANGE_CONFIG':
            // TODO: Acá tengo que hacer que se fije si puede conectarse a la base antes de
            // ponerle que está conectado.
            return (
                {
                    connected: true,
                    url: action.config.url,
                    user: action.config.user,
                    pass: action.config.pass
                }               
            );
            break;
        case 'CONNECTION_FAILED':
            return (
                {
                    connected: false,
                    url: state.url,
                    user: state.user,
                    pass: state.pass
                }
            );
            break;
        default:
            return state;
    }
}