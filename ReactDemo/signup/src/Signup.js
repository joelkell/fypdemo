import React, { useState, useRef } from "react";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import InputGroup from "react-bootstrap/InputGroup";
import FormControl from "react-bootstrap/FormControl";

import "./Signup.css";
import "bootstrap/dist/css/bootstrap.min.css";

function Signup({ history }) {
  const [error, setError] = useState("invisible");
  const [errorText, setErrorText] = useState("An unkown error has occured");
  const usernameRef = useRef(null);
  const emailRef = useRef(null);
  const passwordRef = useRef(null);

  const signup = () => {
    fetch("http://localhost:8080/users", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: usernameRef.current.value,
        email: emailRef.current.value,
        password: passwordRef.current.value,
      }),
    }).then((response) => {
      handleResponse(response);
    });
  };

  const handleResponse = (response) => {
    if (response.status === 200) {
      return response.json().then((data) => {
        createCart(data);
      });
    } else if (response.status === 403 || response.status) {
      return response.text().then((data) => {
        setErrorText(data);
        setError("visibile");
      });
    } else {
      console.log("error");
    }
  };

  const createCart = (data) => {
    fetch(`http://localhost:8080/users/username/${data.username}`).then(
      (response) => {
        response.json().then((user) => {
          fetch("http://localhost:8080/carts/createCart/", {
            method: "POST",
            headers: {
              Accept: "application/json",
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              userId: user._id,
            }),
          }).then(() => {
            SignupSuccessful();
          });
        });
      }
    );
  };

  const SignupSuccessful = () => {
    setError("invisible");
    history.push("/login");
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      signup();
    }
  };

  return (
    <Container fluid="md" className="signup-container">
      <Row>
        <Col className="signup-center-text">
          <h1 className="display-6 signup-header">Signup</h1>
        </Col>
      </Row>
      <Row className="signup-error-row">
        <Col xs={12} className="signup-center-text">
          <span className={`signup-error-message ${error}`}>{errorText}</span>
        </Col>
      </Row>
      <Row>
        <Col xs={12} className="signup-input">
          <div className="signup-form-width">
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
        <Col xs={12} className="signup-input">
          <div className="signup-form-width">
            <InputGroup className="mb-3">
              <FormControl
                placeholder="email"
                aria-label="email"
                type="email"
                ref={emailRef}
                onKeyDown={handleKeyDown}
              />
            </InputGroup>
          </div>
        </Col>
        <Col xs={12} className="signup-input">
          <div className="signup-form-width">
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
        <Col xs={12} className="signup-center-text signup-button-column">
          <div>
            <Button
              className="btn btn-primary btn-lg signup-button"
              onClick={() => signup()}
            >
              Signup
            </Button>
          </div>
        </Col>
      </Row>
    </Container>
  );
}

export default Signup;
