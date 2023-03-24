package net.ellise.tictactoe.web.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class BoardHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String,String> params = HandlerUtil.parseGetAttributes(exchange);
        for (Map.Entry<String,String> param : params.entrySet()) {
            System.out.println(String.format("Param: %1$s\t%2$s", param.getKey(), param.getValue()));
        }
        Map<String,String> cookies = HandlerUtil.getCookies(exchange);
        for (Map.Entry<String,String> cookie : cookies.entrySet()) {
            System.out.println(String.format("Cookie: %1$s\t%2$s", cookie.getKey(), cookie.getValue()));
        }
    }
}
