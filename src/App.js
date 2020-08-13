import React from 'react';
import TempGraphPlatform from "layouts/TempGraphPlatform.jsx";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";

export function connect(){
    this.setState({connection_config: {connected: true}});
    console.log("conectamos");
}

class App extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            connection_config:{
                connected: false
            }
        }
        connect = connect.bind(this);
    }
    
    render(){
        return(
            <BrowserRouter>
                <Switch>
                <Route path="/platform" render={props => <TempGraphPlatform {...props} connection={this.state.connection_config} />} />
                <Redirect from="/" to="/platform/visualizer" />
                </Switch>
            </BrowserRouter>
        );
    }
}
export default App;