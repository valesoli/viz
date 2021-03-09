/*!
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard-react/blob/master/LICENSE.md)
* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/
import React, { Component, useContext } from "react";
import { Grid, Row, Col, Button, Jumbotron } from "react-bootstrap";
import { NavLink } from "react-router-dom";

import { Card } from "components/Card/Card.jsx";
import GraphContainer from "containers/NetworkComponents/GraphContainer";
import TempSliderContainer from "containers/TempSliderContainer/TempSliderContainer";
import FilterModule from "containers/NetworkComponents/FilterModule";
import NodeVisualizer from "containers/NetworkComponents/NodeVisualizer";
import PathTimesModule from "containers/NetworkComponents/PathTimesModule";
import QueryBox from "containers/QueryBox/QueryBox";
import { VisualConfigContext } from "core/store/VisualConfigContext/VisualConfigContext";
import { ConnectionConfigContext } from "core/store/ConnectionConfigContext/ConnectionConfigContext";

const Dashboard = (props) => {
  const { connectionConfig } = useContext(ConnectionConfigContext);
  const { visualConfig } = useContext(VisualConfigContext);

  function createLegend(json, type) {
    var legend = [];

    let colors = type === "node"?json.nodeColors:json.edgeColors;
    for (const [index, [key, value]] of Object.entries(Object.entries(colors))) {
      legend.push(
        type == "node"?
        <i className={"fa fa-circle text"} style={{color: value}} key={index} />:
        <i className={"fa text"} style={{color: value, fontSize:'15px', fontWeight:'bolder'}} key={index} >━</i>
      );
      legend.push(" ");
      legend.push(key);
    }
    return legend;
  }
  
  function buildNetworkCard(){
    let NetworkCardContent, NetworkCardNodelegend, NetworkCardEdgelegend, NetworkCardBar;
    let ExtraModules, QueryModule;
    if(!connectionConfig.connected){
        NetworkCardContent = 
          <Jumbotron>
            <h1>Welcome</h1>
            <p>
              You are missing connection settings.
              Direct towards Settings tab to get started.
            </p>
            <Button variant="secondary" size="lg">
              <NavLink
                to={'/platform/config'}
                className="nav-link"
                activeClassName="active"
              >
                <i className='pe-7s-config' />  Settings 
              </NavLink>
              </Button>
          </Jumbotron>
        ;
        NetworkCardNodelegend = NetworkCardEdgelegend = NetworkCardBar = '';
        ExtraModules = QueryModule = '';
    } else {
      NetworkCardContent = <GraphContainer/>
      // ToDo: revisar legend
      NetworkCardNodelegend = createLegend(visualConfig, "node");
      NetworkCardEdgelegend = createLegend(visualConfig, "edge");
      NetworkCardBar = <TempSliderContainer/>;
      ExtraModules = <Col md={3} style={{height:'100%',  paddingLeft:'5px'}}>
                        <NodeVisualizer/>              
                        <PathTimesModule/>
                        <FilterModule/>
                    </Col>
      QueryModule = <QueryBox/>
      // NetworkCardlegend = '';
    }
    return [NetworkCardContent, NetworkCardNodelegend, NetworkCardEdgelegend, NetworkCardBar,ExtraModules, QueryModule];
  }

  let [NetworkCardContent, NetworkCardNodelegend, NetworkCardEdgeLegend, NetworkCardBar,ExtraModules, QueryModule] = buildNetworkCard()

  return (
    <div className="content">
      <Grid fluid>
        <Row style={{height: '100%'}}>
          <Col md={connectionConfig.connected?9:12} style={{height:'100%', paddingRight: '5px'}}>
            {
              connectionConfig.connected
              ?
              <Card 
                id="querybox"
                content = {QueryModule}
              />
              :
              ''
            }
            <Card
              id="chartHours"
              title="Graph"
              slider={NetworkCardBar}
              content={NetworkCardContent}
              legend={
                <div className="legend" style={{height:'35px', display:'block'}}>
                  <div style={{float:'left'}}>{NetworkCardNodelegend}</div>
                  <div style={{float:'right'}}>{NetworkCardEdgeLegend}</div>
                </div>
              }
            />
          </Col>
          {ExtraModules}
        </Row>
      </Grid>
    </div>
  );
}
 
export default Dashboard;
