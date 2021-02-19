import React from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import MicroFrontend from "./MicroFrontend";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Product from "./components/Product/Product";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import PublicRoute from "./components/PublicRoute/PublicRoute";
import NoMatch from "./components/NoMatch/NoMatch";
import LoginPage from "./components/LoginPage/LoginPage";
import SignupPage from "./components/SignupPage/SignupPage";
import AccountPage from "./components/AccountPage/AccountPage";

import "./App.css";

const { REACT_APP_BROWSE_HOST: browseHost } = process.env;

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
          <PublicRoute exact path="/login" component={LoginPage} />
          <PublicRoute exact path="/signup" component={SignupPage} />
          <Route exact path="/cart">
            <div>cart</div>
          </Route>
          <PrivateRoute exact path="/myaccount" component={AccountPage} />
          <Route exact path="/products/:id" component={Product} />
          <Route path="*" component={NoMatch} />
        </Switch>
        <Footer />
      </React.Fragment>
    </BrowserRouter>
  );
};

export default App;
