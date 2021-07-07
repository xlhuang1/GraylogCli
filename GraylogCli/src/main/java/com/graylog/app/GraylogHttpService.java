package com.graylog.app;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GraylogHttpService implements GraylogService {

    private HttpClient client;
    private HttpRequest request;
    private String connectURI = "http://192.168.0.25:12201/gelf";

    public GraylogHttpService(String connectURI) {
        client = HttpClient.newBuilder()
                .build();

        String uri = "http://";
        uri = uri.concat(connectURI).concat("/gelf");

        request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{ \"version\": \"1.1\", \"host\": \"example.org\", \"short_message\": \"hi\", \"level\": 5, \"_some_info\": \"foo\" }"))
                .build();
    }


    public void sendMessage() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(this.request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }

    public void setMessage(String message) {
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
}
