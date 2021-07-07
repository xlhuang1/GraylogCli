package com.graylog.app;

import java.io.IOException;

public class GraylogCli
{
    private static GraylogService service;
    private static GraylogParser parser;

    public static void main( String[] args ) throws IOException, InterruptedException {
        System.out.println(args.length);
        GraylogService service = new GraylogHttpService();
        String test = "{ \"version\": \"1.1\", \"host\": \"me\", \"short_message\": \"xlh\", \"level\": 5, \"_ClientDeviceType\": \"desktop\",\"_ClientIP\": \"192.168.87.52\" }";
//        service.setMessage(test);
//        service.sendMessage();
        GraylogTextParser parser = new GraylogTextParser();
        parser.loadFile("sample-messages.txt");
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
