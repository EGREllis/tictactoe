package net.ellise.tictactoe.web;

import com.sun.net.httpserver.HttpServer;
import net.ellise.tictactoe.web.handler.IndexHandler;

import java.net.InetSocketAddress;

public class WebServerImpl implements WebServer {
    private static final int DELAY_SECONDS = 1;
    private final String hostname;
    private final int port;
    private HttpServer server;

    public WebServerImpl(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void start() throws Exception {
        server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        server.createContext("/", new IndexHandler());
        server.setExecutor(null);
        server.start();
    }

    @Override
    public void stop() throws Exception {
        server.stop(DELAY_SECONDS);
    }

    public static WebServer getDefault() {
        return new WebServerImpl("localhost", 8080);
    }

    public static void main(String[] args) throws Exception {
        WebServer webServer = WebServerImpl.getDefault();
        webServer.start();
    }
}
