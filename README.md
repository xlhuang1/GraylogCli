# GraylogCli
Simple CLI text parser and graylog client

Main class is GraylogCli, which instantiates a GraylogParser and GraylogService

GraylogParser is an interface which allows the user to load files, such as txt files, and convert 
the contents into GELF format

GraylogService is an interface which is used to define a method of transmitting the 
messages that are created/read via parser to the Graylog server.
A simple implementation is the GraylogHttpService class.

BUILD:
import the project into Intellij
run "mvn clean install"
RUN:
then run the created jar file by using java command line
> \# java -jar target/GraylogCli-1.0-SNAPSHOT.jar -p text -s http

USAGE:
Currently supported flags:

-p | -parser : text - uses txt parser and file

-s | -service : http - uses Graylog HTTP API

-a | -address : specifies IP address and port for HTTP API - requires -s http flag.

-debug : true | false - enables debug log level DEBUG for all classes


TODO:
Still need to write implementation for chunking GELF messages that are too large. 
