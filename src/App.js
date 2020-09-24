import React from 'react';
import TempGraphPlatform from "views/TempGraphPlatform.jsx";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";
import ConnectionConfigContextProvider from 'core/store/ConnectionConfigContext';
import VisualConfigContextProvider from 'core/store/VisualConfigContext';
import SelectedNodeContextProvider from 'core/store/SelectedNodeContext';

export function connect(connection_values){
    //TODO: Hacer un fetch a la base para verificar que la información de conexión es correcta y recibir los valores de 
    //min y max date, el resto de las cosas seran por default
    this.setState({
        connection_config: {connected: true, neo4j_config: connection_values},
        temporality: {
            minDate: 1900,
            maxDate: 2000,
            currentLow: 1900,
            currentHigh:2000,
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
        },
        query: query
    });
}

export function visual_change(attributeChange, vis_config){
    let visual = this.state.visual;
    visual[attributeChange] = vis_config
    this.setState({
        visual
    });
}

export function applyFilters(query){
    this.setState({
      query: query
    });
}

export function applyFilterAndSave(query, low, high){
    let new_temp = this.state.temporality;
    new_temp.currentLow = low;
    new_temp.currentHigh = high;
    this.setState({
      query: query,
      temporality: new_temp
    });
}

const query = "select n, f, m match (n) - [f:Friend] -> (m)";

class App extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            connection_config:{
                connected: false
            },
            visual: null,
            query: null
        }
        connect = connect.bind(this);
        visual_change = visual_change.bind(this);
        applyFilters = applyFilters.bind(this);
        applyFilterAndSave = applyFilterAndSave.bind(this);
    }
    
    render(){
        return(
            <ConnectionConfigContextProvider> 
            <VisualConfigContextProvider> 
            <SelectedNodeContextProvider>
                <BrowserRouter>
                    <Switch>
                            <Route path="/platform" render={props => <TempGraphPlatform {...props}
                                connection={this.state.connection_config}
                                visual={this.state.visual}
                                temporality={this.state.temporality}
                                query={this.state.query}
                            />
                            } />
                            <Redirect from="/" to="/platform/visualizer" />
                    </Switch>
                </BrowserRouter>
            </SelectedNodeContextProvider>
            </VisualConfigContextProvider>
            </ConnectionConfigContextProvider>
        );
    }
}
export default App;