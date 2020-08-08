import React, { Component } from "react";
import PropTypes from "prop-types";
import NeoVis from "neovis.js/dist/neovis.js"


function refreshGraph(min, max){
  this.vis.renderWithCypher("match (o)-[r]-() where toInteger(split(o.interval[0], '—')[0]) > " 
                            + min
                            + " AND toInteger(split(o.interval[0], '—')[0]) < "
                            + max 
                            + " return o,r");
  //this.vis.renderWithCypher("match (o:Object)where toInteger(split(o.interval[0], '—')[0]) > 1960 AND toInteger(split(o.interval[0], '—')[0]) < 2000 return o");
}

class NeoGraph extends Component {
  constructor(props) {
    super(props);
    this.visRef = React.createRef();
    refreshGraph = refreshGraph.bind(this);
  }

  componentDidMount() {
    const { neo4jUri, neo4jUser, neo4jPassword } = this.props;
    const config = {
      container_id: this.visRef.current.id,
      server_url: neo4jUri,
      server_user: neo4jUser,
      server_password: neo4jPassword,
      labels: {
        "Object": {
          "caption": "title"
        },
        "Attribute": {
          "caption": "title"
        },
        "Value": {
          "caption": "value"
        }
      },
      relationships: {
        "Edge": {
          "caption": false,
          "thickness": 1
        },
        "LivedIn": {
          "thickness": 1
        },
        "Friend": {
          "thickness": 1
        },
        "Fan": {
          "thickness": 1
        }
      },
      initial_cypher:
        "match (n)-[r]-() return n,r"
    };
    /*
      Since there is no neovis package on NPM at the moment, we have to use a "trick":
      we bind Neovis to the window object in public/index.html.js
    */
   
    this.vis = new NeoVis(config);
    this.vis.render();
  }

  render() {
    const {height, containerId, backgroundColor } = this.props;
    return (
      <div
        id={containerId}
        ref={this.visRef}
        className="col"
        style={{
          height: `${height}px`,
          backgroundColor: `${backgroundColor}`
        }}
      />
    );
  }
}

NeoGraph.defaultProps = {
  width: 600,
  height: 600,
  backgroundColor: "#d3d3d3"
};

NeoGraph.propTypes = {
  width: PropTypes.number.isRequired,
  height: PropTypes.number.isRequired,
  containerId: PropTypes.string.isRequired,
  neo4jUri: PropTypes.string.isRequired,
  neo4jUser: PropTypes.string.isRequired,
  neo4jPassword: PropTypes.string.isRequired,
  backgroundColor: PropTypes.string
};

export default NeoGraph;