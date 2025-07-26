package com.tco.misc;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestGeographicCoordinate {
    private Geo geo;
    public class Geo extends HashMap<String, String> implements GeographicCoordinate {

        public Geo(String latitude, String longitude) {
            this.put("latitude", latitude);
            this.put("longitude", longitude);
        }

        @Override
        public double latRadians() {
            double lat = strToDouble(this.get("latitude"));
            return degreesToRadians(lat);
        }

        @Override
        public double lonRadians() {
            double lon = strToDouble(this.get("longitude"));
            return degreesToRadians(lon);
        }
    
        private double degreesToRadians(double num) {
            return num * (Math.PI / 180.0);
        }
        
        private double strToDouble(String str) {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("The given lat/lon value is not a valid double.");
            }
        }
    }

    @BeforeEach
    public void build(){
        geo = new Geo("30.0", "60.0");
    }

    @Test
    @DisplayName("duyalva: Geographic latitude coordinate is 30.0")
    public void testLatRadians() { assertEquals(geo.latRadians(), Math.toRadians(30.0), 1e-6); }

    @Test
    @DisplayName("duyalva: Geographic longitude coordinate is 60.0")
    public void testLonRadians() { assertEquals(geo.lonRadians(), Math.toRadians(60.0), 1e-6); }

    @Test
    @DisplayName("duyalva: Coordinate degrees convert to radians")
    public void testDegreesToRadians() { assertEquals(geo.degreesToRadians(180.0), Math.PI, 1e-6); }

    @Test
    @DisplayName("duyalva: Coordinate String is converted to a Double")
    public void testStrToDouble() { assertEquals(geo.strToDouble("60.5"), 60.5, 1e-6); }

    @Test
    @DisplayName("duyalva: Invalid String")
    public void testFailStrToDouble() { assertThrows(IllegalArgumentException.class, () -> geo.strToDouble("nah")); }

    /*
     * To-do
     * Create multiple Geo points
     * Define the earth radius values to test
     */
}
