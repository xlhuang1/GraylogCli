package com.graylog.app;

public class GraylogCli
{
    private static GraylogService service;
    private static GraylogParser parser;

    public static void main( String[] args )
    {
        GraylogService service = new GraylogHttpService();
        GraylogParser parser = new GraylogTextParser();
    }


}
