import * as webpack from 'webpack';

export default {
  plugins: [],
  optimization: {
    runtimeChunk: false,
    splitChunks: {
      cacheGroups: {
        default: false,
      },
    },
  },
  output: {
    filename: 'static/js/[name].js',
  },
} as webpack.Configuration;
