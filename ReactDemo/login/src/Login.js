import React, { useState, useRef } from "react";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import InputGroup from "react-bootstrap/InputGroup";
import FormControl from "react-bootstrap/FormControl";
import PropTypes from "prop-types";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

function Login({ history, setToken }) {
  const [error, setError] = useState("invisible");
  const usernameRef = useRef(null);
  const passwordRef = useRef(null);

  const login = () => {
    fetch("http://localhost:8080/users/login", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: usernameRef.current.value,
        email: "e@e",
        password: passwordRef.current.value,
      }),
    }).then((response) => {
      handleResponse(response);
    });
  };

  const handleResponse = (response) => {
    if (response.status === 200) {
      return response.json().then((data) => {
        LoginSuccessful(data);
      });
    } else if (response.status === 401) {
      return response.text().then((data) => {
        setError("visibile");
      });
    } else {
      console.log("error");
    }
  };

  const LoginSuccessful = (data) => {
    let expires = new Date();
    expires.setSeconds(expires.getSeconds() + data.expires_in);
    data.expires_in = expires;
    setToken(data);
    setError("invisible");
    history.goBack();
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      login();
    }
  };

  return (
    <Container fluid="md" className="login-container">
      <Row>
        <Col className="login-center-text">
          <h1 className="display-6 login-header">Login</h1>
        </Col>
      </Row>
      <Row className="login-error-row">
        <Col xs={12} className="login-center-text">
          <span className={`login-error-message ${error}`}>
            Username or Password is incorrect. Please double-check and try
            again.
          </span>
        </Col>
      </Row>
      <Row>
        <Col xs={12} className="login-input">
          <div className="login-form-width">
            <InputGroup className="mb-3">
              <FormControl
                placeholder="username"
                aria-label="username"
                ref={usernameRef}
                onKeyDown={handleKeyDown}
              />
            </InputGroup>
          </div>
        </Col>
        <Col xs={12} className="login-input">
          <div className="login-form-width">
            <InputGroup className="mb-3">
              <FormControl
                placeholder="password"
                aria-label="password"
                type="password"
                ref={passwordRef}
                onKeyDown={handleKeyDown}
              />
            </InputGroup>
          </div>
        </Col>
        <Col xs={12} className="login-center-text login-button-column">
          <div>
            <Button
              className="btn btn-primary btn-lg login-button"
              onClick={() => login()}
            >
              Login
            </Button>
          </div>
        </Col>
      </Row>
    </Container>
  );
}

Login.propTypes = {
  setToken: PropTypes.func.isRequired,
};

export default Login;
