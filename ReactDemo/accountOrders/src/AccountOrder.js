import React, { useState, useEffect } from "react";
import { withRouter, NavLink } from "react-router-dom";
import Container from "react-bootstrap/Container";
import Loader from "react-loader-spinner";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

import "./AccountOrder.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";

function AccountOrder() {
  const [orders, setOrders] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    let ignore = false;
    setLoading(true);
    let userToken = getToken();
    if (userToken) {
      let authToken = userToken.token_type + " " + userToken.access_token;
      fetch(`http://localhost:8080/orders/user/${userToken.username}`, {
        method: "GET",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: authToken,
        },
      }).then((response) => {
        response.json().then((data) => {
          if (!ignore) {
            data.forEach(async (order) => {
              order.orderItems.forEach(async (orderItem) => {
                orderItem.name = await getProductName(orderItem.productId);
              });
            });
            setOrders(data);
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

  const getProductName = async (productId) => {
    const response = await fetch(`http://localhost:8080/products/${productId}`);
    const data = await response.json();
    return data.name;
  };

  const OrderItems = (orderItems) => {
    return orderItems.orderItems.map((orderItem, index) => (
      <div
        className="lead d-flex flex-column justify-content-between accountOrder-order-items"
        key={index}
      >
        <NavLink to={`/products/${orderItem.productId}`}>
          {orderItem.name}
        </NavLink>
        <div>Quantity: {orderItem.quantity}</div>
      </div>
    ));
  };

  const getDate = (date) => {
    let orderDate = new Date(date);
    return (
      orderDate.getDate() +
      " " +
      new Intl.DateTimeFormat("en-IE", { month: "long" }).format(orderDate) +
      " " +
      orderDate.getFullYear()
    );
  };

  const Orders = () => {
    if (orders.length > 0) {
      return orders.map((order, index) => (
        <Col
          xs={12}
          className="d-flex flex-column justify-content-between accountOrder-order-div"
          key={index}
        >
          <div className="lead">
            <strong>Status: {order.status}</strong>
          </div>
          <div className="lead">Date: {getDate(order.timestamp)}</div>
          <div className="lead">Products:</div>
          <OrderItems orderItems={order.orderItems} />
        </Col>
      ));
    } else {
      return (
        <Col xs={12} className="accountOrder-order-div">
          <div className="lead">No Previous Orders</div>
        </Col>
      );
    }
  };

  if (!loading) {
    return (
      <Container fluid="true">
        <Row noGutters>
          <Col xs={12}>
            <h1>Orders</h1>
          </Col>
          <Col xs={12}>
            <Orders />
          </Col>
        </Row>
      </Container>
    );
  } else {
    return (
      <Container fluid="true" className="accountOrder-loading-container">
        <Row className="justify-content-xs-center">
          <Col>
            <Loader
              type="TailSpin"
              color="#343a40"
              height={200}
              width={200}
              className="accountOrder-spinner-center"
              timeout={1000}
            />
          </Col>
        </Row>
      </Container>
    );
  }
}

export default withRouter(AccountOrder);
