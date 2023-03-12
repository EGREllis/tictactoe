package net.ellise.tictactoe.web.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.ellise.tictactoe.repository.UserRepository;

import java.io.IOException;
import java.util.Map;

public class RegisterHandler implements HttpHandler {
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String CONFIRM_KEY = "confirm";
    private final UserRepository userRepository;

    public RegisterHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String html = HandlerUtil.loadPageFromClasspath("html/error.html");
        int responseCode = 503;
        switch(exchange.getRequestMethod()) {
            case "GET":
                html = HandlerUtil.loadPageFromClasspath("html/register.html");
                responseCode = 200;
                break;
            case "POST":
                Map<String,String> attributes = HandlerUtil.parsePostAttributes(exchange);
                if (attributes.containsKey(USERNAME_KEY) &&
                        attributes.containsKey(PASSWORD_KEY) &&
                        attributes.containsKey(CONFIRM_KEY)) {
                    String username = attributes.get(USERNAME_KEY);
                    String password = attributes.get(PASSWORD_KEY);
                    String confirm = attributes.get(CONFIRM_KEY);
                    if (username != null && !username.isBlank() &&
                        password != null && !password.isBlank() &&
                        confirm != null && !confirm.isBlank() &&
                        confirm.equals(password)) {
                        if (userRepository.saveUser(username, password)) {
                            html = HandlerUtil.loadPageFromClasspath("html/index.html");
                            responseCode = 200;
                        } else {
                            html = HandlerUtil.loadPageFromClasspath("html/register.html");
                            responseCode = 200;
                        }
                    } else {
                        html = HandlerUtil.loadPageFromClasspath("html/register.html");
                        responseCode = 200;
                    }
                }
                break;
            default:
                break;
        }

        HandlerUtil.writeHtmlAndClose(exchange, html, responseCode);
    }
}
