import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import NavDropdown from "react-bootstrap/NavDropdown";
import LinkContainer from "react-router-bootstrap/lib/LinkContainer";
import "bootstrap/dist/css/bootstrap.min.css";

import "./Header.css";

function Header() {
  return (
    <Navbar
      collapseOnSelect
      bg="dark"
      variant="dark"
      expand="md"
      className="header-navbar"
    >
      <LinkContainer exact to="/">
        <Navbar.Brand>Shop</Navbar.Brand>
      </LinkContainer>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto">
          {/* <LinkContainer exact to="/cart">
            <Nav.Link>Cart</Nav.Link>
          </LinkContainer> */}
          <NavDropdown title="Account" id="basic-nav-dropdown">
            <LinkContainer exact to="/login">
              <NavDropdown.Item>Login</NavDropdown.Item>
            </LinkContainer>
            {/* <LinkContainer to="/signup">
              <NavDropdown.Item>Sign Up</NavDropdown.Item>
            </LinkContainer>
            <LinkContainer to="/logout">
              <NavDropdown.Item>Logout</NavDropdown.Item>
            </LinkContainer>
            <NavDropdown.Divider />
            <LinkContainer to="/myaccount">
              <NavDropdown.Item>My Account</NavDropdown.Item>
            </LinkContainer> */}
          </NavDropdown>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  );
}

export default Header;
