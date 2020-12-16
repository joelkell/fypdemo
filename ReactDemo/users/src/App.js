import React, { useState, useEffect } from "react";
import logo from "./logo.svg";
import "./App.css";

function App() {
  const [userP, setUserP] = useState(null);

  const getAllUsers = () => {
    setUserP("");
    fetch(`http://localhost:8080/users`).then((response) => {
      return response
        .json()
        .then((data) => {
          setUserP(JSON.stringify(data));
        })
        .catch((err) => {
          console.log(err);
        });
    });
  };

  return (
    <div>
      <header>
        <h3>Users</h3>
        <div>
          <button onClick={() => getAllUsers()}>Get all Users</button>
        </div>
        <div>
          <p>{userP}</p>
        </div>
      </header>
    </div>
  );
}

export default App;
