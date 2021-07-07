package com.graylog.app;

import java.io.IOException;

public interface GraylogService {

    public void sendMessage() throws IOException, InterruptedException;
    public void setConnection(String string);
    public String getConnection();
    public void setMessage(String string);

}
