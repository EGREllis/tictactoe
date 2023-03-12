package net.ellise.tictactoe.web;

import com.sun.net.httpserver.HttpServer;
import net.ellise.tictactoe.repository.UserRepository;
import net.ellise.tictactoe.web.handler.IndexHandler;
import net.ellise.tictactoe.web.handler.RegisterHandler;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.ServiceLoader;

public class WebServerImpl implements WebServer {
    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static final int DELAY_SECONDS = 1;
    private final String hostname;
    private final int port;
    private HttpServer server;

    // Used by the Java Services API
    public WebServerImpl() {
        this(DEFAULT_HOSTNAME, DEFAULT_PORT);
    }

    public WebServerImpl(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void start() throws Exception {
        server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        UserRepository userRepository = loadUserRepository();
        server.createContext("/", new IndexHandler(userRepository));
        server.createContext("/register", new RegisterHandler(userRepository));
        server.setExecutor(null);
        server.start();
    }

    @Override
    public void stop() {
        server.stop(DELAY_SECONDS);
    }

    public static WebServer getDefault() {
        return new WebServerImpl(DEFAULT_HOSTNAME, DEFAULT_PORT);
    }

    public static void main(String[] args) throws Exception {
        WebServer webServer = WebServerImpl.getDefault();
        webServer.start();
    }

    private UserRepository loadUserRepository() {
        UserRepository result;
        ServiceLoader<UserRepository> userRepositoryServiceLoader = ServiceLoader.load(UserRepository.class);
        Iterator<UserRepository> userRepositoryIterator = userRepositoryServiceLoader.iterator();
        if (!userRepositoryIterator.hasNext()) {
            throw new IllegalStateException("Could not load UserRepository Service");
        }
        result = userRepositoryIterator.next();
        return result;
    }
}
