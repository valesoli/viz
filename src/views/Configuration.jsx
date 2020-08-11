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
  render() {
    return (
      <div className="content">
        <Grid fluid>
          <Row>
            <Col md={12}>
              <DbConfigCard/>
            </Col>
            {/* <Col md={4}>
              <UserCard
                bgImage="https://ununsplash.imgix.net/photo-1431578500526-4d9613015464?fit=crop&fm=jpg&h=300&q=75&w=400"
                avatar={avatar}
                name="Mike Andrew"
                userName="michael24"
                description={
                  <span>
                    "Lamborghini Mercy
                    <br />
                    Your chick she so thirsty
                    <br />
                    I'm in that two seat Lambo"
                  </span>
                }
                socials={
                  <div>
                    <Button simple>
                      <i className="fa fa-facebook-square" />
                    </Button>
                    <Button simple>
                      <i className="fa fa-twitter" />
                    </Button>
                    <Button simple>
                      <i className="fa fa-google-plus-square" />
                    </Button>
                  </div>
                }
              />
            </Col> */}
          </Row>
          <Row>
            <Col md={12}>
              <GranularityConfigCard/>
            </Col>
          </Row>
          <Row>
            <Col md={9}>
              <NodeConfigCard/>
            </Col>
            <Col md={3}>
              <EdgeConfigCard/>
            </Col>
          </Row>
        </Grid>
      </div>
    );
  }
}

export default Configuration;
