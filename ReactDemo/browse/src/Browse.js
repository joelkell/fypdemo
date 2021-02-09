import React from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import ItemCarousel from "./ItemCarousel";

import "./Browse.css";
import "bootstrap/dist/css/bootstrap.min.css";

function Browse({ history }) {
  return (
    <Container fluid="md" className="browse-container">
      <Row className="browse-row">
        <Col xs={12}>
          <h3 className="h3">Food</h3>
        </Col>
        <Col xs={12}>
          <ItemCarousel category="food" history={history} />
        </Col>
      </Row>
      <Row className="browse-row">
        <Col xs={12}>
          <h3 className="h3">Technology</h3>
        </Col>
        <Col xs={12}>
          <ItemCarousel category="technology" history={history} />
        </Col>
      </Row>
      <Row className="browse-row">
        <Col xs={12}>
          <h3 className="h3">Music</h3>
        </Col>
        <Col xs={12}>
          <ItemCarousel category="music" history={history} />
        </Col>
      </Row>
    </Container>
  );
}

export default Browse;
