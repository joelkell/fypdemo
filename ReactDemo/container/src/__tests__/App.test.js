import React from "react";
import { mount } from "enzyme";
import App from "../App";

jest.mock("../MicroFrontend", () => {
  const MicroFrontend = () => <div />;
  return MicroFrontend;
});

describe("App", () => {
  const getCurrentRoute = (app) =>
    app.find("Router").prop("history").location.pathname;

  it("can render the browse micro frontend", () => {
    const app = mount(<App />);

    expect(getCurrentRoute(app)).toEqual("/");
    expect(app.find("MicroFrontend")).toHaveProp({ name: "Browse" });
  });
});
