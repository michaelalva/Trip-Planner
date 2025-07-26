package com.tco.misc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class HaversineCalculatorTest {

    private void test(GeographicCoordinate from, GeographicCoordinate to, double earthRadius, long expected) {
        HaversineCalculator calculator = new HaversineCalculator();
        assertEquals(expected, calculator.between(from, to, earthRadius), 50);
    }

    @Test
    @DisplayName("zack722: Distance between same points should be zero")
    void testDistanceBetweenSamePoints() {
        GeographicCoordinate point = new TestGeoCoordinate(0, 0);
        test(point, point, 6371.0, 0);
    }

    @Test
    @DisplayName("zack722: Distance between Denver, CO and Los Angeles, CA")
    void testDistanceBetweenDifferentPoints() {
        GeographicCoordinate point1 = new TestGeoCoordinate(39.7392, -104.9903);
        GeographicCoordinate point2 = new TestGeoCoordinate(34.0522, -118.2437);
        test(point1, point2, 6371.0, 1337);
    }

    // Should never be reached according to Dave
    // @Test
    // @DisplayName("zack722: Null coordinates should throw exception")
    // void testNullCoordinates() {
    // HaversineCalculator calculator = new HaversineCalculator();
    // assertThrows(IllegalArgumentException.class, () -> {
    // calculator.between(null, new TestGeoCoordinate(0, 0), 6371.0);
    // });
    // }

    // Should never be reached according to Dave
    // @Test
    // @DisplayName("bryce24: Negative earth radius should throw exception")
    // void testNegativeEarthRadius() {
    // GeographicCoordinate point1 = new TestGeoCoordinate(0, 0);
    // GeographicCoordinate point2 = new TestGeoCoordinate(10, 10);
    // HaversineCalculator calculator = new HaversineCalculator();
    // assertThrows(IllegalArgumentException.class, () -> {
    // calculator.between(point1, point2, -6371.0);
    // });
    // }

    private static class TestGeoCoordinate implements GeographicCoordinate {
        private final double lat;
        private final double lon;

        public TestGeoCoordinate(double lat, double lon) {
            this.lat = Math.toRadians(lat);
            this.lon = Math.toRadians(lon);
        }

        @Override
        public double latRadians() {
            return lat;
        }

        @Override
        public double lonRadians() {
            return lon;
        }
    }
}
