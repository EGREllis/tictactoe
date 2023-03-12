package net.ellise.tictactoe.repository;

import net.ellise.tictactoe.domain.User;

public interface UserRepository {
    User loadUser(String username, String password);
    boolean saveUser(String username, String password);
}
