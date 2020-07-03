import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Popoto from './Components/Popoto';
import { NeoGraph, ResponsiveNeoGraph } from "./Components/NeoGraph";
import { render } from '@testing-library/react';

const NEO4J_URI = "bolt://localhost:7687";
const NEO4J_USER = "neo4j";
const NEO4J_PASSWORD = "admin";


class App extends Component {
  render() {
    return (
      <div className="App" style={{ fontFamily: "Quicksand" }}>
          <h1>React Neovis Example</h1>
          <ResponsiveNeoGraph
            containerId={"id0"}
            neo4jUri={NEO4J_URI}
            neo4jUser={NEO4J_USER}
            neo4jPassword={NEO4J_PASSWORD}
          />
          <NeoGraph
            width={400}
            height={300}
            containerId={"id1"}
            neo4jUri={NEO4J_URI}
            neo4jUser={NEO4J_USER}
            neo4jPassword={NEO4J_PASSWORD}
            backgroundColor={"#b2beb5"}
          />
        </div>
      /*
      <Popoto />
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            Edit <code>src/App.js</code> and save to reload.
          </p>
          <a
            className="App-link"
            href="https://reactjs.org"
            target="_blank"
            rel="noopener noreferrer"
          >
            Learn React
          </a>
        </header>
      </div>
      */
    );
  }
}

export default App;
