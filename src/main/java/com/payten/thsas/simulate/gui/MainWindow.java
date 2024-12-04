package com.payten.thsas.simulate.gui;

import com.payten.thsas.simulate.ISOContent;
import com.payten.thsas.simulate.ISOSender;
import com.payten.thsas.simulate.config.Config;
import com.payten.thsas.simulate.config.Constants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainWindow extends JFrame {

  private static Logger log = LogManager.getLogger(MainWindow.class);

  private ActionListener actionListener;

  private JPanel mainPanel;
  private JTextField ipTextField;
  private JTextField portTextField;
  private JTextField nameLenTextField;
  private JTextField sizeLenTextField;
  private JTextField packagerTextField;
  private JCheckBox send60AsHexCheckBox;
  private JCheckBox get60AsHexCheckBox;
  private JCheckBox send61AsHexCheckBox;
  private JCheckBox get61AsHexCheckBox;

  JTextArea inputTextArea;
  JTextArea outputTextArea;

  private JButton sendButton;
  private JButton readIsoFileButton;
  private JButton saveIsoFileButton;
  private JButton readConfigButton;
  private JButton saveConfigButton;
  private JButton clearOutputButton;
  private JButton saveOutputButton;

  private int windowHeight = 600;
  private int windowWidth = 1100;

  public MainWindow() {
    super();
    init();
    populateConfig();
    populateIsoMsg();
    initConnections();
  }

  private void populateIsoMsg() {
    populateIsoMsg(Config.ISO_MSG);
  }

  private void populateIsoMsg(String isoMsg) {
    ISOContent isoContent = new ISOContent();
    isoContent.readFile(isoMsg);
    inputTextArea.setText(isoContent.getAsString());

    revalidate();
    repaint();
  }

  private void populateConfig() {

    if (Constants.DEBUG) {
      log.debug("DEBUG MODE ON");
      String configPath = "./src/main/config/";
      Config.readConfig(configPath + "config_debug.txt");
    } else {
      Config.readConfig();
    }
    if (Config.URL != null) {
      ipTextField.setText(Config.URL);
    }
    if (Config.PORT != null) {
      portTextField.setText(String.valueOf(Config.PORT));
    }
    if (Config.TAG_NAME_LEN != null) {
      nameLenTextField.setText(String.valueOf(Config.TAG_NAME_LEN));
    }
    if (Config.TAG_SIZE_LEN != null) {
      sizeLenTextField.setText(String.valueOf(Config.TAG_SIZE_LEN));
    }
    if (Config.PACKAGER != null) {
      packagerTextField.setText(Config.PACKAGER);
    }
    if (Config.SEND_60_AS_HEX != null) {
      send60AsHexCheckBox.setSelected(Config.SEND_60_AS_HEX);
    }
    if (Config.SEND_61_AS_HEX != null) {
      send61AsHexCheckBox.setSelected(Config.SEND_61_AS_HEX);
    }
    if (Config.GET_60_AS_HEX != null) {
      get60AsHexCheckBox.setSelected(Config.GET_60_AS_HEX);
    }
    if (Config.GET_61_AS_HEX != null) {
      get61AsHexCheckBox.setSelected(Config.GET_61_AS_HEX);
    }
    revalidate();
    repaint();
  }

  private void updateConfig() {
    Config.URL = ipTextField.getText();
    Config.PORT = Integer.valueOf(portTextField.getText());
    Config.TAG_NAME_LEN = Integer.valueOf(nameLenTextField.getText());
    Config.TAG_SIZE_LEN = Integer.valueOf(sizeLenTextField.getText());
    Config.SEND_60_AS_HEX = send60AsHexCheckBox.isSelected();
    Config.SEND_61_AS_HEX = send61AsHexCheckBox.isSelected();
    Config.GET_60_AS_HEX = get60AsHexCheckBox.isSelected();
    Config.GET_61_AS_HEX = get61AsHexCheckBox.isSelected();
  }

  private void init() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = (int) screenSize.getWidth();
    int screenHeight = (int) screenSize.getHeight();
    setSize(windowWidth, windowHeight);
    setTitle("SendISOMessageGUI");
    setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    mainPanel = new JPanel(new BorderLayout(5, 5));

    JPanel configPanel = new JPanel();
    configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));

    JPanel configPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

    JLabel ipLabel = new JLabel("IP:");
    configPanel1.add(ipLabel);
    ipTextField = new JTextField();
    ipTextField.setColumns(15);
    configPanel1.add(ipTextField);

    JLabel portLabel = new JLabel("PORT:");
    configPanel1.add(portLabel);
    portTextField = new JTextField();
    portTextField.setColumns(5);
    configPanel1.add(portTextField);

    JLabel nameLenLabel = new JLabel("Tag name len:");
    configPanel1.add(nameLenLabel);
    nameLenTextField = new JTextField();
    nameLenTextField.setColumns(3);
    configPanel1.add(nameLenTextField);

    JLabel sizeLenLabel = new JLabel("Tag size len:");
    configPanel1.add(sizeLenLabel);
    sizeLenTextField = new JTextField();
    sizeLenTextField.setColumns(3);
    configPanel1.add(sizeLenTextField);

    configPanel.add(configPanel1);
    JPanel configPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

    JLabel packagerLabel = new JLabel("Packager:");
    configPanel2.add(packagerLabel);
    packagerTextField = new JTextField();
    packagerTextField.setColumns(50);
    configPanel2.add(packagerTextField);

    send60AsHexCheckBox = new JCheckBox("Send F60 HEX?");
    configPanel2.add(send60AsHexCheckBox);

    get60AsHexCheckBox = new JCheckBox("Get F60 HEX?");
    configPanel2.add(get60AsHexCheckBox);

    send61AsHexCheckBox = new JCheckBox("Send F61 HEX?");
    configPanel2.add(send61AsHexCheckBox);

    get61AsHexCheckBox = new JCheckBox("Get F61 HEX?");
    configPanel2.add(get61AsHexCheckBox);

    configPanel.add(configPanel2);

    mainPanel.add(configPanel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    sendButton = new JButton("Send");
    buttonPanel.add(sendButton);
    readIsoFileButton = new JButton("Read ISO File");
    buttonPanel.add(readIsoFileButton);
    saveIsoFileButton = new JButton("Save ISO File");
    buttonPanel.add(saveIsoFileButton);
    readConfigButton = new JButton("Read Config");
    buttonPanel.add(readConfigButton);
    saveConfigButton = new JButton("Save Config");
    buttonPanel.add(saveConfigButton);
    clearOutputButton = new JButton("Clear Output");
    buttonPanel.add(clearOutputButton);
    saveOutputButton = new JButton("Save Output");
    buttonPanel.add(saveOutputButton);

    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    int textAreaWidth = 20;
    int textAreaHeight = 20;

    inputTextArea = new JTextArea();
    inputTextArea.setLineWrap(true);
    inputTextArea.setMinimumSize(new Dimension(textAreaWidth, textAreaHeight));

    JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
    inputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    inputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

    outputTextArea = new JTextArea();
    outputTextArea.setEditable(false);
    outputTextArea.setLineWrap(true);
    outputTextArea.setMinimumSize(new Dimension(textAreaWidth, textAreaHeight));

    JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
    outputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    outputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

    JPanel centralPanel = new JPanel();
    centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.X_AXIS));
    centralPanel.add(inputScrollPane);
    centralPanel.add(outputScrollPane);
    mainPanel.add(centralPanel, BorderLayout.CENTER);

    add(mainPanel);

    revalidate();
    repaint();
  }

  private void initConnections() {
    actionListener =
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendButton) {
              sendButtonPressed();
            }
            if (e.getSource() == readIsoFileButton) {
              readIsoFileButtonPressed();
            }
            if (e.getSource() == saveIsoFileButton) {
              saveIsoFileButtonPressed();
            }
            if (e.getSource() == readConfigButton) {
              readConfigButtonPressed();
            }
            if (e.getSource() == saveConfigButton) {
              saveConfigButtonPressed();
            }
            if (e.getSource() == clearOutputButton) {
              clearOutputButtonPressed();
            }
            if (e.getSource() == saveOutputButton) {
              saveOutputButtonPressed();
            }
          }
        };

    sendButton.addActionListener(actionListener);
    readIsoFileButton.addActionListener(actionListener);
    saveIsoFileButton.addActionListener(actionListener);
    readConfigButton.addActionListener(actionListener);
    saveConfigButton.addActionListener(actionListener);
    clearOutputButton.addActionListener(actionListener);
    saveOutputButton.addActionListener(actionListener);
  }

  protected void saveOutputButtonPressed() {
    String outputText = outputTextArea.getText();

    JFileChooser fileChooser = new JFileChooser();
    String configLocation = Constants.CONFIG_LOCATION;
    fileChooser.setCurrentDirectory(new File(configLocation));

    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    String fileName = "out_" + df.format(new Date()) + ".txt";
    log.debug("fileName {}", fileName);
    fileChooser.setSelectedFile(new File(fileName));

    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      FileWriter fw = null;
      try {
        File selectedFile = fileChooser.getSelectedFile();
        fw = new FileWriter(selectedFile);
        fw.write(outputText);

      } catch (IOException e) {
        log.error("IOException: {}", e.getLocalizedMessage());
      } finally {
        if (fw != null) {
          try {
            fw.close();
          } catch (IOException e) {
            log.error("IOException: {}", e.getLocalizedMessage());
          }
        }
      }
    }
  }

  protected void clearOutputButtonPressed() {
    outputTextArea.setText("");
    revalidate();
    repaint();
  }

  protected void readConfigButtonPressed() {
    populateConfig();
  }

  private void saveConfigButtonPressed() {
    if (ipTextField != null) {
      Config.URL = ipTextField.getText();
    }

    if (portTextField != null) {
      Config.PORT = Integer.valueOf(portTextField.getText());
    }

    if (nameLenTextField != null) {
      Config.TAG_NAME_LEN = Integer.valueOf(nameLenTextField.getText());
    }

    if (sizeLenTextField != null) {
      Config.TAG_SIZE_LEN = Integer.valueOf(sizeLenTextField.getText());
    }

    if (packagerTextField != null) {
      Config.PACKAGER = packagerTextField.getText();
    }

    if (get60AsHexCheckBox != null) {
      Config.GET_60_AS_HEX = get60AsHexCheckBox.isSelected();
    }

    if (send60AsHexCheckBox != null) {
      Config.SEND_60_AS_HEX = send60AsHexCheckBox.isSelected();
    }

    if (get61AsHexCheckBox != null) {
      Config.GET_61_AS_HEX = get61AsHexCheckBox.isSelected();
    }

    if (send61AsHexCheckBox != null) {
      Config.SEND_61_AS_HEX = send61AsHexCheckBox.isSelected();
    }

    Config.writeConfig();
  }

  protected void readIsoFileButtonPressed() {
    JFileChooser fileChooser = new JFileChooser();
    String configLocation = Constants.CONFIG_LOCATION;
    fileChooser.setCurrentDirectory(new File(configLocation));

    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      ISOContent input = new ISOContent();
      input.readFile(selectedFile.getAbsolutePath());

      inputTextArea.setText(input.getAsString());

      revalidate();
      repaint();
    }
  }

  protected void saveIsoFileButtonPressed() {
    String inputText = inputTextArea.getText();

    JFileChooser fileChooser = new JFileChooser();
    String configLocation = Constants.CONFIG_LOCATION;
    fileChooser.setCurrentDirectory(new File(configLocation));
    fileChooser.setSelectedFile(new File("iso_msg.txt"));

    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      FileWriter fw = null;
      try {
        fw = new FileWriter(selectedFile);
        fw.write(inputText);

      } catch (IOException e) {
        log.error("IOException: {}", e.getLocalizedMessage());
      } finally {
        if (fw != null) {
          try {
            fw.close();
          } catch (IOException e) {
            log.error("IOException: {}", e.getLocalizedMessage());
          }
        }
      }
    }
  }

  protected void sendButtonPressed() {
    updateConfig();
    ISOSender isoSender = new ISOSender();
    ISOContent input = new ISOContent();
    input.setAsString(inputTextArea.getText());

    isoSender.setInputMessage(input);
    isoSender.send();

    StringBuilder outputString = new StringBuilder();
    String previousOutput = outputTextArea.getText();
    if (!StringUtils.isBlank(previousOutput)) {
      outputString.append(previousOutput);
      outputString.append(Constants.NEW_LINE);
    }
    outputString.append(Constants.TRANSACTION_SEPARATOR);
    outputString.append(Constants.NEW_LINE);
    outputString.append(input.getAsString());
    outputString.append(System.lineSeparator());

    ISOContent output = isoSender.getOutputMessage();
    if (output != null) {
      outputString.append(output.getAsString());
    } else {
      outputString.append("null");
    }
    outputString.append(Constants.TRANSACTION_SEPARATOR);

    outputTextArea.setText(outputString.toString());
  }
}
