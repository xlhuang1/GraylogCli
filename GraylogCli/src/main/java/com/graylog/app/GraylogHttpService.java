package com.graylog.app;


import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GraylogHttpService implements GraylogService {

    private HttpClient client;
    private HttpRequest request;
    private String connectURI = "http://192.168.0.25:12201/gelf";
    private static final Logger logger = LogManager.getLogger(GraylogHttpService.class);

    public GraylogHttpService(String connectURI) {
        logger.setLevel(Level.INFO);
        client = HttpClient.newBuilder()
                .build();

        String uri = "http://";
        uri = uri.concat(connectURI).concat("/gelf");
        this.connectURI = uri;
        logger.info("Creating HTTP Client object with uri : "+uri);

        request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{ \"version\": \"1.1\", \"host\": \"example.org\", \"short_message\": \"hi\", \"level\": 5, \"_some_info\": \"foo\" }"))
                .build();
    }


    public void sendMessage() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(this.request, HttpResponse.BodyHandlers.ofString());
        logger.debug("Got HTTP response : "+response.statusCode());
        if (!response.body().equals("")) {
            logger.debug("Got HTTP body : "+response.body());
        }
    }

    public void setMessage(String message) {
        logger.debug("Creating HTTP request with following message body: "+message);
        this.request = HttpRequest.newBuilder()
                .uri(URI.create(connectURI))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();
    }

    public void setConnection(String uri) {
        this.connectURI = uri;
    }
    public String getConnection() {
        return this.connectURI;
    }

    public void setDebug(Level level) {
        logger.setLevel(level);
    }
}
