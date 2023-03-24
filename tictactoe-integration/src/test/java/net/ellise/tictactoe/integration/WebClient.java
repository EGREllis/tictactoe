package net.ellise.tictactoe.integration;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

class WebClient {
    private final String rootUrl;
    private final WebConversation conversation;

    WebClient(String rootUrl, WebConversation conversation) {
        this.rootUrl = rootUrl;
        this.conversation = conversation;
    }

    public void start() throws Exception {
        conversation.getResponse(rootUrl);
    }

    public void followLink(String linkName) throws Exception {
        WebLink registerLink = conversation.getCurrentPage().getLinkWith(linkName);
        registerLink.click();
    }

    public void completeLoginPage(String username, String password) throws Exception {
        WebForm loginForm = getCurrentPage().getFormWithID("login");
        loginForm.setParameter("username", username);
        loginForm.setParameter("password", password);
        loginForm.submit();
    }

    public void completeRegistrationPage(String username, String password) throws Exception {
        WebForm registerForm = getCurrentPage().getFormWithID("register");
        registerForm.setParameter("username", username);
        registerForm.setParameter("password", password);
        registerForm.setParameter("confirm", password);
        registerForm.submit();
    }

    public WebResponse getCurrentPage() {
        return conversation.getCurrentPage();
    }

    public void placePiece(int x, int y) throws Exception {
    }
}
