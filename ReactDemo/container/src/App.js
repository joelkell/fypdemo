import React from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import MicroFrontend from "./MicroFrontend";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Product from "./components/Product/Product";
import Browse from "./components/Browse/Browse";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import NoMatch from "./components/NoMatch/NoMatch";

import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

const { REACT_APP_LOGIN_HOST: loginHost } = process.env;

function Login({ history }) {
  return <MicroFrontend history={history} host={loginHost} name="Login" />;
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
          <Route exact path="/product/:id" component={Product} />
          <Route path="*" component={NoMatch} />
        </Switch>
        <Footer />
      </React.Fragment>
    </BrowserRouter>
  );
};

export default App;
