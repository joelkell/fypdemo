import React, { useState, useEffect } from "react";
import logo from "./logo.svg";
import "./App.css";

function App() {
  const [productP, setProductP] = useState(null);

  const getAllProducts = () => {
    setProductP("");
    fetch(`http://localhost:8080/products`).then((response) => {
      return response
        .json()
        .then((data) => {
          var products = [];

          for (var i = 0; i < data.length; i++) {
            let p = new Product();
            p._id = data[i]._id;
            p.name = data[i].name;
            p.description = data[i].description;
            p.stockLevel = data[i].stockLevel;
            p.price = data[i].price;
            p.categories = data[i].categories;

            console.log("Product: " + i + " " + JSON.stringify(p));

            products.push(p);
          }
          setProductP(JSON.stringify(products));
        })
        .catch((err) => {
          console.log(err);
        });
    });
  };

  class Product {
    constructor(_id, name, description, stockLevel, price, categories) {
      this._id = _id;
      this.name = name;
      this.description = description;
      this.stockLevel = stockLevel;
      this.price = price;
      this.categories = categories;
    }
  }

  return (
    <div>
      <header>
        <h3>Products</h3>
        <div>
          <button onClick={() => getAllProducts()}>Get all Products</button>
        </div>
        <div>
          <p>{productP}</p>
        </div>
      </header>
    </div>
  );
}

export default App;
