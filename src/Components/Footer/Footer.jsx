/*!
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard-react/blob/master/LICENSE.md)
* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/

import React, { Component } from "react";
import { Grid } from "react-bootstrap";

class Footer extends Component {
  render() {
    return (
      <footer className="footer">
        <Grid fluid>
          <nav className="pull-left">
            <ul>
              <li>
                <a>Home</a>
              </li>
              <li>
                <a>Company</a>
              </li>
              <li>
                <a>Portfolio</a>
              </li>
              <li>
                <a>Blog</a>
              </li>
            </ul>
          </nav>
          <p className="copyright pull-right">
            &copy; {new Date().getFullYear()}{" "}
            <a href="https://github.com/joacorma/temp-graph-viz">
             Temporal Graph Visualizator 
            </a>
            , to visualize new structures.
          </p>
        </Grid>
      </footer>
    );
  }
}

export default Footer;
