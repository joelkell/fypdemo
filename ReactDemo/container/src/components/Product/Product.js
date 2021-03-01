import React from "react";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import NoMatch from "../NoMatch/NoMatch";
import MicroFrontend from "../../MicroFrontend";

import "./Product.css";

const {
  REACT_APP_PRODUCT_HOST: productHost,
  REACT_APP_PRODUCTREVIEW_HOST: productReviewHost,
  REACT_APP_CART_HOST: cartHost,
} = process.env;

function Product({ history }) {
  return <MicroFrontend history={history} host={productHost} name="Product" />;
}

function Cart({ history }) {
  return <MicroFrontend history={history} host={cartHost} name="Cart" />;
}

function ProductReview({ history }) {
  return (
    <MicroFrontend
      history={history}
      host={productReviewHost}
      name="ProductReview"
    />
  );
}

class ProductPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      loading: true,
      error: false,
      product: null,
      errormessage: null,
    };
  }

  handleResponse = (response) => {
    if (response.status === 200) {
      return response.json().then((data) => {
        this.setState({
          product: data,
          loading: false,
        });
      });
    } else if (response.status === 404) {
      return response.json().then((data) => {
        this.setState({
          loading: false,
          error: true,
          errormessage: data.message,
        });
      });
    } else {
      this.setState({ loading: false, error: true });
    }
  };

  componentDidMount() {
    const id = this.props.match.params.id;

    fetch(`http://localhost:8080/products/${id}`).then((response) => {
      this.handleResponse(response);
    });
  }

  render() {
    if (this.state.loading) {
      return <div>loading</div>;
    }
    if (this.state.error) {
      return <NoMatch history={this.props.history} />;
    }

    return (
      <Container fluid="true">
        <Row noGutters>
          <Col xs={12} lg={8} className="container-product-container">
            <div key={this.props.match.params.id}>
              <Product history={this.props.history} />
            </div>
          </Col>
          <Col xs={12} lg={4} className="container-cart-container">
            <div key={this.props.match.params.id}>
              <Cart history={this.props.history} />
            </div>
          </Col>
          <Col xs={12} className="container-productReview-container">
            <div key={this.props.match.params.id}>
              <ProductReview history={this.props.history} />
            </div>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default ProductPage;
