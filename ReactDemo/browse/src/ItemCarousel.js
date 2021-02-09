import React from "react";
import Carousel from "react-multi-carousel";
import Button from "react-bootstrap/Button";
import Item from "./Item";

import "./ItemCarousel.css";
import "react-multi-carousel/lib/styles.css";

const responsive = {
  desktop: {
    breakpoint: { max: 3000, min: 768 },
    items: 3,
    slidesToSlide: 1,
    partialVisibilityGutter: 30,
  },
  tablet: {
    breakpoint: { max: 768, min: 576 },
    items: 3,
    slidesToSlide: 1,
    partialVisibilityGutter: 20,
  },
  mobile: {
    breakpoint: { max: 576, min: 0 },
    items: 2,
    slidesToSlide: 1,
    partialVisibilityGutter: 30,
  },
};

const CustomRightArrow = ({ onClick, ...rest }) => {
  const {
    onMove,
    carouselState: { currentSlide, deviceType },
  } = rest;
  return (
    <Button
      variant="outline-dark"
      onClick={() => onClick()}
      className="itemCarousel-button itemCarousel-button-right"
    >
      {">"}
    </Button>
  );
};

const CustomLeftArrow = ({ onClick, ...rest }) => {
  const {
    onMove,
    carouselState: { currentSlide, deviceType },
  } = rest;
  return (
    <Button
      variant="outline-dark"
      onClick={() => onClick()}
      className="itemCarousel-button itemCarousel-button-left"
    >
      {"<"}
    </Button>
  );
};

class ItemCarousel extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      loading: true,
      error: false,
      products: null,
    };
  }

  handleResponse = (response) => {
    if (response.status === 200) {
      return response.json().then((data) => {
        this.setState({
          products: data,
          loading: false,
        });
      });
    } else {
      this.setState({ loading: false, error: true });
    }
  };

  componentDidMount() {
    const category = this.props.category;

    fetch(`http://localhost:8080/products/category/${category}`).then(
      (response) => {
        this.handleResponse(response);
      }
    );
  }

  render() {
    if (this.state.loading) {
      return <div>loading</div>;
    }
    if (this.state.error) {
      return <div>An Unknown Error has Occured</div>;
    }

    return (
      <Carousel
        responsive={responsive}
        additionalTransfrom={0}
        arrows={true}
        autoPlaySpeed={3000}
        centerMode={false}
        containerClass="container-padding-bottom"
        customRightArrow={<CustomRightArrow />}
        customLeftArrow={<CustomLeftArrow />}
        dotListClass=""
        draggable
        focusOnSelect={false}
        infinite={true}
        itemClass="react-multi-carousel-item"
        keyBoardControl={false}
        minimumTouchDrag={80}
        renderButtonGroupOutside={false}
        renderDotsOutside
        partialVisible={true}
        showDots
        sliderClass=""
        slidesToSlide={1}
        swipeable
        className="item-carousel"
      >
        {this.state.products.map((product) => (
          <Item
            product={product}
            history={this.props.history}
            key={product._id}
          />
        ))}
      </Carousel>
    );
  }
}

export default ItemCarousel;
