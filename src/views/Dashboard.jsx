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
import NetworkVis from "components/NetworkComponents/NetworkVis"
import FilterModule from "components/NetworkComponents/FilterModule";
import NodeVisualizer from "components/NetworkComponents/NodeVisualizer";

class Dashboard extends Component {
  constructor(props){
    super(props);
    this.state = { notConnected: true }
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
    for (var i = 0; i < json["names"].length; i++) {
      var type = "fa fa-circle text-" + json["types"][i];
      legend.push(<i className={type} key={i} />);
      legend.push(" ");
      legend.push(json["names"][i]);
    }
    return legend;
  }

  buildNetworkCard(){
    let NetworkCardContent;
    let NetworkCardlegend;
    if(this.state.notConnected){
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
    } else {
      NetworkCardContent = <NetworkVis/>;
      NetworkCardlegend = this.createLegend(legendNodes);
    }
    return [NetworkCardContent, NetworkCardlegend]
  }

  render() {
    let [NetworkCardContent, NetworkCardlegend] = this.buildNetworkCard();

    return (
      <div className="content">
        <Grid fluid>
          <Row>
            <Col md={9}>
              <Card
                statsIcon="fa fa-history"
                id="chartHours"
                title="Graph"
                stats="Updated 3 minutes ago"
                content={NetworkCardContent}
                legend={
                  <div className="legend">{NetworkCardlegend}</div>
                }
              />
            </Col>
            <Col md={3}>
                <Row>
                  <NodeVisualizer/>
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
                legend={
                  <div className="legend">{this.createLegend(legendBar)}</div>
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
