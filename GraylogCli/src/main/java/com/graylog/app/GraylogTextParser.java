package com.graylog.app;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraylogTextParser implements GraylogParser {
    private BufferedReader reader;
    private static final Logger logger = Logger.getLogger(GraylogTextParser.class);

    public void loadFile(String path) {
        logger.info("current directory: "+new File(".").getAbsolutePath());
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);
        reader = new BufferedReader(new InputStreamReader(inputStream));

    }

    public String getNextLine() {
        if (reader == null) {
            logger.warn("reader is null - did not load file correctly");
            return null;
        }
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String prependBackslashToQuotes(String message) {
        // appends backslashes to all double quotes, and also appends required json fields per GELF specification
        StringBuilder builder = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (c == '{')
                builder.append("{ \"version\": \"1.1\", \"host\": \"me\", \"short_message\": \"loaded via text parser\", ");
            else if (c == '"')
                builder.append("\"");
            else
                builder.append(c);
        }
        logger.debug("prependBackslashToQuotes: message is - "+builder.toString());
        return builder.toString();
    }

    public String processFieldsForGELF(String message) {
        // the input data has fields without preceding "_" underscore
        // GELF specification requires that all custom fields start with underscore
        // this matches all fields that precede a colon, and then prepends underscore to them.
        String p = "((\\w+)\":)+";
        Pattern p1 = Pattern.compile(p);
        Matcher m = p1.matcher(message);
        ArrayList<String> matches = new ArrayList<String>();
        ArrayList<String> replacem = new ArrayList<String>();
        while (m.find()) {
            String x = m.group();
            logger.debug("found field: "+x);
            matches.add(x);
            replacem.add("_".concat(x));
        }
        for (int x = 0 ; x < matches.size(); x++) {
            message = message.replaceFirst(matches.get(x), replacem.get(x));
        }
        logger.debug("message after processing fields: "+message);
        message = prependBackslashToQuotes(message);
        return message;
    }

    public void setDebug(Level level) {
        logger.setLevel(level);
    }
}
