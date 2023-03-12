package net.ellise.tictactoe.integration;

import net.ellise.tictactoe.web.WebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.ServiceLoader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class AcceptanceTest {
    private static final String CLIENT_ROOT_URL = "http://localhost:8080/";
    private WebServer webServer;

    @BeforeEach
    void setup() throws Exception {
        ServiceLoader<WebServer> serviceLoader = ServiceLoader.load(WebServer.class);
        Iterator<WebServer> webServerIterator = serviceLoader.iterator();
        if (!webServerIterator.hasNext()) {
            throw new IllegalStateException("Could not load WebServer.class from ServiceLoader!");
        }
        webServer = webServerIterator.next();
        webServer.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        webServer.stop();
    }

    @Test
    void canConnect() throws Exception {
        URL url = new URL(CLIENT_ROOT_URL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.connect();
        assertThat(connection.getResponseCode(), is(equalTo(200)));
    }
}
