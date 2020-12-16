import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./App";
import * as serviceWorker from "./serviceWorker";

window.renderUsers = (containerId, history) => {
  ReactDOM.render(
    <App history={history} />,
    document.getElementById(containerId)
  );
  serviceWorker.unregister();
};

window.unmountUsers = (containerId) => {
  ReactDOM.unmountComponentAtNode(document.getElementById(containerId));
};

if (!document.getElementById("Users-container")) {
  ReactDOM.render(<App />, document.getElementById("root"));
  serviceWorker.unregister();
}
