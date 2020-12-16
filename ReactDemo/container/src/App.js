import React, { useState } from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import { createBrowserHistory } from "history";
import MicroFrontend from "./MicroFrontend";

import "./App.css";

const defaultHistory = createBrowserHistory();

const {
  REACT_APP_PRODUCTS_HOST: productsHost,
  REACT_APP_USERS_HOST: usersHost,
} = process.env;

function Header() {
  return (
    <div className="banner">
      <h1 className="banner-title">
        Micro Frontend Container Application Prototype
      </h1>
      <h4>Users and Products Services</h4>
    </div>
  );
}

function Users({ history }) {
  return <MicroFrontend history={history} host={usersHost} name="Users" />;
}

function Products({ history }) {
  return (
    <MicroFrontend history={history} host={productsHost} name="Products" />
  );
}

function Home({ history }) {
  const [input, setInput] = useState("");

  return (
    <div>
      <Header />
      <div className="home">
        <div className="content">
          <div className="user">
            <Users />
          </div>
          <div class="border"></div>
          <div className="product">
            <Products />
          </div>
        </div>
      </div>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <React.Fragment>
        <Switch>
          <Route exact path="/" component={Home} />
        </Switch>
      </React.Fragment>
    </BrowserRouter>
  );
}

export default App;
