import React, { Component } from 'react';
import './App.css';
import {Navbar, Nav, Container, Col, Row} from 'react-bootstrap';
import TempSlider from "./Components/TempSlider";
import Network from './NetworkComponents/Network'

const NEO4J_URI = "bolt://localhost:7687";
const NEO4J_USER = "neo4j";
const NEO4J_PASSWORD = "admin";

const nodes = [{ id: 1 }, { id: 2 }, { id: 3 }, { id: 4 }, { id: 5 }];
const links = [
  { source: 1, target: 2 },
  { source: 1, target: 3 },
  { source: 1, target: 4 },
  { source: 2, target: 4 },
  { source: 3, target: 4 },
  { source: 4, target: 5 }
];


class App extends Component {
  componentDidMount() {
  }

  componentWillUnmount() {
  }

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
            <Network
              width={1110}
              height={700}
              network={{
                nodes: nodes,
                links: links
              }}
            />
          </Col>
        </Row>
        <TempSlider initMinDate={1900} initMaxDate={1980}/>
      </Container>
    );
  }
}

export default App;
