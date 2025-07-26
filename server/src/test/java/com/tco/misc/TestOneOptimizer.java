package com.tco.misc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import com.tco.requests.Places;
import com.tco.requests.Place;

import java.util.Arrays;

public class TestOneOptimizer {

    private OneOptimizer optimizer;
    private Places places;
    private Place denver;
    private Place boulder;
    private Place fortCollins;

    @BeforeEach
    public void init() {
        optimizer = new OneOptimizer();
        places = new Places();

        denver = new Place();
        denver.put("name", "Denver");
        denver.put("latitude", "39.7392");
        denver.put("longitude", "-104.9903");

        boulder = new Place();
        boulder.put("name", "Boulder");
        boulder.put("latitude", "40.0150");
        boulder.put("longitude", "-105.2705");

        fortCollins = new Place();
        fortCollins.put("name", "Fort Collins");
        fortCollins.put("latitude", "40.5853");
        fortCollins.put("longitude", "-105.0844");

        places.addAll(Arrays.asList(boulder, fortCollins, denver)); // random order
    }

    @Test
    @DisplayName("zack722: Optimizer returns empty tour for no places")
    public void testEmptyPlaces() {
        Places empty = new Places();
        Places result = optimizer.construct(empty, 6371.0, "haversine", 0);
        assertTrue(result.isEmpty(), "Empty input should return empty output");
    }

    @Test
    @DisplayName("zack722: Optimizer handles single place correctly")
    public void testSinglePlace() {
        Places single = new Places();
        single.add(denver);
        Places result = optimizer.construct(single, 6371.0, "haversine", 0);
        assertEquals(1, result.size());
        assertEquals("Denver", result.get(0).get("name"));
    }

    @Test
    @DisplayName("zack722: Tour includes all places exactly once")
    public void testAllPlacesIncludedOnce() {
        Places result = optimizer.construct(places, 6371.0, "haversine", 0);
        assertEquals(3, result.size());
        assertTrue(result.contains(denver));
        assertTrue(result.contains(boulder));
        assertTrue(result.contains(fortCollins));
    }

    @Test
    @DisplayName("zack722: Throws on bad formula input")
    public void testBadFormulaThrows() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            optimizer.construct(places, 6371.0, "notAFormula", 0);
        });
        assertTrue(exception.getMessage().contains("Invalid formula"), "Should throw for bad formula");
    }


    @Test
    @DisplayName("zack722: Optimizer doesn’t duplicate places")
    public void testNoDuplicatePlaces() {
        Places result = optimizer.construct(places, 6371.0, "haversine", 0);
        long denverCount = result.stream().filter(p -> p.get("name").equals("Denver")).count();
        long boulderCount = result.stream().filter(p -> p.get("name").equals("Boulder")).count();
        long fcCount = result.stream().filter(p -> p.get("name").equals("Fort Collins")).count();
        assertEquals(1, denverCount);
        assertEquals(1, boulderCount);
        assertEquals(1, fcCount);
    }
    @Test
    @DisplayName("cougar: Checking for nullinput")
    public void testNullInput(){
        Places result = optimizer.construct(null,100,"great_circle",1.0);
        assertNull(result);
    }
}
