package com.payten.thsas.simulate;

import com.payten.thsas.simulate.config.Config;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOContent {

  private static Logger log = LogManager.getLogger(ISOContent.class);
  private static final String TYPE_FILE = "F";
  private static final String TYPE_TAG_60 = "T60";
  private static final String TYPE_TAG_61 = "T61";

  private Map<Integer, String> fields;
  private Map<String, String> f60tags;
  private Map<String, String> f61tags;
  private int maxField = -1;

  public ISOContent() {
    maxField = -1;
    fields = new HashMap<>();
    f60tags = new HashMap<>();
    f61tags = new HashMap<>();
  }

  public ISOContent(ISOMsg isoMsg) {
    maxField = -1;
    fields = new HashMap<>();
    f60tags = new HashMap<>();
    f61tags = new HashMap<>();

    if (isoMsg == null) {
      return;
    }
    maxField = isoMsg.getMaxField();

    log.debug("ISOMsg:");
    for (int i = 0; i <= isoMsg.getMaxField(); i++) {
      if (isoMsg.hasField(i)) {
        log.debug("F[{}]: {}", i, isoMsg.getValue(i));
        if (i == 60) {
          String field60 = (String) isoMsg.getValue(60);
          while (field60.length() > 0) {
            String name = field60.substring(0, Config.TAG_NAME_LEN);
            log.debug("tag name: {}", name);
            field60 = field60.substring(Config.TAG_NAME_LEN);
            String lenStr = field60.substring(0, Config.TAG_SIZE_LEN);
            int len = Integer.valueOf(lenStr);
            log.debug("tag len: {}", len);
            field60 = field60.substring(Config.TAG_SIZE_LEN);
            String value = field60.substring(0, len);
            log.debug("tag value: {}", value);
            field60 = field60.substring(len);
            f60tags.put(name, value);
          }
        } else if (i == 61) {
          String field61 = null;
          if (Config.GET_61_AS_HEX) {
            byte[] field61ByteArray = (byte[]) isoMsg.getValue(61);
            String field61Hex = byteArrayToHex(field61ByteArray);
            field61 = convertHexToString(field61Hex);

          } else {
            field61 = (String) isoMsg.getValue(61);
          }
          while (field61.length() > 0) {
            String name = field61.substring(0, Config.TAG_NAME_LEN);
            log.debug("tag name: {}", name);
            field61 = field61.substring(Config.TAG_NAME_LEN);
            String lenStr = field61.substring(0, Config.TAG_SIZE_LEN);
            int len = Integer.valueOf(lenStr);
            log.debug("tag len: {}", len);
            field61 = field61.substring(Config.TAG_SIZE_LEN);
            String value = field61.substring(0, len);
            log.debug("tag value: {}", value);
            field61 = field61.substring(len);
            f61tags.put(name, value);
          }

        } else {

          fields.put(i, (String) isoMsg.getValue(i));
        }
      }
    }
  }

  public void readFile() {
    readFile(Config.ISO_MSG);
  }

  public void readFile(String filepath) {
    log.info("readFile: {}", filepath);
    File isoFile = new File(filepath);

    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(isoFile));
      String line = br.readLine();
      log.debug("line: {}", line);

      while (line != null) {

        String type = null;
        if (line.contains(" ")) {
          type = line.substring(0, line.indexOf(" "));
          line = line.substring(line.indexOf(" ") + 1);
        }
        log.debug("type: {}", type);

        String name = null;
        if (line.contains(" ")) {
          name = line.substring(0, line.indexOf(" "));
          line = line.substring(line.indexOf(" ") + 1);
        }
        log.debug("name: {}", name);

        String content = line;
        log.debug("content: {}", content);

        if (!StringUtils.isEmpty(type)
            && !StringUtils.isEmpty(name)
            && !StringUtils.isEmpty(line)) {
          if (TYPE_FILE.equals(type)) {
            Integer fieldNumber = Integer.valueOf(name);
            if (fieldNumber != null) {
              if (fieldNumber > maxField) {
                maxField = fieldNumber;
              }
              fields.put(fieldNumber, content);
            }
          } else if (TYPE_TAG_60.equals(type)) {
            if (maxField < 60) {
              maxField = 60;
            }
            f60tags.put(name, content);
          } else if (TYPE_TAG_61.equals(type)) {
            if (maxField < 61) {
              maxField = 61;
            }
            f61tags.put(name, content);
          }
        }

        line = br.readLine();
      }

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

  public ISOMsg getISOMsg() throws ISOException {
    log.info("getISOMsg");

    if (fields == null || fields.size() == 0) {
      return null;
    }
    ISOMsg isoMsg = new ISOMsg();
    if (fields.containsKey(0)) {
      log.debug("setting field 0 to {}", fields.get(0));
      isoMsg.setMTI(fields.get(0));
    }
    for (Integer fieldNumber : fields.keySet()) {
      log.debug("setting field {} to {}", fieldNumber, fields.get(fieldNumber));
      if (fieldNumber > 0) {
        isoMsg.set(fieldNumber, fields.get(fieldNumber));
      }
    }
    if (!fields.containsKey(60) && f60tags != null && f60tags.size() > 0) {
      String tagStr = composeTags(f60tags);
      log.debug("setting field 60 to {}", tagStr);
      isoMsg.set(60, tagStr);
    }
    if (!fields.containsKey(61) && f61tags != null && f61tags.size() > 0) {
      String tagStr = composeTags(f61tags);
      log.debug("setting field 61 to {}", tagStr);
      isoMsg.set(61, tagStr);
    }

    log.debug("ISOMsg:");
    for (int i = 0; i <= isoMsg.getMaxField(); i++) {
      if (isoMsg.hasField(i)) {
        log.debug("F[" + i + "]: " + isoMsg.getValue(i));
      }
    }

    return isoMsg;
  }

  private String composeTags(Map<String, String> tags) {
    log.info("composeTags");
    if (tags == null || tags.isEmpty()) {
      return "";
    }
    StringBuilder tagsString = new StringBuilder("");
    for (String name : tags.keySet()) {
      log.debug("Setting tag {} to {}", name, tags.get(name));
      tagsString.append(name);
      String lenStr =
          StringUtils.leftPad(String.valueOf(tags.get(name).length()), Config.TAG_SIZE_LEN, "0");
      tagsString.append(lenStr);
      tagsString.append(tags.get(name));
    }

    return tagsString.toString();
  }

  public void printISOMessage() {

    for (int i = 0; i <= maxField; i++) {
      if (i == 60) {
        for (String name : f60tags.keySet()) {
          log.info("F60_Tag[" + name + "]: " + f60tags.get(name));
        }
      } else if(i == 61) {
        for (String name : f61tags.keySet()) {
          log.info("F61_Tag[" + name + "]: " + f61tags.get(name));
        }
      } else {
        if (fields.containsKey(i)) {
          log.info("Field[" + i + "]: " + fields.get(i));
        }
      }
    }
  }

  private static String byteArrayToHex(byte[] a) {
    StringBuilder sb = new StringBuilder(a.length * 2);
    for (byte b : a) sb.append(String.format("%02x", b));
    return sb.toString();
  }

  private static String convertHexToString(String hex) {

    StringBuilder sb = new StringBuilder();
    StringBuilder temp = new StringBuilder();

    // 49204c6f7665204a617661 split into two characters 49, 20, 4c...
    for (int i = 0; i < hex.length() - 1; i += 2) {

      // grab the hex in pairs
      String output = hex.substring(i, (i + 2));
      // convert hex to decimal
      int decimal = Integer.parseInt(output, 16);
      // convert the decimal to character
      sb.append((char) decimal);

      temp.append(decimal);
    }
    return sb.toString();
  }
}
