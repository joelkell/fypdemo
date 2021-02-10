import React from "react";
import { useHistory } from "react-router-dom";
import MicroFrontend from "../../MicroFrontend";

const { REACT_APP_SIGNUP_HOST: signupHost } = process.env;

function Signup({ history }) {
  return <MicroFrontend history={history} host={signupHost} name="Signup" />;
}

const SignupPage = () => {
  let history = useHistory();
  return <Signup history={history} />;
};

export default SignupPage;
