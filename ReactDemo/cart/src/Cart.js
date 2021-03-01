import React, { useState, useEffect } from "react";
import { withRouter, NavLink } from "react-router-dom";
import Container from "react-bootstrap/Container";
import Loader from "react-loader-spinner";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";

import "./Cart.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";

function Cart() {
  const [cart, setCart] = useState({});
  const [loading, setLoading] = useState(false);
  const [loggedIn, setLoggedIn] = useState(false);

  useEffect(() => {
    window.addEventListener(
      "addToCart",
      (event) => {
        if (event.detail.cartItems) {
          event.detail.cartItems.forEach(async (cartItem) => {
            let product = await getProduct(cartItem.productId);
            cartItem.name = product.name;
          });
        }
        setLoading(true);
        setTimeout(() => {
          setCart(event.detail);
          setLoading(false);
        }, 1000);
      },
      false
    );

    let ignore = false;
    setLoading(true);
    let userToken = getToken();
    if (userToken) {
      setLoggedIn(true);
      let authToken = userToken.token_type + " " + userToken.access_token;
      fetch(`http://localhost:8080/carts/${userToken.username}`, {
        method: "GET",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: authToken,
        },
      }).then((response) => {
        response.json().then((data) => {
          if (!ignore) {
            if (data.cartItems) {
              data.cartItems.forEach(async (cartItem) => {
                let product = await getProduct(cartItem.productId);
                cartItem.name = product.name;
              });
            }
            setCart(data);
          }
          setTimeout(() => {
            setLoading(false);
          }, 1000);
        });
      });
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

  const getProduct = async (productId) => {
    const response = await fetch(`http://localhost:8080/products/${productId}`);
    const data = await response.json();
    return data;
  };

  const createOrder = () => {
    let userToken = getToken();
    if (userToken) {
      let authToken = userToken.token_type + " " + userToken.access_token;
      fetch(`http://localhost:8080/orders/`, {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: authToken,
        },
        body: JSON.stringify({
          userId: userToken.username,
          status: "ORDERED",
          totalPrice: cart.totalPrice,
          orderItems: cart.cartItems,
        }),
      }).then((response) => {
        response.json().then((data) => {
          updateCart();
        });
      });
    } else {
      alert("not logged in!");
    }
  };

  const updateCart = () => {
    let userToken = getToken();
    if (userToken) {
      let authToken = userToken.token_type + " " + userToken.access_token;
      fetch(`http://localhost:8080/carts/${userToken.username}`, {
        method: "PUT",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: authToken,
        },
        body: JSON.stringify({
          _id: cart._id,
          userId: userToken.username,
          deliveryPrice: 0,
          totalPrice: 0,
        }),
      }).then((response) => {
        response.json().then((data) => {
          alert("Order Created!");
          setCart(data);
        });
      });
    } else {
      alert("not logged in!");
    }
  };

  const removeItemFromCart = async (itemIndex, productId, quantity) => {
    let cartItems = cart.cartItems;
    cartItems.splice(itemIndex, 1);
    let product = await getProduct(productId);

    //TODO add stock level back up

    let userToken = getToken();
    if (userToken) {
      let authToken = userToken.token_type + " " + userToken.access_token;
      fetch(`http://localhost:8080/carts/${userToken.username}`, {
        method: "PUT",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: authToken,
        },
        body: JSON.stringify({
          _id: cart._id,
          userId: userToken.username,
          deliveryPrice: 0,
          totalPrice: cart.totalPrice - product.price * quantity,
          cartItems: cartItems,
        }),
      }).then((response) => {
        response.json().then(async (data) => {
          console.log("item removed!");
          if (data.cartItems) {
            await data.cartItems.forEach(async (cartItem) => {
              let product = await getProduct(cartItem.productId);
              cartItem.name = product.name;
            });
          }
          setLoading(true);
          setTimeout(() => {
            setCart(data);
            setLoading(false);
          }, 1000);
        });
      });
    } else {
      alert("not logged in!");
    }
  };

  const CartItems = (cartItems) => {
    if (cartItems.cartItems) {
      return cartItems.cartItems.map((cartItems, index) => (
        <div
          className="lead d-flex flex-column justify-content-between cart-cart-items"
          key={index}
        >
          <NavLink to={`/products/${cartItems.productId}`}>
            {cartItems.name}
          </NavLink>
          <div>Quantity: {cartItems.quantity}</div>
          <div className="d-flex flex-row justify-content-end">
            <Button
              variant="danger"
              className="cart-remove-button"
              onClick={() =>
                removeItemFromCart(
                  index,
                  cartItems.productId,
                  cartItems.quantity
                )
              }
            >
              Remove From Cart
            </Button>
          </div>
        </div>
      ));
    } else {
      return <div className="lead">No Items in Cart</div>;
    }
  };

  const Cart = () => {
    return (
      <Col
        xs={12}
        className="d-flex flex-column justify-content-between cart-container"
      >
        <CartItems cartItems={cart.cartItems} />
        <div className="lead">Delivery Price: €{cart.deliveryPrice}</div>
        <div className="lead">Total Price: €{cart.totalPrice}</div>
        <div className="d-flex flex-row justify-content-end">
          <Button
            variant="success"
            className="cart-order-button"
            onClick={() => createOrder()}
          >
            Order
          </Button>
        </div>
      </Col>
    );
  };

  if (loading) {
    return (
      <Container fluid="true" className="cart-loading-container">
        <Row className="justify-content-xs-center">
          <Col>
            <Loader
              type="TailSpin"
              color="#343a40"
              height={200}
              width={200}
              className="cart-spinner-center"
              timeout={1000}
            />
          </Col>
        </Row>
      </Container>
    );
  } else if (!loggedIn) {
    return (
      <Container fluid="true">
        <Row noGutters>
          <Col xs={12}>
            <h1 className="display-4">Cart</h1>
          </Col>
          <Col xs={12}>
            <div className="lead">
              <NavLink to="/login">Login</NavLink> to add items to cart
            </div>
          </Col>
        </Row>
      </Container>
    );
  } else {
    return (
      <Container fluid="true">
        <Row noGutters>
          <Col xs={12}>
            <h1 className="display-4">Cart</h1>
          </Col>
          <Col xs={12}>
            <Cart />
          </Col>
        </Row>
      </Container>
    );
  }
}

export default withRouter(Cart);
