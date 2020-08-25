import React from 'react';
import TempGraphPlatform from "layouts/TempGraphPlatform.jsx";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";
import AdminNavbar from "components/Navbars/AdminNavbar";
import Dashboard from 'views/Dashboard';
import Configuration from 'views/Configuration';

export function connect(connection_values){
    //TODO: Hacer un fetch a la base para verificar que la información de conexión es correcta y recibir los valores de 
    //min y max date, el resto de las cosas seran por default
    this.setState({
        connection_config: {connected: true, neo4j_config: connection_values},
        temporality: {
            minDate: 1900,
            maxDate: 2000,
            granularity: 1,
            shouldHaveTextInput: false
        },
        visual: {
            nodeColors: {
                Person: "#33cccc", 
                City: "#f6ecd2", 
                Brand: "#ff9f88"
            },
            nodeAvatars: {
                Person: "pe-7s-users", 
                City: "pe-7s-world", 
                Brand: "pe-7s-cart"
            },
            edgeColors: {
                LivedIn: "#33cccc", 
                Friend: "#f6ecd2", 
                Fan: "#ff9f88"
            }
        }
    });
}

export function visual_change(attributeChange, vis_config){
    let visual = this.state.visual;
    visual[attributeChange] = vis_config
    this.setState({
        visual
    });
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
            
            {/* <div id="main-panel" className="main-panel" ref="mainPanel">
                <AdminNavbar/>
                <Switch>
                    <Route path="/visualizer">
                        <Dashboard connection={this.state.connection_config} visual={this.state.visual}/>
                    </Route>
                    <Route path="/config">
                        <Configuration connection={this.state.connection_config} visual={this.state.visual}/>
                    </Route>
                </Switch>
            </div> */}
                <Switch>
                <Route path="/platform" render={props => <TempGraphPlatform {...props}  
                                                            connection={this.state.connection_config} 
                                                            visual={this.state.visual} 
                                                            temporality={this.state.temporality}
                                                            />
                                                } />
                <Redirect from="/" to="/platform/visualizer" />
                </Switch>
            </BrowserRouter>
        );
    }
}
export default App;