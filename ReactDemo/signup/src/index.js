import "react-app-polyfill/ie11";
import React from "react";
import ReactDOM from "react-dom";
import App from "./App";
import { unregister } from "./registerServiceWorker";

window.renderSignup = (containerId, history) => {
  ReactDOM.render(
    <App history={history} />,
    document.getElementById(containerId)
  );
  unregister();
};

window.unmountSignup = (containerId) => {
  ReactDOM.unmountComponentAtNode(document.getElementById(containerId));
};

if (!document.getElementById("Signup-container")) {
  ReactDOM.render(<App />, document.getElementById("container"));
  unregister();
}
