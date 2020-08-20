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
import {
  Grid,
  Row,
  Col,
  FormGroup,
  ControlLabel,
  FormControl
} from "react-bootstrap";

import DbConfigCard from 'components/Card/DbConfigCard';
import NodeConfigCard from "components/Card/NodeConfigCard";
import EdgeConfigCard from "components/Card/EdgeConfigCard";
import GranularityConfigCard from "components/Card/GranularityConfigCard";
import avatar from "assets/img/faces/face-3.jpg";


class Configuration extends Component {
  constructor(props){
    super(props);
    this.state = {connected: this.props.connection.connected}
  }

  componentWillReceiveProps(props){
    this.setState({connected: props.connection.connected});
  }

  getVisualConfig(){
    if(this.state.connected){
      return (
        <Row>
            <Col md={9}>
              <NodeConfigCard visual={this.props.visual}/>
            </Col>
            <Col md={3}>
              <EdgeConfigCard/>
            </Col>
        </Row>
      );
    } else {
      return '';
    }
  }

  getGranularityCard(){
    if(this.state.connected){
      return (
        <Row>
          <Col md={12}>
            <GranularityConfigCard/>
          </Col>
        </Row>
      );
    } else {
      return '';
    }
  }

  render() {
    let granularityCard = this.getGranularityCard();
    let visualCards = this.getVisualConfig();

    return (
      <div className="content">
        <Grid fluid>
          <Row>
            <Col md={12}>
              <DbConfigCard connection={this.props.connection} visual={this.props.visual}/>
            </Col>
          </Row>
          {granularityCard}
          {visualCards}
        </Grid>
      </div>
    );
  }
}

export default Configuration;
