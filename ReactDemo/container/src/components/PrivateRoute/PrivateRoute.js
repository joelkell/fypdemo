import React from "react";
import { Route, Redirect, withRouter } from "react-router-dom";

function PrivateRoute({ component, history, ...rest }) {
  let child = component();
  let token = getToken();
  return (
    <Route
      {...rest}
      render={({ location }) =>
        token ? (
          child
        ) : (
          <Redirect to={{ pathname: "/login", state: { from: location } }} />
        )
      }
    />
  );
}

function getToken() {
  const tokenString = localStorage.getItem("token");
  const userToken = JSON.parse(tokenString);
  let currentTime = new Date();
  if (userToken) {
    if (currentTime.getTime() < Date.parse(userToken.expires_in)) {
      return userToken;
    } else {
      return null;
    }
  } else {
    return null;
  }
}

export default withRouter(PrivateRoute);
