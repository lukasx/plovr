package org.plovr;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.SourceMap;
import com.sun.net.httpserver.HttpServer;

public final class CompilationServer implements Runnable {

  private final int port;

  // All maps are keyed a Config id rather than a Config because there could be
  // multiple, different Config objects with the same id because of how query
  // data can be used to redefine a Config for an individual request.

  /**
   * Map of config ids to original Config objects.
   */
  private final Map<String, Config> configs;

  /**
   * Map of config ids to the SourceMap from the last compilation.
   */
  private final Map<String, SourceMap> sourceMaps;

  /**
   * Map of config ids to the exports from the last compilation.
   */
  private final Map<String, String> exports;

  public CompilationServer(int port) {
    this.port = port;
    this.configs = Maps.newHashMap();
    this.sourceMaps = Maps.newHashMap();
    this.exports = Maps.newHashMap();
  }

  public void registerConfig(Config config) {
    String id = config.getId();
    if (configs.containsKey(id)) {
      throw new IllegalArgumentException(
          "A config with this id has already been registered: " + id);
    }
    configs.put(id, config);
  }

  @Override
  public void run() {
    InetSocketAddress addr = new InetSocketAddress(port);
    HttpServer server;
    try {
      server = HttpServer.create(addr, 0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    server.createContext("/compile", new CompileRequestHandler(this));
    server.createContext("/externs", new ExternsHandler(this));
    server.createContext("/input", new InputFileHandler(this));
    server.createContext("/size", new SizeHandler(this));
    server.createContext("/sourcemap", new SourceMapHandler(this));
    server.createContext("/view", new ViewFileHandler(this));
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
  }

  public boolean containsConfigWithId(String id) {
    return configs.containsKey(id);
  }

  public Config getConfigById(String id) {
    return configs.get(id);
  }

  /** Records the SourceMap from the last compilation for the config. */
  public void recordSourceMap(Config config, SourceMap sourceMap) {
    sourceMaps.put(config.getId(), sourceMap);
  }

  public SourceMap getSourceMapFor(Config config) {
    return sourceMaps.get(config.getId());
  }

  /** Records the exported externs from the last compilation for the config. */
  public void recordExportsAsExterns(Config config, String exportJs) {
    exports.put(config.getId(), exportJs);
  }

  public String getExportsAsExternsFor(Config config) {
    return exports.get(config.getId());
  }
}