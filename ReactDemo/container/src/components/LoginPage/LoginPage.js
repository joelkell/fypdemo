import React from "react";
import { useHistory, NavLink } from "react-router-dom";
import MicroFrontend from "../../MicroFrontend";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

import "bootstrap/dist/css/bootstrap.min.css";

const { REACT_APP_LOGIN_HOST: loginHost } = process.env;

function Login({ history }) {
  return <MicroFrontend history={history} host={loginHost} name="Login" />;
}

const LoginPage = () => {
  let history = useHistory();
  return (
    <React.Fragment>
      <Login history={history} />
      <Container fluid="md">
        <Row>
          <Col>
            <p className="lead">
              Don't have an account? Sign up here:{" "}
              <NavLink to="/signup">create new account</NavLink>
              {/* <Button onClick={() => signup()}>create new account</Button> */}
            </p>
          </Col>
        </Row>
      </Container>
    </React.Fragment>
  );
};

export default LoginPage;
