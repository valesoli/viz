import React from 'react';
import TempGraphPlatform from "layouts/TempGraphPlatform.jsx";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";

export function connect(connection_values){
    this.setState({connection_config: {connected: true, neo4j_config: connection_values}});
    console.log("conectamos");
}

export function visual_change(vis_config){
    this.setState({
        visual: {
            nodeColors: {
                "Person": "#33cccc", 
                "City": "#f6ecd2", 
                "Brand": "#ff9f88"
            },
            edgeColors: vis_config}
    });
    console.log("Cambios visuales");
}

class App extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            connection_config:{
                connected: false
            },
            visual: null
        }
        connect = connect.bind(this);
        visual_change = visual_change.bind(this);
    }
    
    render(){
        return(
            <BrowserRouter>
                <Switch>
                <Route path="/platform" render={props => <TempGraphPlatform {...props}
                                                            connection={this.state.connection_config} 
                                                            visual={this.state.visual} />
                                                } />
                <Redirect from="/" to="/platform/visualizer" />
                </Switch>
            </BrowserRouter>
        );
    }
}
export default App;