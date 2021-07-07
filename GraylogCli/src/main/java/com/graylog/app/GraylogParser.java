package com.graylog.app;

public interface GraylogParser {
    public String loadFile();
    public String getNextLine();
}
