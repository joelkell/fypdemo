// import React from "react";
// import { Router, Route, Switch } from "react-router-dom";
// import { createBrowserHistory } from "history";
// import Login from "./Login";

// const defaultHistory = createBrowserHistory();

// const App = ({ history = defaultHistory }) => (
//   <Router history={history}>
//     <Switch>
//       <Route exact path="/login" component={Login} />
//     </Switch>
//   </Router>
// );

// export default App;

import React from "react";
import { Router, Route, Switch } from "react-router-dom";
import { createBrowserHistory } from "history";
import Login from "./Login";

const defaultHistory = createBrowserHistory();

function setToken(userToken) {
  localStorage.setItem("token", JSON.stringify(userToken));
}

class App extends React.Component {
  render() {
    return (
      <Router history={this.props.history || defaultHistory}>
        <Switch>
          <Route path="/">
            <Login
              setToken={setToken}
              history={this.props.history || defaultHistory}
            />
          </Route>
        </Switch>
      </Router>
    );
  }
}

export default App;
