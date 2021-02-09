import React, { useEffect, useState } from "react";
import Card from "react-bootstrap/Card";

import "./Item.css";

import placeholder from "./placeholder-images.png";

function Item({ product, history }) {
  const [imageSource, setSource] = useState(
    require("./placeholder-images.png")
  );
  const redirect = () => {
    history.push(`/products/${product._id}`);
  };

  // A bug with react-multi-carousel means this will get called multiple times as
  // in infinite mode it duplicates react components with the same key
  useEffect(() => {
    fetch(`http://localhost:5002/images/${product._id}.jpg`).then(
      (response) => {
        if (response.status === 200) {
          setSource(`http://localhost:5002/images/${product._id}.jpg`);
        }
      }
    );
  }, []);

  return (
    <Card onClick={() => redirect()} className="item-Card">
      <Card.Img variant="top" src={imageSource} className="img-fluid" />
      <Card.Body className="d-flex justify-content-center">
        <Card.Title className="item-card-title">{product.name}</Card.Title>
      </Card.Body>
    </Card>
  );
}

export default Item;
