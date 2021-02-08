import React from "react";

class Product extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      loading: true,
      error: false,
      name: null,
      description: null,
      stockLevel: null,
      price: null,
      categories: null,
      errormessage: null,
    };
  }

  handleResponse = (response) => {
    if (response.status === 200) {
      return response.json().then((data) => {
        console.log(data);
        this.setState({
          name: data.name,
          description: data.description,
          stockLevel: data.stockLevel,
          price: data.price,
          categories: data.categories,
          loading: false,
        });
        console.log(this.state.categories);
      });
    } else if (response.status === 404) {
      return response.json().then((data) => {
        console.log(data);
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
      //return "Loading";
    }
    if (this.state.error) {
      return <div>{this.state.errormessage}</div>;
      //return "Sorry, but that restaurant is currently unavailable.";
    }

    return (
      <div>
        <div>name: {this.state.name}</div>
        <div>description: {this.state.description}</div>
      </div>
    );
  }
}

export default Product;
