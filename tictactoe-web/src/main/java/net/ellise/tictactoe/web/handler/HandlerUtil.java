package net.ellise.tictactoe.web.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HandlerUtil {
    private static final Logger log = Logger.getLogger(HandlerUtil.class.getCanonicalName());

    public static String loadPageFromClasspath(String path) {
        StringBuilder result = new StringBuilder();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                result.append(line);
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace(System.err);
        }
        return result.toString();
    }

    public static void writeHtmlAndClose(HttpExchange exchange, String html, int responseCode) throws IOException {
        byte[] response = html.getBytes(UTF_8);
        exchange.sendResponseHeaders(responseCode, response.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response);
        outputStream.close();
    }

    public static void writeCookie(HttpExchange exchange, String cookieName, String cookieValue) {
        Headers responseHeader = exchange.getResponseHeaders();
        responseHeader.set("Set-Cookie", cookieName+"="+cookieValue+";");
    }

    public static String getCookie(HttpExchange exchange, String cookieName) {
        String result = "";
        Headers headers = exchange.getRequestHeaders();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if ("Cookie".equals(entry.getKey())) {
                for (String value : entry.getValue()) {
                    for (String parameter : value.split("; ")) {
                        Pattern pattern = Pattern.compile("([^=]+)=([^=]+)");
                        Matcher matcher = pattern.matcher(parameter);
                        if (matcher.matches()) {
                            if (cookieName.equals(matcher.group(1))) {
                                result = matcher.group(2);
                                return result;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static Map<String,String> parsePostAttributes(HttpExchange exchange) {
        Map<String,String> attributes = new HashMap<>();
        StringBuilder body = new StringBuilder();
        try (InputStream input = exchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                body.append(line);
            }
            String[] parameters = body.toString().split("&");
            for (String parameter : parameters) {
                String[] attr = parameter.split("=");
                attributes.put(attr[0], attr[1]);
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace(System.err);
        }
        return attributes;
    }
}
