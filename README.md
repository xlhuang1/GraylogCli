# GraylogCli
Simple CLI text parser and graylog client

Main class is GraylogCli, which instantiates a GraylogParser and GraylogService

GraylogParser is an interface which allows the user to load files, such as txt files, and convert 
the contents into GELF format

GraylogService is an interface which is used to define a method of transmitting the 
messages that are created/read via parser to the Graylog server.
A simple implementation is the GraylogHttpService class.