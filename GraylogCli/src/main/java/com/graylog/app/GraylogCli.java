package com.graylog.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraylogCli
{
    private static GraylogService service;
    private static GraylogParser parser;

    public static void main( String[] args ) throws IOException, InterruptedException {
        final Map<String, List<String>> params = new HashMap<>();

        ArrayList<String> options = null;
        for (int i = 0; i < args.length; i++) {
            final String a = args[i];

            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    return;
                }

                options = new ArrayList<>();
                params.put(a.substring(1), options);
            }
            else if (options != null) {
                options.add(a);
            }
            else {
                System.err.println("Illegal parameter usage");
                return;
            }
        }

        String serv = null;
        String addr = null;
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
            } else {
                service = new GraylogHttpService("192.168.0.25:12201");
            }
        } else {
            System.err.println("Illegal parameter usage");
        }

        String pars = null;
        if (params.get("parser") != null) {
            pars = params.get("parser").get(0);
        } else if (params.get("p") != null) {
            pars = params.get("p").get(0);
        }

        if (pars == null || pars.equals("text")) {
            parser = new GraylogTextParser();
            parser.loadFile("sample-messages.txt");
        } else {
            System.err.println("Illegal parameter usage");
        }

        if (service != null && parser != null) {
            Run();
        }

    }

    public static void Run() throws IOException, InterruptedException {
        String x = parser.getNextLine();
        while (x != null) {
            x = parser.getNextLine();
            if (x == null) break;
            x = parser.processFieldsForGELF(x);
            service.setMessage(x);
            service.sendMessage();
        }
    }


}
