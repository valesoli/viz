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
import GraphContainer from "components/NetworkComponents/GraphContainer"
import TempSlider from "components/Slider/TempSlider";
import FilterModule from "components/NetworkComponents/FilterModule";
import NodeVisualizer from "components/NetworkComponents/NodeVisualizer";
import { DataSet, Network } from 'vis-network/standalone';


const nodes = new DataSet([
  { id: 1, label: 'Node 1' },
  { id: 2, label: 'Node 2' },
  { id: 3, label: 'Node 3' },
  { id: 4, label: 'Node 4' },
  { id: 5, label: 'Node 5' }
]);

// create an array with edges
const edges = new DataSet([
  { from: 1, to: 3 },
  { from: 1, to: 2 },
  { from: 2, to: 4 },
  { from: 2, to: 5 }
]);

const my_data = {
  nodes: nodes,
  edges: edges
};


class Dashboard extends Component {
  constructor(props){
    super(props);
    this.state = { connected: this.props.connection.connected };
  }

  componentDidMount() {
    
  }

  componentWillUnmount() {
    if (this.chart) {
      this.chart.dispose();
    }
  }

  createLegend(json) {
    var legend = [];

    let nodeColors = json.nodeColors;
    for (const [index, [key, value]] of Object.entries(Object.entries(nodeColors))) {
      var type = "fa fa-circle text";
      legend.push(<i className={type} style={{color: value}} key={index} />);
      legend.push(" ");
      legend.push(key);
    }
    return legend;
  }

  buildNetworkCard(){
    let NetworkCardContent;
    let NetworkCardlegend;
    let NetworkCardBar;
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
        NetworkCardlegend = '';
        NetworkCardBar = '';
    } else {
      NetworkCardContent = <GraphContainer con_config={ this.props.connection.neo4j_config } visual={this.props.visual}/>
      // NetworkCardContent = <MyVis con_config={ this.props.connection.neo4j_config } visual={this.props.visual} data={my_data}/>;
      // NetworkCardContent = <NetworkVis con_config={ this.props.connection.neo4j_config } visual={this.props.visual}/>;
      // ToDo: revisar legend
      NetworkCardlegend = this.createLegend(this.props.visual);
      NetworkCardBar = <TempSlider initMinDate={1900} initMaxDate={1980}/>;
      // NetworkCardlegend = '';
    }
    return [NetworkCardContent, NetworkCardlegend, NetworkCardBar]
  }

  render() {
    let [NetworkCardContent, NetworkCardlegend, NetworkCardBar] = this.buildNetworkCard();
    
    return (
      <div className="content">
        <Grid fluid>
          <Row>
            <Col md={9}>
              <Card
                id="chartHours"
                title="Graph"
                slider={NetworkCardBar}
                content={NetworkCardContent}
                legend={
                  <div className="legend">{NetworkCardlegend}</div>
                }
              />
            </Col>
            <Col md={3}>
                <Row>
                  <NodeVisualizer con_config={ this.props.connection.neo4j_config } visual={this.props.visual}/>              
                </Row>
                <Row>
                  <FilterModule/>
                </Row>
            </Col>
          </Row>
          <Row>
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
          </Row>
        </Grid>
      </div>
    );
  }
}

export default Dashboard;
