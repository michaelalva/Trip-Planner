package com.tco.misc;

import com.tco.requests.Places;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("zack722: TestDatabase")
public class TestDatabase {

    @Test
    @DisplayName("zack722: should find known city count > 0")
    public void testFoundKnownCity() throws Exception {
        String sql = Select.found("Fort Collins");
        Integer count = Database.found(sql);
        assertNotNull(count, "Expected non-null result");
        assertTrue(count > 0, "Expected at least one match for 'Fort Collins'");
    }

    @Test
    @DisplayName("zack722: should find city records with match")
    public void testPlacesMatchLimit() throws Exception {
        String sql = Select.match("Fort Collins", 3);
        Places places = Database.places(sql, 3);
        assertNotNull(places, "Expected non-null places result");
        assertFalse(places.isEmpty(), "Expected some places to be returned");
        assertTrue(places.size() <= 3, "Expected no more than 3 results");
    }

    @Test
    @DisplayName("zack722: should return empty when no city match")
    public void testNoMatchReturnsEmpty() throws Exception {
        String sql = Select.match("asldkfjalskdjflas", 5); // gibberish
        Places places = Database.places(sql, 5);
        assertNotNull(places, "Expected non-null even if no matches");
        assertEquals(0, places.size(), "Expected 0 results for nonsense input");
    }

}
