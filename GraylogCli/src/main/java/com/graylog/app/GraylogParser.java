package com.graylog.app;

public interface GraylogParser {
    public void loadFile(String path);
    public String getNextLine();
    public String prependBackslashToQuotes(String x);
}
