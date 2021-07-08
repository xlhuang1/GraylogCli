package com.graylog.app;

//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GraylogTextParserTest {

    public GraylogTextParserTest() {
        super();
    }

    @Test
    public void processFieldsForGELF() {
        // Given
        GraylogTextParser testing = new GraylogTextParser();

        // When
        String input = "{\"ClientDeviceType\": \"desktop\",\"ClientIP\": \"192.168.87.52\"}";
        String result = testing.processFieldsForGELF(input);
        System.out.println(result);
        assertEquals("{ \"version\": \"1.1\", \"host\": \"me\", \"short_message\": \"loaded via text parser\", \"_ClientDeviceType\": \"desktop\",\"_ClientIP\": \"192.168.87.52\"}", result);
    }
}