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
          setProductP(JSON.stringify(data));
        })
        .catch((err) => {
          console.log(err);
        });
    });
  };

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
