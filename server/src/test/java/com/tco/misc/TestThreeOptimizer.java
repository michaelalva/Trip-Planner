package com.tco.misc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import com.tco.requests.Place;
import com.tco.requests.Places;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

public class TestThreeOptimizer {

    private ThreeOptimizer optimizer;
    private Places places;
    private Place denver;
    private Place boulder;
    private Place fortCollins;
    private String haversine = "haversine";
    private double earthRadius = 6371.0;

    @BeforeEach
    public void init() {
        optimizer = new ThreeOptimizer();
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
        
        places.addAll(Arrays.asList(boulder, fortCollins, denver));
    }

    @Test
    @DisplayName("duyalva: Test construct() returns a non-null tour")
    public void testConstructNonNull() {
        Places tour = optimizer.construct(places, 6371.0, haversine, 0.0);
        assertNotNull(tour);
    }

    @Test
    @DisplayName("duyalva: ThreeOpt does not modify empty route")
    public void testThreeOptEmptyRoute() {
        Places emptyPlaces = new Places();
        optimizer.threeOpt(emptyPlaces);
        assertEquals(0, emptyPlaces.size());
    }

    @Test
    @DisplayName("duyalva: Construct starts with Denver")
    public void testConstructStartsWithDenver() {
        Places p = new Places();
        p.add(denver);
        Places tour = optimizer.construct(p, 6371.0, haversine, 0.0);
        assertEquals("Denver", tour.get(0).get("name"));
    }

    @Test
    @DisplayName("duyalva: Construct returns a tour with the correct number of places")
    public void testConstructCorrectSize() {
        Places tour = optimizer.construct(places, 6371.0, haversine, 0.0);
        assertEquals(3, tour.size());
    }

    @Test
    @DisplayName("duyalva: Construct includes all original places")
    public void testConstructIncludesAllPlaces() {
        Places tour = optimizer.construct(places, 6371.0, haversine, 0.0);
        assertTrue(tour.contains(denver));
        assertTrue(tour.contains(boulder));
        assertTrue(tour.contains(fortCollins));
    }

    @Test
    @DisplayName("duyalva: threeOpt does not modify empty route")
    public void testThreeOptEmptyRouteSize() {
        Places emptyPlaces = new Places();
        optimizer.threeOpt(emptyPlaces);
        assertEquals(0, emptyPlaces.size());
    }

    @Test
    @DisplayName("duyalva: Construct with single place stays the same")
    public void testConstructSinglePlace() {
        Places single = new Places();
        single.add(denver);
        Places tour = optimizer.construct(single, 6371.0, haversine, 0.0);
        assertEquals(1, tour.size());
        assertEquals("Denver", tour.get(0).get("name"));
    }

    @Test
    @DisplayName("duyalva: Construct returns a reordered tour")
    public void testConstructReordersPlaces() {
        Places tour = optimizer.construct(places, 6371.0, haversine, 0.0);
        assertNotEquals(places, tour);
    }

    @Test
    @DisplayName("duyalva: threeOpt does not fail with 2 places")
    public void testThreeOptTwoPlaces() {
        Places two = new Places();
        two.add(denver);
        two.add(boulder);

        assertDoesNotThrow(() -> optimizer.threeOpt(two));
        assertEquals(2, two.size());
    }

    @Test
    @DisplayName("duyalva: improve() does nothing for tour with less than 4 places")
    public void testImproveSmallTour() {
        Places smallTour = new Places();
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
        smallTour.addAll(Arrays.asList(boulder, fortCollins, denver));
        try {
            java.lang.reflect.Field optimizedTourField = ThreeOptimizer.class.getDeclaredField("optimizedTour");
            optimizedTourField.setAccessible(true);
            optimizedTourField.set(optimizer, smallTour);
            optimizer.improve();
            assertEquals(3, smallTour.size());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access optimizedTour field: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("duyalva: Test construct() throws RuntimeException for invalid formula")
    public void testConstructInvalidFormula() {
        assertThrows(RuntimeException.class, () -> optimizer.construct(places, 6371.0, "invalid_formula", 0.0));
    }

    @Test
    @DisplayName("bryce24: threeOpt maintains tour size after improvement")
    public void testThreeOptMaintainsTourSize() {
        Places tour = optimizer.construct(places, 6371.0, haversine, 0.0);
        int originalSize = tour.size();

        try {
            java.lang.reflect.Field optimizedTourField = ThreeOptimizer.class.getDeclaredField("optimizedTour");
            optimizedTourField.setAccessible(true);
            optimizedTourField.set(optimizer, tour);
            optimizer.improve();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection error: " + e.getMessage());
        }

        assertEquals(originalSize, tour.size(), "Tour size should remain unchanged after 3-opt");
    }

    @Test
    @DisplayName("duyalva: improve() does nothing if optimizedTour is null")
    public void testImproveWithNullOptimizedTour() {
        try {
            java.lang.reflect.Field optimizedTourField = ThreeOptimizer.class.getDeclaredField("optimizedTour");
            optimizedTourField.setAccessible(true);
            optimizedTourField.set(optimizer, null);
            optimizer.improve();
            assertNull(optimizedTourField.get(optimizer));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access optimizedTour field: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("duyalva: threeOpt does not fail with 3 places")
    public void testThreeOptThreePlaces() {
        Places three = new Places();
        three.add(denver);
        three.add(boulder);
        three.add(fortCollins);
        assertDoesNotThrow(() -> optimizer.threeOpt(three));
        assertEquals(3, three.size());
    }

    @Test
    @DisplayName("duyalva: construct initializes optimizedTour")
    public void testConstructInitializesOptimizedTour() {
        optimizer.construct(places, earthRadius, haversine, 0.0);
        try {
            java.lang.reflect.Field optimizedTourField = ThreeOptimizer.class.getDeclaredField("optimizedTour");
            optimizedTourField.setAccessible(true);
            assertNotNull(optimizedTourField.get(optimizer));
            assertEquals(places.size(), ((Places) optimizedTourField.get(optimizer)).size());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access optimizedTour field: " + e.getMessage());
        }
    }
    @Test
    @DisplayName("bryce24: threeOpt handles wraparound at end of list")
    public void testThreeOptWraparound() {
        Places input = new Places();
        input.add(new Place("40.0150", "-105.2705")); // Boulder
        input.add(new Place("40.5853", "-105.0844")); // Fort Collins
        input.add(new Place("39.7392", "-104.9903")); // Denver
        input.add(new Place("40.0150", "-105.2705")); // Boulder again
    
        assertDoesNotThrow(() -> optimizer.threeOpt(input), "threeOpt should handle circular routes safely");
    }

    @Test
    @DisplayName("bryce24: construct() does not modify original input")
    public void testConstructDoesNotMutateInput() {
        Places original = new Places();
        original.add(new Place("40.0150", "-105.2705")); // Boulder
        original.add(new Place("40.5853", "-105.0844")); // Fort Collins
        original.add(new Place("39.7392", "-104.9903")); // Denver

        Places copyBefore = new Places(original);
        optimizer.construct(original, earthRadius, haversine, 0.0);
        assertEquals(copyBefore, original, "Original input list should remain unchanged");
}


    
    


}
