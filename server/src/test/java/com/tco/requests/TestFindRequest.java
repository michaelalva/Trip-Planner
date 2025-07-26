package com.tco.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestFindRequest {

    private FindRequest find;

    @BeforeEach
    public void setUp() throws Exception {
        String match = "texas";
        List<String> type = List.of("airport");
        List<String> where = List.of("united states");
        Integer limit = 5;
        find = new FindRequest(match, type, where, limit);
        find.buildResponse();
    }

    @Test
    @DisplayName("duyalva: Request type is \"find\"")
    public void testType() {
        assertEquals("find", find.getRequestType());
    }

    @Test
    @DisplayName("duyalva: Places list is not null")
    public void testPlacesNotNull() {
        assertNotNull(find.getPlaces());
    }

    @Test
    @DisplayName("duyalva: Found count is non-negative")
    public void testFoundNonNegative() {
        assertTrue(find.getFound() >= 0);
    }

    @Test
    @DisplayName("duyalva: Limit is set")
    public void testLimitSet() {
        assertNotNull(find.getLimit());
        assertTrue(find.getLimit() > 0);
    }
    
}
