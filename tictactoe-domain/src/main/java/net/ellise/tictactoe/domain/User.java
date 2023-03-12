package net.ellise.tictactoe.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class User {
    int id;
    String username;
}
