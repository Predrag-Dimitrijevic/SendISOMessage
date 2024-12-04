package com.payten.thsas.simulate;

import com.payten.thsas.simulate.config.Config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Hello world! */
public class App {
	private static Logger log = LogManager.getLogger(App.class);

  public static void main(String[] args) {
    log.info("Starting SendISOMessage");

    ISOSender sender = new ISOSender();
    ISOContent input = new ISOContent();

    Config.readConfig();

    input.readFile();
    log.info("Input: ");
    input.printISOMessage();
    sender.setInputMessage(input);
    sender.send();

    ISOContent output = sender.getOutputMessage();
    log.info("output: ");
    output.printISOMessage();
  }
}
