/*!

=========================================================
* Light Bootstrap Dashboard React - v1.3.0
=========================================================

* Product Page: https://www.creative-tim.com/product/light-bootstrap-dashboard-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React, { Component } from "react";
import ChartistGraph from "react-chartist";
import { Grid, Row, Col, Button, Jumbotron } from "react-bootstrap";
import { NavLink } from "react-router-dom";

import { Card } from "components/Card/Card.jsx";
import { StatsCard } from "components/StatsCard/StatsCard.jsx";
import { Tasks } from "components/Tasks/Tasks.jsx";
import {
  dataBar,
  optionsBar,
  responsiveBar,
  legendBar,
  legendNodes
} from "variables/Variables.jsx";
import GraphContainer from "components/NetworkComponents/GraphContainer";
import TempSlider from "components/Slider/TempSlider";
import FilterModule from "components/NetworkComponents/FilterModule";
import NodeVisualizer from "components/NetworkComponents/NodeVisualizer";
import { DataSet, Network } from 'vis-network/standalone';

class Dashboard extends Component {
  constructor(props){
    super(props);
    this.state = { 
      connected: props.connection.connected,
      query: props.query
    };    
  }

  componentDidMount() {

  }

  componentWillUnmount() {
    if (this.chart) {
      this.chart.dispose();
    }
  }

  createLegend(json, type) {
    var legend = [];

    let colors = type == "node"?json.nodeColors:json.edgeColors;
    for (const [index, [key, value]] of Object.entries(Object.entries(colors))) {
      var type = "fa fa-circle text";
      legend.push(<i className={type} style={{color: value}} key={index} />);
      legend.push(" ");
      legend.push(key);
    }
    return legend;
  }

  buildNetworkCard(){
    let NetworkCardContent;
    let NetworkCardNodelegend;
    let NetworkCardEdgelegend;
    let NetworkCardBar;
    let ExtraModules;
    if(!this.state.connected){
        NetworkCardContent = 
          <Jumbotron>
            <h1>Bienvenid@!</h1>
            <p>
              Aún no realizó las configuraciones de connexión con neo4j.
              Ahí mismo puede configurar algunos atributos para la visualización.
            </p>
            <Button variant="secondary" size="lg">
              <NavLink
                to={'/platform/config'}
                className="nav-link"
                activeClassName="active"
              >
                
                  <i className='pe-7s-config' />   Configuración
                
              </NavLink>
              </Button>
          </Jumbotron>
        ;
        NetworkCardNodelegend = '';
        NetworkCardEdgelegend = '';
        NetworkCardBar = '';
        ExtraModules = '';
    } else {
      NetworkCardContent = <GraphContainer con_config={ this.props.connection.neo4j_config } visual={this.props.visual} query={this.props.query}/>
      // NetworkCardContent = <MyVis con_config={ this.props.connection.neo4j_config } visual={this.props.visual} data={my_data}/>;
      // NetworkCardContent = <NetworkVis con_config={ this.props.connection.neo4j_config } visual={this.props.visual}/>;
      // ToDo: revisar legend
      NetworkCardNodelegend = this.createLegend(this.props.visual, "node");
      NetworkCardEdgelegend = this.createLegend(this.props.visual, "edge");
      NetworkCardBar = <TempSlider temporality={ this.props.temporality }/>;
      ExtraModules = <Col md={3}>
                        <Row>
                          <NodeVisualizer con_config={ this.props.connection.neo4j_config } visual={this.props.visual}/>              
                        </Row>
                        <Row>
                          <FilterModule/>
                        </Row>
                    </Col>
      // NetworkCardlegend = '';
    }
    return [NetworkCardContent, NetworkCardNodelegend, NetworkCardEdgelegend, NetworkCardBar,ExtraModules];
  }

  render() {
    let [NetworkCardContent, NetworkCardNodelegend, NetworkCardEdgeLegend, NetworkCardBar,ExtraModules] = this.buildNetworkCard();
    
    return (
      <div className="content">
        <Grid fluid>
          <Row>
            <Col md={this.state.connected?9:12}>
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
          {/* <Row>
            <Col md={6}>
              <Card
                id="chartActivity"
                title="2014 Sales"
                category="All products including Taxes"
                stats="Data information certified"
                statsIcon="fa fa-check"
                content={
                  <div className="ct-chart">
                    <ChartistGraph
                      data={dataBar}
                      type="Bar"
                      options={optionsBar}
                      responsiveOptions={responsiveBar}
                    />
                  </div>
                }
              />
            </Col>

            <Col md={6}>
              <Card
                title="Tasks"
                category="Backend development"
                stats="Updated 3 minutes ago"
                statsIcon="fa fa-history"
                content={
                  <div className="table-full-width">
                    <table className="table">
                      <Tasks />
                    </table>
                  </div>
                }
              />
            </Col>
          </Row> */}
        </Grid>
      </div>
    );
  }
}

export default Dashboard;
