import React, { Component } from 'react';
import './App.css';
import {Navbar, Nav, Container, Col, Row} from 'react-bootstrap';
import NeoGraph from "./Components/NeoGraph";

const NEO4J_URI = "bolt://localhost:7687";
const NEO4J_USER = "neo4j";
const NEO4J_PASSWORD = "admin";


class App extends Component {
  render() {
    return (
      <Container>
        <Row className="pb-1 pt-1">
          <Col>
            <Navbar bg="primary" variant="dark" className="justify-content-between">
              <Navbar.Brand href="#home">React NeoVis Example</Navbar.Brand>
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
            <NeoGraph
              width={400}
              height={700}
              containerId={"id1"}
              neo4jUri={NEO4J_URI}
              neo4jUser={NEO4J_USER}
              neo4jPassword={NEO4J_PASSWORD}
              backgroundColor={"rgba(178, 190, 181, 0.16)"}
            />
          </Col>
        </Row>
      </Container>
    );
  }
}

export default App;
