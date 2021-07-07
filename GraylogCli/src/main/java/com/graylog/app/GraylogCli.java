package com.graylog.app;

import java.io.IOException;

public class GraylogCli
{
    private static GraylogService service;
    private static GraylogParser parser;

    public static void main( String[] args ) throws IOException, InterruptedException {
        GraylogService service = new GraylogHttpService();
        service.setMessage("{ \"version\": \"1.1\", \"host\": \"me\", \"short_message\": \"lolol\", \"level\": 5, \"_some_info\": \"foo\" }");
        service.sendMessage();
        GraylogParser parser = new GraylogTextParser();
    }


}
