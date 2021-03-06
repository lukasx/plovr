package org.plovr.cli;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.plovr.CompilationServer;
import org.plovr.Config;
import org.plovr.ConfigParser;
import org.plovr.ConfigParseException;

public class ServeCommand extends AbstractCommandRunner<ServeCommandOptions> {

  @Override
  ServeCommandOptions createOptions() {
    return new ServeCommandOptions();
  }

  @Override
  public int runCommandWithOptions(ServeCommandOptions options) throws IOException {
    List<String> arguments = options.getArguments();
    if (arguments.size() < 1) {
      printUsage();
      return 1;
    }

    CompilationServer server = new CompilationServer(options.getListenAddress(),
        options.getPort(),
        options.isHttps());
    // Register all of the configs.
    for (String arg : arguments) {
      File configFile = new File(arg);
      Config config;
      try {
        config = ConfigParser.parseFile(configFile);
      } catch (ConfigParseException e) {
        e.print(System.err);
        return 1;
      }
      server.registerConfig(config);
    }
    server.run();
    return STATUS_NO_EXIT;
  }

  @Override
  String getUsageIntro() {
    return "Specify a list of config files to serve.";
  }

}
