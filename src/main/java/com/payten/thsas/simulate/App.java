package com.payten.thsas.simulate;

import com.payten.thsas.simulate.config.Config;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Config.readConfig();
        ISOContent content = new ISOContent();
        content.readFile();
        ISOSender sender = new ISOSender();
        sender.setMessage(content);
    
        sender.send();

    }
}
