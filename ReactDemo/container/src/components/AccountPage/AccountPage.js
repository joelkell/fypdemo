import React from "react";
import { useHistory } from "react-router-dom";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import MicroFrontend from "../../MicroFrontend";

import "./AccountPage.css";

const { REACT_APP_ACCOUNT_HOST: accountHost } = process.env;

function Account({ history }) {
  return <MicroFrontend history={history} host={accountHost} name="Account" />;
}

const AccountPage = () => {
  let history = useHistory();
  return (
    <Container fluid="md">
      <Row>
        <Col xs={12}>
          <Account history={history} />
        </Col>
      </Row>
    </Container>
  );
};

export default AccountPage;
