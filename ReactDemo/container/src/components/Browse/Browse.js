import React from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

const Browse = () => (
  <Container fluid="md" className="container">
    <Row>
      <Col className="center-text">
        <h1 className="display-4">Browse</h1>
      </Col>
    </Row>
    <Row>
      <Col className="center-text">
        <h3>Row of Products</h3>
      </Col>
    </Row>
    <Row>
      <Col className="center-text">
        <h3>Another Row of Products</h3>
      </Col>
    </Row>
    <Row>
      <Col className="center-text">
        <h3>
          each of these h3s will be react components in a sub folder which will
          be a carousel of products. Each will be a mf
        </h3>
      </Col>
    </Row>
  </Container>
);

export default Browse;
