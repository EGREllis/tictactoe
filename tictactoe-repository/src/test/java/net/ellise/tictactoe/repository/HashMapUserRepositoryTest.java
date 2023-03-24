package net.ellise.tictactoe.repository;

import net.ellise.tictactoe.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class HashMapUserRepositoryTest {
    private UserRepository repository;

    @BeforeEach
    void setup() {
        repository = new HashMapUserRepository();
    }

    @Test
    void shouldLookupARegisteredUser() {
        boolean registered = repository.saveUser("John.Wick", "dog");
        User user = repository.loadUser("John.Wick", "dog");

        assertThat(registered, is(equalTo(true)));
        assertThat(user.getUsername(), is(equalTo("John.Wick")));
    }

    @Test
    void shouldNotLoadUserWithWrongPassword() {
        boolean registered = repository.saveUser("John.Wick", "dog");
        User wick = repository.loadUser("John.Wick", "High table");

        assertThat(registered, is(equalTo(true)));
        assertThat(wick, is(nullValue()));
    }

    @Test
    void shouldGiveDifferentUsersDifferentIds() {
        boolean registerWick = repository.saveUser("John.Wick", "dog");
        boolean registerCharon = repository.saveUser("Charon", "Continental");
        User wick = repository.loadUser("John.Wick", "dog");
        User charon = repository.loadUser("Charon", "Continental");

        assertThat(registerWick, is(equalTo(true)));
        assertThat(registerCharon, is(equalTo(true)));
        assertThat(wick.getId(), is(not(equalTo(charon.getId()))));
    }

    @Test
    void shouldReturnNullIfUsernamePresentButPasswordWrong() {
        boolean registerWick = repository.saveUser("john.wick", "dog");

        User user = repository.loadUser("john.wick", null);

        assertThat(user, is(nullValue()));
    }
}
