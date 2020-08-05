import React, { Component } from 'react';
import './App.css';
import {Navbar, Nav, Container, Col, Row} from 'react-bootstrap';
import TempSlider from "./Components/TempSlider";
import NetworkWrapper from './NetworkComponents/NetworkWrapper';
import NodeVisualizer from './NetworkComponents/NodeVisualizer';

class App extends Component {
  render() {
    return (
      <Container>
        <Row className="pb-1 pt-1">
          <Col>
            <Navbar bg="primary" variant="dark" className="justify-content-between">
              <Navbar.Brand href="#home">React NeoVis</Navbar.Brand>
              <Nav className="ml-auto">
                <Nav.Link href="#home">Graph Viz</Nav.Link>
                <Nav.Link href="#features">Stats</Nav.Link>
                <Nav.Link href="#pricing">Info</Nav.Link>
              </Nav>
            </Navbar>
          </Col>
        </Row>
        <Row >
          <Col>
            <NetworkWrapper/>
          </Col>
          <NodeVisualizer/>
        </Row>
        {/* <TempSlider initMinDate={1900} initMaxDate={1980}/> */}
      </Container>
    );
  }
}

export default App;
