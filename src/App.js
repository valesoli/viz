import React, { Component } from 'react';
import './App.css';
import {Navbar, Nav, Container, Col, Row} from 'react-bootstrap';
import NeoGraph from "./Components/NeoGraph";
import TempSlider from "./Components/TempSlider";
import { Graph } from "@vx/network";
import {
  forceCenter,
  forceLink,
  forceManyBody,
  forceSimulation
} from "d3-force";
import {api_cypherQuery} from './GraphService/graphQueryService';

const NEO4J_URI = "bolt://localhost:7687";
const NEO4J_USER = "neo4j";
const NEO4J_PASSWORD = "admin";

class NetworkNode extends React.Component {
  render() {
    return <circle r={10} fill={"#9280FF"} />;
  }
}

class Network extends React.Component {
  constructor(props) {
    super(props);

    const links = props.network.links;
    const nodes = props.network.nodes;

    this.state = {
      data: {
        nodes,
        links
      }
    };
    this.networkCallback = this.networkCallback.bind(this);
  }

  // Update force if the width or height of the graph changes
  componentDidUpdate(newProps) {
    
    this.force = this.force
      .force("center", forceCenter(
        newProps.width / 2,
        newProps.height / 2
      ))
      .restart();

  }

  // Setup D3 force
  componentDidMount() {
    this.force = forceSimulation(this.state.data.nodes)
      .force(
        "link",
        forceLink()
          .id(function(d) {
            return d.id;
          })
          .links(this.state.data.links)
      )
      .force("charge", forceManyBody().strength(-500))
      .force(
        "center",
        forceCenter(this.props.width / 2, this.props.height / 2)
      );

    // Force-update the component on each force tick   
    this.force.on("tick", () => this.forceUpdate());
    api_cypherQuery("match (n) with collect(id(n)) as nodes match (m)-[r]->(o) with nodes, collect([id(m),id(o)]) as edges return nodes, edges", this.networkCallback);    
  }

  networkCallback(response){
    let response_tuple = response.results[0].data[0].row
    let nodes = [];
    let links = [];
    response_tuple[0].forEach(e => {
      nodes.push({id: e})
    });
    response_tuple[1].forEach(e => {
      links.push({source: e[0], target: e[1]})
    });
    this.setState({
      data: {
        nodes: nodes,
        links: links
      }
    });
    this.force = forceSimulation(this.state.data.nodes)
      .force(
        "link",
        forceLink()
          .id(function(d) {
            return d.id;
          })
          .links(this.state.data.links)
      )
      .force("charge", forceManyBody().strength(-500))
      .force(
        "center",
        forceCenter(this.props.width / 2, this.props.height / 2)
      );
  }

  render() {
    return (
      <div style={{ width: "100%", height: "100%" }}>
        <svg width={this.props.width} height={this.props.height}>
          <rect
            width={this.props.width}
            height={this.props.height}
            fill="#f9fcff"
          />
          <Graph graph={this.state.data} nodeComponent={NetworkNode} />
        </svg>
      </div>
    );
  }
}

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
