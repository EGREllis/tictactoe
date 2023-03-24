package net.ellise.tictactoe.integration;

import com.meterware.httpunit.*;
import net.ellise.tictactoe.web.WebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.ServiceLoader;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class AcceptanceTest {
    private static final String CLIENT_ROOT_URL = "http://localhost:8080/";
    private WebServer webServer;
    private WebClient client;

    @BeforeEach
    void setup() throws Exception {
        ServiceLoader<WebServer> serviceLoader = ServiceLoader.load(WebServer.class);
        Iterator<WebServer> webServerIterator = serviceLoader.iterator();
        if (!webServerIterator.hasNext()) {
            throw new IllegalStateException("Could not load WebServer.class from ServiceLoader!");
        }
        webServer = webServerIterator.next();
        webServer.start();
        this.client = new WebClient(CLIENT_ROOT_URL, new WebConversation());
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

    @Test
    void indexPageLinksToRegisterPage() throws Exception {
        client.start();
        client.followLink("register");
        WebResponse registerPage = client.getCurrentPage();

        assertThat(registerPage.getResponseCode(), is(equalTo(200)));
    }

    @Test
    void indexPageCanNotLoginWithoutRegistration() throws Exception {
        client.start();
        client.completeLoginPage("john.wick", "dog");

        WebResponse canNotLogin = client.getCurrentPage();
        assertThat(canNotLogin.getResponseCode(), is(equalTo(200)));
        assertThat(canNotLogin.getText(), containsString("We could not find a user with that username and password."));
    }

    @Test
    void indexPageCanLogInAfterRegistration() throws Exception {
        client.start();
        client.followLink("register");
        client.completeRegistrationPage("john.wick", "dog");
        client.completeLoginPage("john.wick", "dog");

        WebResponse lobbyPage = client.getCurrentPage();
        assertThat(lobbyPage.getResponseCode(), is(equalTo(200)));
        assertThat(lobbyPage.getText(), containsString("Welcome john.wick"));
    }

    @Test
    void lobbyPageContainsLinkToNewGame() throws Exception {
        client.start();
        client.followLink("register");
        client.completeRegistrationPage("john.wick", "dog");
        client.completeLoginPage("john.wick", "dog");

        WebResponse gamePage = client.getCurrentPage();
        client.placePiece(1, 1);
    }
}
