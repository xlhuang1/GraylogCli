package com.graylog.app;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraylogTextParser implements GraylogParser {
    private BufferedReader reader;

    public void loadFile(String path) {
        System.out.println(new File(".").getAbsolutePath());
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);
        reader = new BufferedReader(new InputStreamReader(inputStream));

    }

    public String getNextLine() {
        if (reader == null) {
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
        StringBuilder builder = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (c == '{')
                builder.append("{ \"version\": \"1.1\", \"host\": \"me\", \"short_message\": \"loaded via text parser\", ");
            else if (c == '"')
                builder.append("\"");
            else
                builder.append(c);
        }
        return builder.toString();
    }

    public String processFieldsForGELF(String message) {
        String p = "((\\w+)\":)+";
        Pattern p1 = Pattern.compile(p);
        Matcher m = p1.matcher(message);
        ArrayList<String> matches = new ArrayList<String>();
        ArrayList<String> replacem = new ArrayList<String>();
        while (m.find()) {
            String x = m.group();
            matches.add(x);
            replacem.add("_".concat(x));
        }
        for (int x = 0 ; x < matches.size(); x++) {
            message = message.replaceFirst(matches.get(x), replacem.get(x));
        }
        message = prependBackslashToQuotes(message);
        return message;
    }



}
