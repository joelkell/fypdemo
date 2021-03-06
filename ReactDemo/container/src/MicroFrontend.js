import React from "react";
import ReactDOM from "react-dom";

class MicroFrontend extends React.Component {
  componentDidMount() {
    const { name, host, document } = this.props;
    const scriptId = `micro-frontend-script-${name}`;
    const styleId = `micro-frontend-style-${name}`;

    if (document.getElementById(scriptId)) {
      this.renderMicroFrontend();
      return;
    }

    fetch(`${host}/asset-manifest.json`)
      .then((res) => res.json())
      .then((manifest) => {
        const style = document.createElement("link");
        style.id = styleId;
        style.rel = "stylesheet";
        style.crossOrigin = "";
        style.href = `${host}${manifest["files"]["main.css"]}`;
        document.head.appendChild(style);
        const script = document.createElement("script");
        script.id = scriptId;
        script.crossOrigin = "";
        script.src = `${host}${manifest["files"]["main.js"]}`;
        script.onload = this.renderMicroFrontend;
        document.head.appendChild(script);
      });
  }

  componentWillUnmount() {
    const { name, window } = this.props;

    if (typeof window[`unmount${name}`] === "function") {
      window[`unmount${name}`](`${name}-container`);
    } else {
      ReactDOM.unmountComponentAtNode(
        document.getElementById(`${name}-container`)
      );
    }
  }

  renderMicroFrontend = () => {
    const { name, window, history } = this.props;

    window[`render${name}`](`${name}-container`, history);
  };

  render() {
    return <main id={`${this.props.name}-container`} />;
  }
}

MicroFrontend.defaultProps = {
  document,
  window,
};

export default MicroFrontend;
