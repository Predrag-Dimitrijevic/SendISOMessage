package com.payten.thsas.simulate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.payten.thsas.simulate.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOContent {

  private static final String TYPE_FILE = "F";
  private static final String TYPE_TAG = "T";
  
  private Map<Integer, String> fields;
  private Map<String, String> tags;

  public ISOContent() {
    fields = new HashMap<>();
    tags = new HashMap<>();
  }

  public void readFile() {
    readFile(Config.ISO_MSG);

  }

  public void readFile(String filepath) {
    File isoFile = new File(filepath);
 
    BufferedReader br = null;
    int maxField = -1;
    try {
      br = new BufferedReader(new FileReader(isoFile));
      String line = br.readLine();

      while (line != null) {

        String type = null;
        if(line.contains(" ")) {
          type = line.substring(0, line.indexOf(" "));
          line = line.substring(line.indexOf(" ") + 1);
        }

        String name = null;
        if(line.contains(" ")) {
          name = line.substring(0, line.indexOf(" "));
          line = line.substring(line.indexOf(" ") + 1);
        }
        String content = line;

        if(!StringUtils.isEmpty(type) && !StringUtils.isEmpty(name) && !StringUtils.isEmpty(line)) {
          if(TYPE_FILE.equals(type)) {
            Integer fieldNumber = Integer.valueOf(name);
            if(fieldNumber != null) {
              if(fieldNumber > maxField) {
                maxField = fieldNumber;
              }
              fields.put(fieldNumber, content);
            }
          } else if(TYPE_TAG.equals(type)) {
            if(maxField < 60) {
              maxField = 60;
            }
            tags.put(name, content);
          }
        }

        line = br.readLine();
      }

    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    } finally {
      try {
        if (br != null) br.close();
      } catch (IOException e) {
        System.err.println("IOException: " + e.getMessage());
      }
    }

    for (int i = 0; i <= maxField; i++) {
      if(i != 60) {
        if(fields.containsKey(i)) {
          System.out.println("Field[" + i + "]: " + fields.get(i));
        }
      } else {
        for (String name : tags.keySet()) {
          System.out.println("Tag[" + name + "]: " + tags.get(name));
        }

      }
      
    }
  }

  public ISOMsg getISOMsg() throws ISOException {
    if(fields == null || fields.size() == 0) {
      return null;
    }
    ISOMsg isoMsg = new ISOMsg();
    if(fields.containsKey(0)) {
      isoMsg.setMTI(fields.get(0));
    }
    for (Integer fieldNumber : fields.keySet()) {
      if(fieldNumber != 1) {
        isoMsg.set(fieldNumber, fields.get(fieldNumber));
      }
    }
    if(!fields.containsKey(60) && tags != null && tags.size() > 0) {
      isoMsg.set(60, composeTags());
    }

    System.out.println("ISOMsg:");
    for(int i = 0; i <= isoMsg.getMaxField(); i++) {
      if(isoMsg.hasField(i)) {
        System.out.println("F[" + i + "]: " + isoMsg.getValue(i));
      }
    }
    


    return isoMsg;
  }

  private String composeTags() {
    if(tags == null && tags.isEmpty()) {
      return "";
    }
    StringBuilder tagsString = new StringBuilder("");
    for (String name : tags.keySet()) {
      tagsString.append(name);
      String lenStr = StringUtils.leftPad(String.valueOf(tags.get(name).length()), Config.TAG_NAME_LEN, "0");
      tagsString.append(lenStr);
      tagsString.append(tags.get(name));
    }

    return tagsString.toString();

  }
}
