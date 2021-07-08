package com.graylog.app;

import org.apache.log4j.Level;

public interface GraylogParser {
    public void loadFile(String path);
    public String getNextLine();
    public String processFieldsForGELF(String x);
    public void setDebug(Level level);
}
