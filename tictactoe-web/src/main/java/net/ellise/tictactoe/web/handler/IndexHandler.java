package net.ellise.tictactoe.web.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.ellise.tictactoe.repository.UserRepository;

import java.io.*;
import java.util.Map;
import java.util.logging.Logger;

public class IndexHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(IndexHandler.class.getCanonicalName());
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private final UserRepository userRepository;

    public IndexHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String html = HandlerUtil.loadPageFromClasspath("html/error.html");
        int responseCode = 503;
        if ("GET".equals(exchange.getRequestMethod())) {
            html = HandlerUtil.loadPageFromClasspath("html/index.html");
            responseCode = 200;
        } else if ("POST".equals(exchange.getRequestMethod())) {
            Map<String,String> attributes = HandlerUtil.parsePostAttributes(exchange);
            if (attributes.containsKey(USERNAME_KEY) && attributes.containsKey(PASSWORD_KEY)) {
                String username = attributes.get(USERNAME_KEY);
                if (userRepository.loadUser(username, attributes.get(PASSWORD_KEY)) == null) {
                    html = HandlerUtil.loadPageFromClasspath("html/index_failed_login.html");
                    responseCode = 200;
                } else {
                    //HandlerUtil.writeCookie(exchange, "TTT_USERNAME", username);
                    String htmlTemplate = HandlerUtil.loadPageFromClasspath("html/lobby.html");
                    html = String.format(htmlTemplate, username);
                    responseCode = 200;
                }
            }
        }
        HandlerUtil.writeHtmlAndClose(exchange, html, responseCode);
    }
}
