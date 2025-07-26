package com.tco.misc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCredentials {

    @Test
    @DisplayName("leoo: Test for correct username")
    public void testUsername() {
        assertEquals("cs314-db", Credentials.USER);
    }

    @Test
    @DisplayName("leoo: Test for correct password")
    public void testPassword() {
        assertEquals("eiK5liet1uej", Credentials.PASSWORD);
    }

    @Test
    @DisplayName("leoo: Test for correct URL")
    public void testURL() {
        assertEquals("jdbc:mariadb://faure.cs.colostate.edu/cs314", Credentials.URL);
    }
    
}
