import React from "react";
import { useHistory } from "react-router-dom";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import MicroFrontend from "../../MicroFrontend";

import "./CartPage.css";

const { REACT_APP_CART_HOST: cartHost } = process.env;

function Cart({ history }) {
  return <MicroFrontend history={history} host={cartHost} name="cart" />;
}

const CartPage = () => {
  let history = useHistory();
  return (
    <Container fluid="md">
      <Row>
        <Col xs={12} className="container-cart-container">
          <Cart history={history} />
        </Col>
      </Row>
    </Container>
  );
};

export default CartPage;
