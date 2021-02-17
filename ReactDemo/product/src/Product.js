import React from "react";
import { withRouter } from "react-router-dom";
import Loader from "react-loader-spinner";
import Button from "react-bootstrap/Button";
import Image from "react-bootstrap/Image";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Modal from "react-bootstrap/Modal";

import "./Product.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";
import placeholder from "./placeholder-images.png";

class Product extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      loading: true,
      error: false,
      product: null,
      buttonDisabled: false,
      imageSource: placeholder,
      modalShow: false,
    };
  }

  handleResponse = (response) => {
    if (response.status === 200) {
      return response.json().then((data) => {
        setTimeout(() => {
          this.setState({
            product: data,
            loading: false,
          });
          if (this.state.product.stockLevel <= 0) {
            this.setState({
              buttonDisabled: true,
            });
          }
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

    fetch(`http://localhost:8080/products/${id}`).then((response) => {
      this.handleResponse(response);
    });
    fetch(`http://localhost:5002/images/${id}.jpg`).then((response) => {
      if (response.status === 200) {
        this.setState({
          imageSource: `http://localhost:5002/images/${id}.jpg`,
        });
      }
    });
  }

  handleClose = () => {
    this.setState({ modalShow: false });
  };
  handleShow = () => {
    this.setState({ modalShow: true });
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

  addToCart() {
    let token = this.getToken();
    if (!token) {
      this.handleShow();
    } else {
      this.getCart(token);
    }
  }

  getCart(token) {
    let productId = this.props.match.params.id;
    let userId = token.username;
    let authToken = token.token_type + " " + token.access_token;
    fetch(`http://localhost:8080/carts/${userId}`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authToken,
      },
    }).then((response) => {
      if (response.status === 200) {
        return response.json().then((data) => {
          fetch(`http://localhost:8080/carts/AddProduct/${userId}`, {
            method: "POST",
            headers: {
              Accept: "application/json",
              "Content-Type": "application/json",
              Authorization: authToken,
            },
            body: JSON.stringify({
              productId: productId,
              quantity: 1,
            }),
          }).then((response) => {
            response.json().then((data) => {
              this.updatePrice(data, userId, authToken);
              this.reduceStockLevel(authToken);
            });
          });
        });
      } else {
        console.log(response);
      }
    });
  }

  updatePrice(data, userId, authToken) {
    let totalPrice =
      data.totalPrice + data.deliveryPrice + this.state.product.price;
    fetch(`http://localhost:8080/carts/${userId}`, {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authToken,
      },
      body: JSON.stringify({
        _id: data._id,
        userId: userId,
        deliveryPrice: data.deliveryPrice,
        cartItems: data.cartItems,
        totalPrice: totalPrice,
      }),
    });
  }

  reduceStockLevel(authToken) {
    let stockLevel = this.state.product.stockLevel - 1;
    let productId = this.state.product._id;
    fetch(`http://localhost:8080/products/${productId}`, {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authToken,
      },
      body: JSON.stringify({
        name: this.state.product.name,
        description: this.state.product.description,
        stockLevel: stockLevel,
        price: this.state.product.price,
        categories: this.state.product.categories,
      }),
    }).then((response) => {
      response.json().then((data) => {
        this.setState({
          product: data,
        });
      });
    });
  }

  render() {
    if (this.state.loading) {
      return (
        <Container fluid="true" className="product-loading-container">
          <Row className="justify-content-xs-center">
            <Col>
              <Loader
                type="TailSpin"
                color="#343a40"
                height={200}
                width={200}
                className="product-spinner-center"
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
              <h1 className="display-4">Product</h1>
              <div>No Product Found</div>
            </Col>
          </Row>
        </Container>
      );
    }

    return (
      <Container fluid="true">
        <Row noGutters>
          <Col xs={12}>
            <h1 className="display-4">{this.state.product.name}</h1>
          </Col>
          <Col xs={12} sm={4} className="product-image-col">
            <Image src={this.state.imageSource} fluid />
          </Col>
          <Col
            xs={12}
            sm={8}
            className="product-details-col d-flex flex-column justify-content-between"
          >
            <div>
              <h1 className="h1">€{this.state.product.price}</h1>
              <div>{this.state.product.description}</div>
            </div>
            <Button
              className="product-button-cart"
              variant="dark"
              disabled={this.state.buttonDisabled}
              onClick={() => this.addToCart()}
            >
              Add to Cart
            </Button>

            <Modal
              show={this.state.modalShow}
              onHide={() => this.handleClose()}
            >
              <Modal.Header closeButton>
                <Modal.Title>Log In!</Modal.Title>
              </Modal.Header>
              <Modal.Body>
                You must be logged in to add an item to your cart
              </Modal.Body>
              <Modal.Footer>
                <Button variant="secondary" onClick={() => this.handleClose()}>
                  Close
                </Button>
                <Button variant="primary" onClick={() => this.login()}>
                  Login
                </Button>
              </Modal.Footer>
            </Modal>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default withRouter(Product);
