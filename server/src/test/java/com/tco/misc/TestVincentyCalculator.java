package com.tco.misc;

import com.tco.requests.Place;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class TestVincentyCalculator {
    private VincentyCalculator calc;
    private double earthRadius;
    private Place from;
    private Place to;

    @BeforeEach
    public void init() {
        earthRadius = 6371;
        from = new Place();
        from.put("latitude", "100");
        from.put("longitude", "100");
        to = new Place();
        to.put("latitude", "50");
        to.put("longitude", "50");
        calc = new VincentyCalculator();
    }

    @Test
    @DisplayName("bryce24: Test to see if between method generates the correct number.")
    public void testInitialCalculation() {
        assertEquals(5220, calc.between(from, to, earthRadius));
    }

    @Test
    @DisplayName("zack722: Test for same point (zero distance)")
    public void testSamePoint() {
        assertEquals(0, calc.between(from, from, earthRadius));
    }

    @Test
    @DisplayName("zack722: Test for antipodal points")
    public void testAntipodalPoints() {
        Place point1 = new Place();
        point1.put("latitude", "0");
        point1.put("longitude", "0");

        Place point2 = new Place();
        point2.put("latitude", "0");
        point2.put("longitude", "180");

        assertEquals((long) (Math.PI * 6371), calc.between(point1, point2, earthRadius));
    }

    @Test
    @DisplayName("zack722: Test for equator crossing")
    public void testEquatorCrossing() {
        Place point1 = new Place();
        point1.put("latitude", "1");
        point1.put("longitude", "-75");

        Place point2 = new Place();
        point2.put("latitude", "-1");
        point2.put("longitude", "-75");

        assertTrue(calc.between(point1, point2, earthRadius) > 0);
    }

    @Test
    @DisplayName("zack722: Test for Prime Meridian crossing")
    public void testPrimeMeridianCrossing() {
        Place point1 = new Place();
        point1.put("latitude", "10");
        point1.put("longitude", "-0.1");

        Place point2 = new Place();
        point2.put("latitude", "10");
        point2.put("longitude", "0.1");

        assertTrue(calc.between(point1, point2, earthRadius) > 0);
    }

    @Test
    @DisplayName("zack722: Test for North Pole")
    public void testNorthPole() {
        Place point1 = new Place();
        point1.put("latitude", "90");
        point1.put("longitude", "0");

        Place point2 = new Place();
        point2.put("latitude", "89");
        point2.put("longitude", "45");

        assertTrue(calc.between(point1, point2, earthRadius) > 0);
    }

    @Test
    @DisplayName("zack722: Test for South Pole")
    public void testSouthPole() {
        Place point1 = new Place();
        point1.put("latitude", "-90");
        point1.put("longitude", "0");

        Place point2 = new Place();
        point2.put("latitude", "-89");
        point2.put("longitude", "45");

        assertTrue(calc.between(point1, point2, earthRadius) > 0);
    }

    // Should never be reached according to Dave (maybe do postman for this?)
    // @Test
    // @DisplayName("zack722: Test for invalid earth radius")
    // public void testInvalidEarthRadius() {
    // assertThrows(IllegalArgumentException.class, () -> calc.between(from, to,
    // -1));
    // }

    // Should never be reached according to Dave (maybe do postman for this?)
    // @Test
    // @DisplayName("zack722: Test null 'from' Place")
    // public void testNullFromPlace() {
    // assertThrows(IllegalArgumentException.class, () -> calc.between(null, to,
    // earthRadius));
    // }

    // Should never be reached according to Dave (maybe do postman for this?)
    // @Test
    // @DisplayName("zack722: Test null 'to' Place")
    // public void testNullToPlace() {
    // assertThrows(IllegalArgumentException.class, () -> calc.between(from, null,
    // earthRadius));
    // }

    @Test
    @DisplayName("zack722: Test very close points")
    public void testVeryClosePoints() {
        Place point1 = new Place();
        point1.put("latitude", "10.000000001");
        point1.put("longitude", "20.000000001");

        Place point2 = new Place();
        point2.put("latitude", "10.000000002");
        point2.put("longitude", "20.000000002");

        assertEquals(0, calc.between(point1, point2, earthRadius));
    }
}
