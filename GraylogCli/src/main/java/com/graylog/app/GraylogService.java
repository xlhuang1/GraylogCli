package com.graylog.app;

import org.apache.log4j.Level;

import java.io.IOException;

public interface GraylogService {

    public int sendMessage() throws IOException, InterruptedException;
    public void setConnection(String string);
    public String getConnection();
    public void setMessage(String string);
    public void setDebug(Level level);

}
