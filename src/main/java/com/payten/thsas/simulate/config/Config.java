package com.payten.thsas.simulate.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
  private static Logger log = LogManager.getLogger(Config.class);

  private static final String CONFIG_LOCATION = "../config/config.txt";

  public static String URL = null;
  public static Integer PORT = null;
  public static String PACKAGER = null;
  public static String ISO_MSG = null;
  public static Integer TAG_NAME_LEN = null;
  public static Integer TAG_SIZE_LEN = null;
  public static Boolean SEND_60_AS_HEX = null;
  public static Boolean SEND_61_AS_HEX = null;
  public static Boolean GET_60_AS_HEX = null;
  public static Boolean GET_61_AS_HEX = null;

  public static void readConfig(String filePath) {
    String currentDir = System.getProperty("user.dir");
    log.info("Current dir using System:" + currentDir);

    File configFile = new File(filePath);
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(configFile));
      String line = br.readLine();
      while (line != null) {
        int blancIndex = line.indexOf(' ');
        if (blancIndex <= 0) {
          line = br.readLine();
          continue;
        }
        String name = line.substring(0, blancIndex);
        String value = line.substring(blancIndex + 1);

        if ("URL".equals(name)) {
          URL = value;
        }
        if ("PORT".equals(name)) {
          PORT = Integer.valueOf(value);
        }
        if ("PACKAGER".equals(name)) {
          PACKAGER = value;
        }
        if ("ISO_MSG".equals(name)) {
          ISO_MSG = value;
        }
        if ("TAG_NAME_LEN".equals(name)) {
          TAG_NAME_LEN = Integer.valueOf(value);
        }
        if ("TAG_SIZE_LEN".equals(name)) {
          TAG_SIZE_LEN = Integer.valueOf(value);
        }
        if ("SEND_60_AS_HEX".equals(name)) {
          SEND_60_AS_HEX = Boolean.valueOf(value);
        }
        if ("SEND_61_AS_HEX".equals(name)) {
          SEND_61_AS_HEX = Boolean.valueOf(value);
        }
        if ("GET_60_AS_HEX".equals(name)) {
          GET_60_AS_HEX = Boolean.valueOf(value);
        }
        if ("GET_61_AS_HEX".equals(name)) {
          GET_61_AS_HEX = Boolean.valueOf(value);
        }
        line = br.readLine();
      }
      log.info("URL: " + URL);
      log.info("PORT: " + PORT);
      log.info("PACKAGER: " + PACKAGER);
      log.info("ISO_MSG: " + ISO_MSG);
      log.info("TAG_NAME_LEN: " + TAG_NAME_LEN);
      log.info("TAG_SIZE_LEN: " + TAG_SIZE_LEN);
      log.info("SEND_60_AS_HEX: " + SEND_60_AS_HEX);
      log.info("SEND_61_AS_HEX: " + SEND_61_AS_HEX);
      log.info("GET_60_AS_HEX: " + GET_60_AS_HEX);
      log.info("GET_61_AS_HEX: " + GET_61_AS_HEX);
    } catch (IOException e) {
      log.error("IOException: " + e.getMessage());
    } finally {
      try {
        if (br != null) br.close();
      } catch (IOException e) {
        log.error("IOException: " + e.getMessage());
      }
    }
  }

  public static void readConfig() {
    readConfig(CONFIG_LOCATION);
  }

  public static void writeConfig(String filePath) {
    String currentDir = System.getProperty("user.dir");
    log.info("Current dir using System:" + currentDir);

    File configFile = new File(filePath);
    if (configFile.exists()) {
      configFile.delete();
    }
    BufferedWriter bw = null;
    try {
      configFile.createNewFile();
      bw = new BufferedWriter(new FileWriter(configFile));
      bw.write("URL " + URL);
      bw.newLine();
      bw.write("PORT " + PORT);
      bw.newLine();
      bw.write("PACKAGER " + PACKAGER);
      bw.newLine();
      bw.write("ISO_MSG " + ISO_MSG);
      bw.newLine();
      bw.write("TAG_NAME_LEN " + TAG_NAME_LEN);
      bw.newLine();
      bw.write("TAG_SIZE_LEN " + TAG_SIZE_LEN);
      bw.newLine();
      bw.write("SEND_60_AS_HEX " + SEND_60_AS_HEX);
      bw.newLine();
      bw.write("SEND_61_AS_HEX " + SEND_61_AS_HEX);
      bw.newLine();
      bw.write("GET_60_AS_HEX " + GET_60_AS_HEX);
      bw.newLine();
      bw.write("GET_61_AS_HEX " + GET_61_AS_HEX);
    } catch (IOException e) {
      log.error("IOException: " + e.getMessage());
    } finally {
      try {
        if (bw != null) bw.close();
      } catch (IOException e) {
        log.error("IOException: " + e.getMessage());
      }
    }
  }

  public static void writeConfig() {
    writeConfig(CONFIG_LOCATION);
  }
}
