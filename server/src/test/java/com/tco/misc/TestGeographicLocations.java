package com.tco.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tco.requests.Place;
import com.tco.requests.Places;

public class TestGeographicLocations {

    final static Place place = new Place("10", "10");
    final static Integer distance = 50;
    final static Double earthRadius = 3959d;
    final static String formula = "haversine";
    final static Integer limit = 1;

    private static Places near = null;

    @BeforeAll
    public static void setup() throws Exception {
        System.out.println("TestGeographicLocations: setup");
        near = GeographicLocations.near(place, distance, earthRadius, formula, limit);
    }

    @Test
    @DisplayName("leoo: Limit correctly applied")
    public void testNearLimit() throws Exception {
        assertTrue(near.size() == limit, "Expected " + limit + " results");
    }

    @Test
    @DisplayName("leoo: Keywords correctly parsed by Database")
    public void testNearKeywords() throws Exception {
        assertEquals(near.get(0).get("keywords"), "Bauchi,Yakubu|Yakoba,Bauchi");
    }

}
