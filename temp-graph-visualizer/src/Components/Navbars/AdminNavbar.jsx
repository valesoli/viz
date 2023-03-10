/*!
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard-react/blob/master/LICENSE.md)
* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/
import React, { Component } from "react";
import { Navbar } from "react-bootstrap";

class Header extends Component {
  constructor(props) {
    super(props);
    this.mobileSidebarToggle = this.mobileSidebarToggle.bind(this);
    this.state = {
      sidebarExists: false
    };
  }
  mobileSidebarToggle(e) {
    if (this.state.sidebarExists === false) {
      this.setState({
        sidebarExists: true
      });
    }
    e.preventDefault();
    document.documentElement.classList.toggle("nav-open");
    var node = document.createElement("div");
    node.id = "bodyClick";
    node.onclick = function() {
      this.parentElement.removeChild(this);
      document.documentElement.classList.toggle("nav-open");
    };
    document.body.appendChild(node);
  }
  render() {
    return (
      <Navbar fluid>
        {/* <Navbar.Header>
          <Navbar.Brand>
            <a href="#pablo">{"NADA"}</a>
          </Navbar.Brand>
          <Navbar.Toggle onClick={this.mobileSidebarToggle}>HOLa</Navbar.Toggle>
        </Navbar.Header> */}
        {/* <Navbar.Collapse>
          <AdminNavbarLinks />
        </Navbar.Collapse> */}
      </Navbar>
    );
  }
}

export default Header;
