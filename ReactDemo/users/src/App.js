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
          var users = [];

          for (var i = 0; i < data.length; i++) {
            let u = new User();
            u._id = data[i]._id;
            u.username = data[i].username;
            u.email = data[i].email;
            u.password = data[i].password;

            console.log("User: " + i + " " + JSON.stringify(u));

            users.push(u);
          }
          setUserP(JSON.stringify(users));
        })
        .catch((err) => {
          console.log(err);
        });
    });
  };

  class User {
    constructor(_id, username, email, password) {
      this._id = _id;
      this.username = username;
      this.email = email;
      this.password = password;
    }
  }

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
