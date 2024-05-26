if(config.devServer) {
  config.devServer.historyApiFallback = {
    rewrites: [
      { from: /.*nav-sample-wasm.wasm/, to: '/nav-sample-wasm.wasm' },
    ]
  }
}
