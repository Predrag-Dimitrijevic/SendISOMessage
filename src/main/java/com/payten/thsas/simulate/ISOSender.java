package com.payten.thsas.simulate;

import com.payten.thsas.simulate.config.Config;
import com.payten.thsas.simulate.config.Constants;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ISOSender {

	private static Logger log = LogManager.getLogger(ISOSender.class);


  private ISOMsg isoMsgIn = null;
  private ISOMsg isoMsgOut = null;
  

  public ISOSender() {}

  public void send() {
    ISOMsg message = getIsoMsgIn();
    try {
      ISOChannel channel =
          new ASCIIChannel(
              Config.URL,
              Config.PORT,
              (ISOPackager) new GenericPackager(new FileInputStream(Config.PACKAGER)));

      channel.connect();
      channel.send(message);

      ISOMsg response = channel.receive();
      setIsoMsgOut(response);

      // log.info(dumpMessage(response));

    } catch (ISOException e) {
      log.error("ISOException: " + e.getMessage());
    } catch (FileNotFoundException e) {
      log.error("FileNotFoundException: " + e.getMessage());
    } catch (IOException e) {
      log.error("IOException: " + e.getMessage());
    }
  }

  public void setInputMessage(ISOContent input) {
    try {
      isoMsgIn = input.getISOMsg();
    } catch (ISOException e) {
      log.error("ISOException: " + e.getMessage());
    }
  }

  public ISOContent getOutputMessage() {
    return new ISOContent(isoMsgOut);
    
  }

  public static String dumpMessage(ISOMsg m) {
    StringBuilder s = new StringBuilder();
    s.append("Message dump: " + Constants.NEW_LINE);
    if (m.getHeader() != null && m.getHeader().length > 0) {
      s.append("Header : " + ISOUtil.dumpString(m.getHeader()) + Constants.NEW_LINE);
    }

    for (int i = 0; i <= 128; i++) {

      if (m.hasField(i)) {
        String fieldString = "Field [" + i + "] : ";
        if (i == 2) {
          s.append(fieldString + maskPan(m.getString(i)) + Constants.NEW_LINE);
        } else if (i == 35) {
          s.append(fieldString + maskTrack2(m.getString(i)) + Constants.NEW_LINE);
        } else if (i == 14) {
          s.append(fieldString + "****" + Constants.NEW_LINE);
        } else if (i == 52) {
          s.append(fieldString + "********" + Constants.NEW_LINE);
        } else {
          s.append(fieldString + m.getString(i) + Constants.NEW_LINE);
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



  public static Logger getLog() {
    return log;
  }



  public static void setLog(Logger log) {
    ISOSender.log = log;
  }



  public static String getNewLine() {
    return Constants.NEW_LINE;
  }



  public ISOMsg getIsoMsgIn() {
    return isoMsgIn;
  }



  public void setIsoMsgIn(ISOMsg isoMsgIn) {
    this.isoMsgIn = isoMsgIn;
  }



  public ISOMsg getIsoMsgOut() {
    return isoMsgOut;
  }



  public void setIsoMsgOut(ISOMsg isoMsgOut) {
    this.isoMsgOut = isoMsgOut;
  }
}
