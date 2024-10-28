package com.payten.thsas.simulate;

import com.payten.thsas.simulate.config.Config;

/** Hello world! */
public class App {
  private static final boolean DEBUG = true;

  public static void main(String[] args) {
    System.out.println("Hello World!");

    ISOSender sender = new ISOSender();
    ISOContent content = new ISOContent();

    if (DEBUG) {
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
