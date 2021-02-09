import React from "react";
import { Router, Route, Switch } from "react-router-dom";
import { createBrowserHistory } from "history";
import Browse from "./Browse";

const defaultHistory = createBrowserHistory();

class App extends React.Component {
  render() {
    return (
      <Router history={this.props.history || defaultHistory}>
        <Switch>
          <Route path="/" component={Browse} />
        </Switch>
      </Router>
    );
  }
}

export default App;
