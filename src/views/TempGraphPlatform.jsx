/*!
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard-react/blob/master/LICENSE.md)
* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/

import React, { Component } from "react";
import { Route, Switch } from "react-router-dom";

import Footer from "components/Footer/Footer";
import Sidebar from "components/Sidebar/Sidebar";

import routes from "routes.js";

import image from "assets/img/sidebar-4.jpg";

class TempGraphPlatform extends Component {
  constructor(props) {
    super(props);
    this.state = {
      image: image,
      color: "black",
      hasImage: true,
      fixedClasses: "dropdown show-dropdown open"
    };
  }
  getRoutes = routes => {
    return routes.map((prop, key) => {
      if (prop.layout === "/platform") {
        return (
          <Route
            path={prop.layout + prop.path}
            render={props => (
                <prop.component
                  {...props}
                  connection={this.props.connection}
                  query = {this.props.query}
                  visual = {this.props.visual}
                  temporality={this.props.temporality}
                />
              )}
              key={key}
            />
        );
      } else {
        return null;
      }
    });
  };
  componentDidMount() {
    this.setState({ _notificationSystem: this.refs.notificationSystem });
  }
  componentDidUpdate(e) {
    if (
      window.innerWidth < 993 &&
      e.history.location.pathname !== e.location.pathname &&
      document.documentElement.className.indexOf("nav-open") !== -1
    ) {
      document.documentElement.classList.toggle("nav-open");
    }
    if (e.history.action === "PUSH") {
      document.documentElement.scrollTop = 0;
      document.scrollingElement.scrollTop = 0;
      this.refs.mainPanel.scrollTop = 0;
    }
  }
  render() {
    return (
      <div className="wrapper">
        <Sidebar {...this.props} routes={routes} image={this.state.image} color={this.state.color} hasImage={this.state.hasImage} />
        <div id="main-panel" className="main-panel" ref="mainPanel">
          <Switch>{this.getRoutes(routes)}</Switch>
          <Footer />
        </div>
      </div>
    );
  }
}

export default TempGraphPlatform;
