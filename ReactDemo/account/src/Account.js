import React, { useState, useEffect } from "react";
import Container from "react-bootstrap/Container";
import Loader from "react-loader-spinner";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import InputGroup from "react-bootstrap/InputGroup";

import "./Account.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";

function Account({ history }) {
  const [user, setUser] = useState({});
  const [loading, setLoading] = useState(false);
  const [formLocked, setFormLocked] = useState(true);
  const [validated, setValidated] = useState(false);
  const [usernameError, setUsernameError] = useState(
    "Username cannot be empty"
  );
  const [emailError, setEmailError] = useState("Email cannot be empty");
  const [passwordError, setPasswordError] = useState(
    "Password cannot be empty"
  );
  const [editButton, setEditButton] = useState("Edit");

  useEffect(() => {
    let ignore = false;
    setLoading(true);
    let userToken = getToken();
    if (userToken) {
      fetch(`http://localhost:8080/users/${userToken.username}`).then(
        (response) => {
          response.json().then((data) => {
            setTimeout(() => {
              if (!ignore) setUser(data);
              setLoading(false);
            }, 1000);
          });
        }
      );
    } else {
      setLoading(false);
    }
    return () => {
      ignore = true;
    };
  }, []);

  const getToken = () => {
    const tokenString = localStorage.getItem("token");
    const userToken = JSON.parse(tokenString);
    let currentTime = new Date();
    if (userToken) {
      if (currentTime.getTime() < Date.parse(userToken.expires_in)) {
        return userToken;
      } else {
        return null;
      }
    } else {
      return null;
    }
  };

  const allowEdit = () => {
    setFormLocked(!formLocked);
    formLocked ? setEditButton("Cancel") : setEditButton("Edit");
  };

  const handleTemplateChange = (e) => {
    e.persist();
  };

  const updateUserDetails = (event) => {
    let token = getToken();
    const form = event.currentTarget;
    event.preventDefault();
    event.stopPropagation();
    setValidated(true);

    if (form.checkValidity() === false) {
      console.log("no submit");
    } else {
      if (token) {
        updateUser(token, event);
      } else {
        console.log("Error - not logged in");
      }
    }
  };

  const updateUser = (userToken, event) => {
    const formData = new FormData(event.target),
      formDataObj = Object.fromEntries(formData.entries());
    let authToken = userToken.token_type + " " + userToken.access_token;
    fetch(`http://localhost:8080/users/${userToken.username}`, {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authToken,
      },
      body: JSON.stringify({
        username: formDataObj.username,
        email: formDataObj.email,
        password: formDataObj.password,
      }),
    }).then((response) => {
      if (response.status === 200) {
        response.json().then((data) => {
          setUser(data);
          allowEdit();
          alert("Account Details Updated");
        });
      } else {
        response.json().then((error) => {
          alert("ACCOUNT DETAILS COULD NOT BE UPDATED \n" + error.message);
        });
      }
      setValidated(false);
    });
  };

  if (!loading) {
    return (
      <Container fluid="true">
        <Row noGutters>
          <Col xs={12} className="d-flex flex-row justify-content-between">
            <h1>Account</h1>
            <div className="d-flex flex-column justify-content-center">
              <Button variant="dark" onClick={() => allowEdit()}>
                {editButton}
              </Button>
            </div>
          </Col>
          <Col xs={12}>
            <Form noValidate validated={validated} onSubmit={updateUserDetails}>
              <fieldset disabled={formLocked}>
                <Form.Group>
                  <Form.Label>Username</Form.Label>
                  <InputGroup hasvalidation="true">
                    <Form.Control
                      name="username"
                      id="username"
                      defaultValue={user.username}
                      onChange={handleTemplateChange}
                      required
                    />
                    <Form.Control.Feedback type="invalid">
                      {usernameError}
                    </Form.Control.Feedback>
                  </InputGroup>
                </Form.Group>
                <Form.Group>
                  <Form.Label>Email</Form.Label>
                  <InputGroup hasvalidation="true">
                    <Form.Control
                      name="email"
                      type="email"
                      id="email"
                      defaultValue={user.email}
                      onChange={handleTemplateChange}
                      required
                    />
                    <Form.Control.Feedback type="invalid">
                      {emailError}
                    </Form.Control.Feedback>
                  </InputGroup>
                </Form.Group>
                <Form.Group>
                  <Form.Label>Password</Form.Label>
                  <InputGroup hasvalidation="true">
                    <Form.Control
                      name="password"
                      type="password"
                      id="password"
                      placeholder="Password"
                      required
                    />
                    <Form.Control.Feedback type="invalid">
                      {passwordError}
                    </Form.Control.Feedback>
                  </InputGroup>
                </Form.Group>
                <Button type="submit" variant="success">
                  Submit
                </Button>
              </fieldset>
            </Form>
          </Col>
        </Row>
      </Container>
    );
  } else {
    return (
      <Container fluid="true" className="account-loading-container">
        <Row className="justify-content-xs-center">
          <Col>
            <Loader
              type="TailSpin"
              color="#343a40"
              height={200}
              width={200}
              className="account-spinner-center"
              timeout={1000}
            />
          </Col>
        </Row>
      </Container>
    );
  }
}

export default Account;
