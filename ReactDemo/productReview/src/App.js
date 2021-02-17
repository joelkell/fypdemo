import React from "react";
import { Router, Route, Switch } from "react-router-dom";
import { createBrowserHistory } from "history";
import ProductReview from "./ProductReview";

const defaultHistory = createBrowserHistory();

class App extends React.Component {
  render() {
    return (
      <Router
        history={this.props.history || defaultHistory}
        location={this.props.location}
      >
        <Switch>
          <Route path="*/:id">
            <ProductReview
              history={this.props.history || defaultHistory}
              location={this.props.location}
            />
          </Route>
        </Switch>
      </Router>
    );
  }
}

export default App;
