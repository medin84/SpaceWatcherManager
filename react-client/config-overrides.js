const path = require("path");

module.exports = (defaultConfig, env) => {
  const isEnvProduction = env === "production";

  const overrides = [
    config => ({
      ...config,
      mode: "development",
      output: {
        ...config.output,
        path: path.resolve(__dirname, "../src/main/resources/client"),
        filename: "static/js/[name].js",
        chunkFilename: "static/js/[name].chunk.js",
        publicPath: "/" // isEnvProduction ? process.env.PUBLIC_URL : "/"
      }
    })
  ];

  const _overrides = overrides.reduce(
    (config, func) => func(config, env),
    defaultConfig
  );
  _overrides.plugins[5].options.filename = "static/css/[name].css";
  _overrides.plugins[5].options.chunkFilename = "static/css/[name].css";
  // console.log(_overrides.plugins);

  return _overrides;
};
