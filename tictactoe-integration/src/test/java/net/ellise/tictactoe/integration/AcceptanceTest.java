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

    @Test
    void indexPageLinksToRegisterPage() throws Exception {
        WebConversation conversation = new WebConversation();
        WebResponse rootResponse = conversation.getResponse(CLIENT_ROOT_URL);
        WebLink registerLink = rootResponse.getLinkWith("register");
        registerLink.click();
        WebResponse registerPage = conversation.getCurrentPage();

        assertThat(registerPage.getResponseCode(), is(equalTo(200)));
    }

    @Test
    void indexPageCanNotLoginWithoutRegistration() throws Exception {
        WebConversation conversation = new WebConversation();
        WebResponse indexResponse = conversation.getResponse(CLIENT_ROOT_URL);
        WebForm loginForm = indexResponse.getFormWithID("login");

        loginForm.setParameter("username", "John");
        loginForm.setParameter("password", "Wick");
        WebResponse canNotLogin = loginForm.submit();

        assertThat(canNotLogin.getResponseCode(), is(equalTo(200)));
        assertThat(canNotLogin.getText(), containsString("We could not find a user with that username and password."));
    }

    @Test
    void indexPageCanLogInAfterRegistration() throws Exception {
        WebConversation webConversation = new WebConversation();
        WebResponse indexResponse = webConversation.getResponse(CLIENT_ROOT_URL);
        WebLink registerLink = indexResponse.getLinkWith("register");
        registerLink.click();
        WebResponse registerPage = webConversation.getCurrentPage();
        WebForm registerForm = registerPage.getFormWithID("register");
        registerForm.setParameter("username", "John.Wick");
        registerForm.setParameter("password", "dog");
        registerForm.setParameter("confirm", "dog");
        registerForm.submit();

        WebResponse loginPage = webConversation.getCurrentPage();
        WebForm loginForm = loginPage.getFormWithID("login");
        loginForm.setParameter("username", "John.Wick");
        loginForm.setParameter("password", "dog");
        loginForm.submit();

        WebResponse lobbyPage = webConversation.getCurrentPage();
        assertThat(lobbyPage.getResponseCode(), is(equalTo(200)));
        assertThat(lobbyPage.getText(), containsString("Welcome John.Wick"));
    }
}
