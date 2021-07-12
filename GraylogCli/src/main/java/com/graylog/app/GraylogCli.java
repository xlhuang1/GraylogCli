package com.graylog.app;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraylogCli
{
    private static GraylogService service;
    private static GraylogParser parser;
    private static final Logger logger = Logger.getLogger(GraylogCli.class);

    public static void main( String[] args ) throws IOException, InterruptedException {
        BasicConfigurator.configure();
        final Map<String, List<String>> params = new HashMap<>();

        ArrayList<String> options = null;
        for (int i = 0; i < args.length; i++) {
            final String a = args[i];

            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    logger.error("Error at argument " + a);
                    return;
                }

                options = new ArrayList<>();
                params.put(a.substring(1), options);
            }
            else if (options != null) {
                options.add(a);
            }
            else {
                logger.error("Illegal parameter usage");
                return;
            }
        }

        String serv = null;
        String addr = null;
        String file = null;
        Boolean debug = false;
        if (params.get("service") != null) {
            serv = params.get("service").get(0);
        } else if (params.get("s") != null) {
            serv = params.get("s").get(0);
        }

        if (params.get("a") != null) {
            addr = params.get("a").get(0);
        } else if (params.get("address") != null) {
            addr = params.get("address").get(0);
        }

        if (serv == null || serv.equals("http")) {
            if (addr != null) {
                service = new GraylogHttpService(addr);
                logger.info("initialized HTTP service with address "+addr);
            } else {
                service = new GraylogHttpService("192.168.0.114:12201");
                logger.info("initialized HTTP service with address 192.168.0.114:12201");
            }
        } else {
            logger.error("Illegal parameter usage");
        }

        String pars = null;
        if (params.get("parser") != null) {
            pars = params.get("parser").get(0);
            if ((params.get("file") != null)) {
                file = params.get("file").get(0);
            } else if ((params.get("f") != null)) {
                file = params.get("f").get(0);
            }
        } else if (params.get("p") != null) {
            pars = params.get("p").get(0);
            if ((params.get("file") != null)) {
                file = params.get("file").get(0);
            } else if ((params.get("f") != null)) {
                file = params.get("f").get(0);
            }
        }

        if (pars == null || pars.equals("text")) {
            parser = new GraylogTextParser();
            if (file != null) {
                logger.info("loading file : "+file);
                parser.loadFile(file);
            } else{
                logger.info("loading file : sample-messages.txt");
                parser.loadFile("sample-messages.txt");
            }
        } else {
            logger.error("Illegal parameter usage");
        }

        if (params.get("debug") != null && Boolean.parseBoolean(params.get("debug").get(0))) {
            logger.setLevel(Level.DEBUG);
            service.setDebug(Level.DEBUG);
            parser.setDebug(Level.DEBUG);
        } else {
            logger.setLevel(Level.INFO);
            service.setDebug(Level.INFO);
            parser.setDebug(Level.INFO);
        }

        if (service != null && parser != null) {
            Run();
        }

    }

    public static void Run() throws IOException, InterruptedException {
        // goes through all lines and attempts to send through service.
        String x = parser.getNextLine();
        int count = 1;
        while (x != null) {
            x = parser.getNextLine();
            if (x == null) break;
            x = parser.processFieldsForGELF(x);
            service.setMessage(x);
            int rc = service.sendMessage();
            if (rc != 202) {
                logger.warn("Message not sent successfully");
                // TODO implement resend or error handling
            }
            count++;
        }
        logger.info("Sent "+String.valueOf(count)+" messages successfully");
    }


}
