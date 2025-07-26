package com.tco.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

class NearRequestTest {

    private NearRequest near;

    @BeforeEach
    public void setUp() throws Exception {
        String formula = "cosines";
        Integer distance = 1000;
        Double earthRadius = 3959.0;
        Integer limit = 5;
        Place place = new Place();
        place.put("latitude", "40.730610");
        place.put("longitude", "-73.935242");
        place.put("name", "New York");
        place.put("id", "123");
        near = new NearRequest(distance, place, earthRadius, formula, limit);
        near.buildResponse();
    }

    @Test
    @DisplayName("duyalva: Request type is \"near\"")
    public void testRequestType() {
        String type = near.getRequestType();
        assertEquals("near", type);
    }

    @Test
    @DisplayName("duyalva: Request properly queries Database for \"near\" places")
        public void testProperPlaces() throws Exception {
        near.buildResponse();
        assertNotNull(near.getPlaces());
    }

    @Test
    @DisplayName("duyalva: Request properly queries Database for \"distances\"")
    public void testProperDistances() throws Exception {
        near.buildResponse();
        assertNotNull(near.getDistances());
    }

    @Test
    @DisplayName("duyalva: Radius is correctly returned")
    public void testGetRadius() {
        assertEquals(3959.0, near.getRadius());
    }

    @Test
    @DisplayName("duyalva: Limit is set correctly and positive")
    public void testGetLimit() {
        assertNotNull(near.getLimit());
        assertTrue(near.getLimit() > 0);
        assertEquals(5, near.getLimit());
    }

    @Test
    @DisplayName("duyalva: Formula is correctly stored and returned")
    public void testGetFormula() {
        assertEquals("cosines", near.getFormula());
    }

    @Test
    @DisplayName("duyalva: Distance field is correctly set and retrievable")
    public void testGetDistance() {
        assertEquals(1000, near.getDistance());
    }

    @Test
    @DisplayName("duyalva: Place is returned and contains expected fields")
    public void testGetPlace() {
        Place returnedPlace = near.getPlace();
        assertNotNull(returnedPlace);
        assertEquals("New York", returnedPlace.get("name"));
        assertEquals("40.730610", returnedPlace.get("latitude"));
        assertEquals("-73.935242", returnedPlace.get("longitude"));
        assertEquals("123", returnedPlace.get("id"));
    }

}
