import React from "react";
import Navbar from "react-bootstrap/Navbar";

import "bootstrap/dist/css/bootstrap.min.css";
import "./Footer.css";

const Footer = () => (
  <Navbar bg="dark" variant="dark" fixed="bottom" className="footer-navbar">
    <Navbar.Text>Joel Kell &copy;2021</Navbar.Text>
  </Navbar>
);

export default Footer;
