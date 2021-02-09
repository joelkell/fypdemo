import React from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

function NoMatch({ history }) {
  return (
    <Container fluid="md">
      <Row>
        <Col>
          <h1 className="display-3">404: Page not found! :(</h1>
          <p className="lead">
            No match for <code>{history.location.pathname}</code>
          </p>
        </Col>
      </Row>
    </Container>
  );
}

export default NoMatch;
