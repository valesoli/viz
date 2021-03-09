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
import { NavLink } from "react-router-dom";

import AdminNavbarLinks from "components/Navbars/AdminNavbarLinks.jsx";

import logo from "assets/img/temp-graph-vis-logo.png";

class Sidebar extends Component {
  constructor(props) {
    super(props);
    this.state = {
      width: window.innerWidth
    };
  }
  activeRoute(routeName) {
    return this.props.location.pathname.indexOf(routeName) > -1 ? "active" : "";
  }
  updateDimensions() {
    this.setState({ width: window.innerWidth });
  }
  componentDidMount() {
    this.updateDimensions();
    window.addEventListener("resize", this.updateDimensions.bind(this));
  }
  render() {
    const sidebarBackground = {
      backgroundImage: "url(" + this.props.image + ")"
    };
    return (
      <div
        id="sidebar"
        className={this.props.collapsed?"sidebar s-collapsed":"sidebar"}
        data-color={this.props.color}
        data-image={this.props.image}
      >
          {this.props.hasImage ? (
            <div className="sidebar-background" style={sidebarBackground} />
          ) : (
            null
          )}
        <div className="logo" style={{display:"flex", padding: this.props.collapsed?'':"5px 15px"}}>
            {this.props.collapsed ? "" :
            (<>
              <div className="logo-img" style={{float:"left"}}>
                <a
                  href="https://github.com/joacorma/temp-graph-visualizer"
                  className="simple-text logo-mini"
                >
                  <img src={logo} style={{marginTop:'-2px'}}alt="logo_image" />
                </a>
              </div>
              <div style={{float:"left"}}>
                <a
                  href="https://github.com/joacorma/temp-graph-visualizer"
                  className="simple-text logo-normal"
                >
                  Temp-G Vis
                </a>
              </div>
            </>)}
          <div style={{borderColor: "transparent", float:"right", marginTop:"8px", marginRight:"-4px", marginLeft:this.props.collapsed?"-6px":"10px"}}>
            <button className={this.props.collapsed ? "my-button" : "my-button-nc"} onClick={this.props.changeCollapse}><i className={this.props.collapsed?"pe-7s-right-arrow":"pe-7s-left-arrow"}/></button>
          </div>
        </div>
        <div className={this.props.collapsed?"sidebar-wrapper s-collapsed":"sidebar-wrapper"}>
          <ul className="nav">
            {this.state.width <= 991 ? <AdminNavbarLinks /> : null}
            {this.props.routes.map((prop, key) => {
              if (!prop.redirect)
                return (
                  <li
                    className={
                      prop.upgrade
                        ? "active active-pro"
                        : this.activeRoute(prop.layout + prop.path)
                    }
                    style={{display:this.props.collapsed?"flex":""}}
                    key={key}
                  >
                    <NavLink
                      to={prop.layout + prop.path}
                      className="nav-link"
                      activeClassName="active"
                    >
                      <i style={{marginRight:this.props.collapsed?"0px":""}} className={prop.icon} />
                      {this.props.collapsed?'':<p>{prop.name}</p>}
                    </NavLink>
                  </li>
                );
              return null;
            })}
          </ul>
        </div>
      </div>
    );
  }
}

export default Sidebar;
