package net.ellise.tictactoe.repository;

import net.ellise.tictactoe.domain.User;

import java.util.HashMap;
import java.util.Map;

public class HashMapUserRepository implements UserRepository {
    private final Map<String, String> usernameToPassword = new HashMap<>();
    private final Map<String, User> usernameToUser = new HashMap<>();
    private int nextUserId = 0;

    @Override
    public User loadUser(String username, String password) {
        User result = null;
        if (password != null && password.equals(usernameToPassword.get(username))) {
            result = usernameToUser.get(username);
        }
        return result;
    }

    @Override
    public boolean saveUser(String username, String password) {
        boolean result;
        if (username == null || username.isBlank() || usernameToPassword.containsKey(username)) {
            result = false;
        } else {
            User user = new User(nextUserId++, username);
            usernameToPassword.put(username, password);
            usernameToUser.put(username, user);
            result = true;
        }
        return result;
    }
}
