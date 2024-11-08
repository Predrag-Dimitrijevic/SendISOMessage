package com.payten.thsas.simulate.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.payten.thsas.simulate.ISOContent;
import com.payten.thsas.simulate.config.Config;

public class MainWindow extends JFrame {
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
    Config.readConfig();
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
    if (Config.SEND_60_AS_HEX != null ) {
      send60AsHexCheckBox.setSelected(Config.SEND_60_AS_HEX);
    }
    if (Config.SEND_61_AS_HEX != null ) {
      send61AsHexCheckBox.setSelected(Config.SEND_61_AS_HEX);
    }
    if (Config.GET_60_AS_HEX != null ) {
      get60AsHexCheckBox.setSelected(Config.GET_60_AS_HEX);
    }
    if (Config.GET_61_AS_HEX != null ) {
      get61AsHexCheckBox.setSelected(Config.GET_61_AS_HEX);
    }
    revalidate();
    repaint();
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

    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    int textAreaWidth = 20;
    int textAreaHeight = 20;

    inputTextArea = new JTextArea();
    inputTextArea.setLineWrap(true);
    inputTextArea.setPreferredSize(new Dimension(textAreaWidth, textAreaHeight));

    JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
    inputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    inputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

    outputTextArea = new JTextArea();
    outputTextArea.setEditable(false);
    outputTextArea.setLineWrap(true);
    outputTextArea.setPreferredSize(new Dimension(textAreaWidth, textAreaHeight));

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

  private void initConnections() {}
}
