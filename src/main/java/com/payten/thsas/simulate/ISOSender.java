package com.payten.thsas.simulate;

import com.payten.thsas.simulate.config.Config;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.GenericPackager;

public class ISOSender {

  public static final String NEW_LINE = System.getProperty("line.separator");

  private ISOMsg isoMsg = null;

  public ISOSender() {}

  public void send() {
    ISOMsg message = getMessage();
    try {
      ISOChannel channel =
          new ASCIIChannel(
              Config.URL,
              Config.PORT,
              (ISOPackager) new GenericPackager(new FileInputStream(Config.PACKAGER)));

      channel.connect();
      channel.send(message);

      ISOMsg response = channel.receive();

      System.out.println(dumpMessage(response));

    } catch (ISOException e) {
      System.err.println("ISOException: " + e.getMessage());
    } catch (FileNotFoundException e) {
      System.err.println("FileNotFoundException: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    }
  }

  public void setMessage(ISOContent isoContent) {
    try {
      isoMsg = isoContent.getISOMsg();
    } catch (ISOException e) {
      System.err.println("ISOException: " + e.getMessage());
    }
  }

  private ISOMsg getMessage() {
    return isoMsg;
  }

  public static String dumpMessage(ISOMsg m) {
    StringBuilder s = new StringBuilder();
    s.append("Message dump: " + NEW_LINE);
    if (m.getHeader() != null && m.getHeader().length > 0) {
      s.append("Header : " + ISOUtil.dumpString(m.getHeader()) + NEW_LINE);
    }

    for (int i = 0; i <= 128; i++) {

      if (m.hasField(i)) {
        String fieldString = "Field [" + i + "] : ";
        if (i == 2) {
          s.append(fieldString + maskPan(m.getString(i)) + NEW_LINE);
        } else if (i == 35) {
          s.append(fieldString + maskTrack2(m.getString(i)) + NEW_LINE);
        } else if (i == 14) {
          s.append(fieldString + "****" + NEW_LINE);
        } else if (i == 52) {
          s.append(fieldString + "********" + NEW_LINE);
        } else {
          s.append(fieldString + m.getString(i) + NEW_LINE);
        }
      }
    }
    return s.toString();
  }

  public static String maskPan(String pan) {
    if (pan != null && pan.length() >= 13 && pan.length() <= 19) {
      pan = pan.substring(0, 6) + "**********" + pan.substring(pan.length() - 4);
    }
    return pan;
  }

  public static String maskTrack2(String track2) {
    if (track2 != null && track2.contains("=")) {
      int clearLength = 6;
      if (track2.startsWith(";")) {
        clearLength = 7;
      }

      track2 =
          track2.substring(0, clearLength)
              + "**********"
              + track2.substring(track2.indexOf('=') - 4, track2.indexOf('=') + 1)
              + "***************";
    }
    return track2;
  }
}
