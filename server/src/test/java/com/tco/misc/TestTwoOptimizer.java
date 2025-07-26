package com.tco.misc;

import static org.junit.jupiter.api.Assertions.*;

import com.tco.requests.Place;
import com.tco.requests.Places;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


public class TestTwoOptimizer {

    private TwoOptimizer optimizer;
    private Places places;
    private Place denver;
    private Place boulder;
    private Place fortCollins;
    private Place coloradoSprings;
    final String haver = "haversine";

    @BeforeEach
    public void init() {
        final String lat = "latitude";
        final String longitude = "longitude";
        optimizer = new TwoOptimizer();
        places = new Places();
        
        denver = new Place();
        denver.put("name", "Denver");
        denver.put(lat, "39.7392");
        denver.put(longitude, "-104.9903");
        
        boulder = new Place();
        boulder.put("name", "Boulder");
        boulder.put(lat, "40.0150");
        boulder.put(longitude, "-105.2705");
        
        fortCollins = new Place();
        fortCollins.put("name", "Fort Collins");
        fortCollins.put(lat, "40.5853");
        fortCollins.put(longitude, "-105.0844");
        
        places.addAll(Arrays.asList(boulder, fortCollins, denver));
    }

    @Test
    @DisplayName("duyalva: Construct creates a non-null tour")
    public void testConstructNotNull() {
        Places tour = optimizer.construct(places, 6371.0, haver, 1.0);
        assertNotNull(tour);
    }

    @Test
    @DisplayName("duyalva: TwoOptReverse correctly swaps elements")
    public void testTwoOptReverse() {
        Places tour = new Places();
        tour.addAll(Arrays.asList(denver, boulder, fortCollins, coloradoSprings));
        TwoOptimizer.twoOptReverse(tour, 1, 2);
        assertEquals(fortCollins, tour.get(1));
    }


    @Test
    @DisplayName("duyalva: Construct handles two places correctly")
    public void testConstructTwoPlaces() {
        Places twoPlaces = new Places();
        twoPlaces.add(denver);
        twoPlaces.add(boulder);
        Places tour = optimizer.construct(twoPlaces, 6371.0, haver, 0.0);
        assertEquals(2, tour.size());
    }

    @Test
    @DisplayName("duyalva: Construct starts with Denver")
    public void testConstructStartsWithDenver() {
        Places p = new Places();
        p.add(denver);
        Places tour = optimizer.construct(p, 6371.0, haver, 0.0);
        assertEquals("Denver", tour.get(0).get("name"));
    }

    @Test
    @DisplayName("cougar: Testing construct with empty places")
    public void testConstructEmptyPlaces(){
        Places empty = new Places();
        Places result = optimizer.construct(empty,3959.0,"haversine",0.0);
        assertTrue(result.isEmpty());
    }
    @Test
    @DisplayName("cougar: Testing the reverse function to make sure that it reverses correctly")
    public void testingReverse(){
        Place place1 = new Place();
        place1.put("name","1");
        Place place2 = new Place();
        place2.put("name","2");
        Place place3 = new Place();
        place3.put("name","3");
        Place place4 = new Place();
        place4.put("name","4");

        Places route = new Places();
        route.add(place1);
        route.add(place2);
        route.add(place3);
        route.add(place4);
        
        TwoOptimizer.twoOptReverse(route,0,3);
        assertEquals(place4, route.get(0));
        assertEquals(place3, route.get(1));
        assertEquals(place2, route.get(2));
        assertEquals(place1, route.get(3));
    }

    @Test
    @DisplayName("cougar: Testing the improve method to make sure it exists early and does nothing when the tour is null and or is too small.")
    public void testingImproveandSmallTour(){
        optimizer.improve();
    }

    @Test
    @DisplayName("bryce24: Improve optimizes the tour to reduce total distance")
    private double totalTourDistance(Places tour, double radius, String formula) throws BadRequestException{
        double total = 0.0;
        DistanceCalculator calc = new CalculatorFactory().get(formula);
        for (int i = 0; i < tour.size() - 1; i++) {
            total += calc.between(tour.get(i), tour.get(i + 1), radius);
        }
        return total;
    }

}
