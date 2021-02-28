import React from "react";
import { Router, Route, Switch } from "react-router-dom";
import { createBrowserHistory } from "history";
import AccountOrder from "./AccountOrder";

const defaultHistory = createBrowserHistory();

class App extends React.Component {
  render() {
    return (
      <Router history={this.props.history || defaultHistory}>
        <Switch>
          <Route path="/">
            <AccountOrder history={this.props.history || defaultHistory} />
          </Route>
        </Switch>
      </Router>
    );
  }
}

export default App;
