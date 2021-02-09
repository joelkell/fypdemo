import React from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import MicroFrontend from "./MicroFrontend";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Product from "./components/Product/Product";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import NoMatch from "./components/NoMatch/NoMatch";

import "./App.css";

const {
  REACT_APP_LOGIN_HOST: loginHost,
  REACT_APP_BROWSE_HOST: browseHost,
} = process.env;

function Login({ history }) {
  return <MicroFrontend history={history} host={loginHost} name="Login" />;
}

function Browse({ history }) {
  return <MicroFrontend history={history} host={browseHost} name="Browse" />;
}

const App = () => {
  return (
    <BrowserRouter>
      <React.Fragment>
        <Header />
        <Switch>
          <Route exact path="/" component={Browse} />
          <Route exact path="/login" component={Login} />
          <Route exact path="/cart">
            <div>cart</div>
          </Route>
          <PrivateRoute exact path="/signup">
            <div>signup</div>
          </PrivateRoute>
          <Route exact path="/logout">
            <div>logout</div>
          </Route>
          <Route exact path="/myaccount">
            <div>myaccount</div>
          </Route>
          <Route exact path="/products/:id" component={Product} />
          <Route path="*" component={NoMatch} />
        </Switch>
        <Footer />
      </React.Fragment>
    </BrowserRouter>
  );
};

export default App;
