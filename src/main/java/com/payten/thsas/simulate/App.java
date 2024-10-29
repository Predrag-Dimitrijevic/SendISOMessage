package com.payten.thsas.simulate;

import com.payten.thsas.simulate.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Hello world! */
public class App {
  private static final boolean DEBUG = false;
	private static Logger log = LogManager.getLogger(App.class);

  public static void main(String[] args) {
    log.info("Starting SendISOMessage");

    ISOSender sender = new ISOSender();
    ISOContent content = new ISOContent();

    if (DEBUG) {
      log.debug("DEBUG MODE ON");
      String configPath = "./src/main/config/";
      Config.readConfig(configPath + "config_debug.txt");
    } else {
      Config.readConfig();
    }

    content.readFile();
    sender.setMessage(content);
    sender.send();
  }
}
