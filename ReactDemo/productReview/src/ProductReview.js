import React from "react";
import { withRouter } from "react-router-dom";
import Loader from "react-loader-spinner";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import InputGroup from "react-bootstrap/InputGroup";

import "./ProductReview.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";

class ProductReview extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      loading: true,
      error: false,
      reviews: null,
      loginModalShow: false,
      reviewModalShow: false,
      reviewExistsModalShow: false,
      validated: false,
      radioValue: "1",
    };
  }

  getUsername = async (userId) => {
    const response = await fetch(`http://localhost:8080/users/${userId}`);
    const data = await response.json();
    return data.username;
  };

  handleResponse = (response) => {
    if (response.status === 200) {
      return response.json().then((data) => {
        this.setState({
          reviews: data,
        });

        this.state.reviews.forEach(async (review) => {
          review.username = await this.getUsername(review.userId);
        });

        setTimeout(() => {
          this.setState({
            loading: false,
          });
        }, 1000);
      });
    } else {
      setTimeout(() => {
        this.setState({
          error: true,
          loading: false,
        });
      }, 1000);
    }
  };

  componentDidMount() {
    const id = this.props.match.params.id;

    fetch(`http://localhost:8080/reviews/products/${id}`).then((response) => {
      this.handleResponse(response);
    });
  }

  handleLoginModalClose = () => {
    this.setState({ loginModalShow: false });
  };
  handleLoginModalShow = () => {
    this.setState({ loginModalShow: true });
  };

  handleReviewModalClose = () => {
    this.setState({ reviewModalShow: false });
  };
  handleReviewModalShow = () => {
    this.setState({ reviewModalShow: true });
  };

  handleReviewExistsModalClose = () => {
    this.setState({ reviewExistsModalShow: false });
  };
  handleReviewExistsModalShow = () => {
    this.setState({ reviewExistsModalShow: true });
  };

  handleRadioChange = (e) => {
    e.persist();

    this.setState({
      radioValue: e.target.value,
    });
  };

  login() {
    let history = this.props.history;
    history.push("/login");
  }

  getToken() {
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
  }

  getReview(token) {
    let reviewExists = false;
    this.state.reviews.forEach((review) => {
      if (review.userId === token.username) {
        reviewExists = true;
      }
    });
    return reviewExists;
  }

  addReview() {
    let token = this.getToken();
    let reviewExists;
    if (token) {
      reviewExists = this.getReview(token);
    }

    if (!token) {
      this.handleLoginModalShow();
    } else if (reviewExists) {
      this.handleReviewExistsModalShow();
    } else {
      this.handleReviewModalShow();
    }
  }

  createReview = (event) => {
    const form = event.currentTarget;
    event.preventDefault();
    event.stopPropagation();
    this.setState({ validated: true });
    if (form.checkValidity() === false) {
      console.log("no submit");
    } else {
      const formData = new FormData(event.target),
        formDataObj = Object.fromEntries(formData.entries());

      let productId = this.props.match.params.id;
      let token = this.getToken();
      let userId = token.username;
      let authToken = token.token_type + " " + token.access_token;
      fetch(`http://localhost:8080/reviews/`, {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: authToken,
        },
        body: JSON.stringify({
          productId: productId,
          userId: userId,
          rating: parseInt(formDataObj.formHorizontalRadios),
          comment: formDataObj.formComment,
        }),
      }).then((response) => {
        response.json().then(async (review) => {
          review.username = await this.getUsername(review.userId);

          this.setState((prevState) => ({
            reviews: [...prevState.reviews, review],
          }));
          this.handleReviewModalClose();
        });
      });
    }
  };

  render() {
    if (this.state.loading) {
      return (
        <Container fluid="true" className="productReview-loading-container">
          <Row className="justify-content-xs-center">
            <Col>
              <Loader
                type="TailSpin"
                color="#343a40"
                height={200}
                width={200}
                className="productReview-spinner-center"
                timeout={1000}
              />
            </Col>
          </Row>
        </Container>
      );
    }
    if (this.state.error) {
      return (
        <Container fluid="md">
          <Row>
            <Col>
              <h1 className="display-4">Reviews</h1>
              <div>No Reviews Found</div>
            </Col>
          </Row>
        </Container>
      );
    }

    let Reviews = () => {
      const Stars = (rating) => {
        let stars = "";
        for (let i = 1; i <= rating; i++) {
          stars += "★";
        }
        return stars;
      };

      return this.state.reviews.map((review, index) => (
        <Col
          xs={12}
          className="d-flex flex-column justify-content-between productReview-review-div"
          key={index}
        >
          <div className="lead">
            <strong>{review.username}</strong>
          </div>
          <div className="lead">{Stars(review.rating)}</div>
          <div className="lead">{review.comment}</div>
        </Col>
      ));
    };

    return (
      <Container fluid="true">
        <Row noGutters>
          <Col xs={12}>
            <h1 className="display-4"> Reviews</h1>
          </Col>
          <Col
            xs={12}
            className="d-flex flex-row-reverse productReview-review-button-col"
          >
            <Button variant="dark" onClick={() => this.addReview()}>
              Add Review
            </Button>
            <Modal
              show={this.state.loginModalShow}
              onHide={() => this.handleLoginModalClose()}
            >
              <Modal.Header closeButton>
                <Modal.Title>Log In!</Modal.Title>
              </Modal.Header>
              <Modal.Body>You must be logged in to add a review</Modal.Body>
              <Modal.Footer>
                <Button
                  variant="secondary"
                  onClick={() => this.handleLoginModalClose()}
                >
                  Close
                </Button>
                <Button variant="primary" onClick={() => this.login()}>
                  Login
                </Button>
              </Modal.Footer>
            </Modal>
            <Modal
              show={this.state.reviewModalShow}
              onHide={() => this.handleReviewModalClose()}
            >
              <Modal.Header closeButton>
                <Modal.Title>Review</Modal.Title>
              </Modal.Header>
              <Form
                noValidate
                validated={this.state.validated}
                onSubmit={this.createReview}
              >
                <Modal.Body>
                  <Form.Group controlId="formBasicEmail">
                    <Form.Label className="mr-3">Rating:</Form.Label>
                    <Form.Check
                      type="radio"
                      label="1"
                      value="1"
                      inline
                      name="formHorizontalRadios"
                      id="formHorizontalRadios1"
                      onChange={this.handleRadioChange}
                      checked={this.state.radioValue === "1"}
                    />
                    <Form.Check
                      type="radio"
                      label="2"
                      value="2"
                      inline
                      name="formHorizontalRadios"
                      id="formHorizontalRadios2"
                      onChange={this.handleRadioChange}
                      checked={this.state.radioValue === "2"}
                    />
                    <Form.Check
                      type="radio"
                      label="3"
                      value="3"
                      inline
                      name="formHorizontalRadios"
                      id="formHorizontalRadios3"
                      onChange={this.handleRadioChange}
                      checked={this.state.radioValue === "3"}
                    />
                    <Form.Check
                      type="radio"
                      label="4"
                      value="4"
                      inline
                      name="formHorizontalRadios"
                      id="formHorizontalRadios4"
                      onChange={this.handleRadioChange}
                      checked={this.state.radioValue === "4"}
                    />
                    <Form.Check
                      type="radio"
                      label="5"
                      value="5"
                      inline
                      name="formHorizontalRadios"
                      id="formHorizontalRadios5"
                      onChange={this.handleRadioChange}
                      checked={this.state.radioValue === "5"}
                    />
                  </Form.Group>

                  <Form.Group controlId="controlTextarea1">
                    <Form.Label>Review Comment</Form.Label>
                    <InputGroup hasvalidation="true">
                      <Form.Control
                        as="textarea"
                        rows={3}
                        name="formComment"
                        required
                      />
                      <Form.Control.Feedback type="invalid">
                        Must include a comment
                      </Form.Control.Feedback>
                    </InputGroup>
                  </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                  <Button
                    variant="secondary"
                    onClick={() => this.handleReviewModalClose()}
                  >
                    Cancel
                  </Button>
                  <Button variant="primary" type="submit">
                    Submit Review
                  </Button>
                </Modal.Footer>
              </Form>
            </Modal>
            <Modal
              show={this.state.reviewExistsModalShow}
              onHide={() => this.handleReviewExistsModalClose()}
            >
              <Modal.Header closeButton>
                <Modal.Title>Review Exists!</Modal.Title>
              </Modal.Header>
              <Modal.Body>
                You have already submitted a review for this product
              </Modal.Body>
              <Modal.Footer>
                <Button
                  variant="danger"
                  onClick={() => this.handleReviewExistsModalClose()}
                >
                  Close
                </Button>
              </Modal.Footer>
            </Modal>
          </Col>
          <Col xs={12}>
            <Container className="productReview-review-container">
              <Row>
                <Reviews />
              </Row>
            </Container>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default withRouter(ProductReview);
