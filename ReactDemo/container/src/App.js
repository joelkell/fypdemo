import React, { useState } from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import MicroFrontend from "./MicroFrontend";
import Header from "./components/Header/Header";
import "bootstrap/dist/css/bootstrap.min.css";

import "./App.css";

const { REACT_APP_LOGIN_HOST: loginHost } = process.env;

function Login({ history }) {
  return <MicroFrontend history={history} host={loginHost} name="Login" />;
}

function Home() {
  return (
    <div>
      <div className="home">
        <div>home</div>
      </div>
    </div>
  );
}

const App = () => (
  <BrowserRouter>
    <React.Fragment>
      <Header />
      <Switch>
        <Route exact path="/" component={Home} />
        {/* <Route exact path="/cart" component={Users} /> */}
        <Route exact path="/login" component={Login} />
        {/* <Route exact path="/signup">
            <div>singup</div>
          </Route>
          <Route exact path="/logout">
            <div>logout</div>
          </Route>
          <Route exact path="/myaccount">
            <div>myaccount</div>
          </Route> */}
      </Switch>
    </React.Fragment>
  </BrowserRouter>
);

export default App;
