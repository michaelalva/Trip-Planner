package com.tco.requests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TestPlace {

    private Place place;

    @BeforeEach
    public void createPlace() {
        place = new Place();
    }

    @Test
    @DisplayName("leoo: Place object has correctly converted latitude")
    public void testPlaceLat() {
        place.put("latitude", "50");
        assertEquals(place.latRadians(), 0.8726646259971648);
    }

    @Test
    @DisplayName("leoo: Place object has correctly converted longitude")
    public void testPlaceLon() {
        place.put("longitude", "100");
        assertEquals(place.lonRadians(), 1.7453292519943295);
    }

    @Test
    @DisplayName("leoo: Place object throws IllegalArgumentException when given non-double lon/lat") 
    public void testInvalidLonLat() {
        place.put("longitude", "long");
        place.put("latitude", "lat");
        assertThrows(IllegalArgumentException.class, () -> place.lonRadians());
        assertThrows(IllegalArgumentException.class, () -> place.latRadians());
    }

}
