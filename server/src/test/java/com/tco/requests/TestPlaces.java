package com.tco.requests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPlaces {

    private Places places;
    private Place place1;
    private Place place2;

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    
    @BeforeEach
    public void createPlace() {
        places = new Places();
        place1  = new Place();
        place2 = new Place();
    }

    @Test
    @DisplayName("duyalva: Put two Place objects in Places")
    public void testPlacesStorage() {
        places.add(place1);
        places.add(place2);
        assertEquals(2, places.size());
    }

    @Test
    @DisplayName("duyalva: Place1 object within Places converts longitude")
    public void testPlaceslat() {
        place1.put(LATITUDE, "50");
        place1.put(LONGITUDE, "100");
        place2.put(LATITUDE, "-60");
        place2.put(LONGITUDE, "-110");
        places.add(place1);
        places.add(place2);
        assertEquals(places.get(0).latRadians(), 0.8726646259971648);
    }

    @Test
    @DisplayName("duyalva: Place2 object within Places converts longitude")
    public void testPlaceslon() {
        place1.put(LATITUDE, "50");
        place1.put(LONGITUDE, "100");
        place2.put(LATITUDE, "-60");
        place2.put(LONGITUDE, "-110");
        places.add(place1);
        places.add(place2);
        assertEquals(places.get(1).lonRadians(), -1.9198621771937625);
    }
}
