package com.tco.misc;

import com.tco.requests.Place;
import com.tco.requests.Places;

import java.util.List;

public class GeographicLocations {

    public static Places find(String match, List<String> type, List<String> where, Integer limit) throws Exception {
        String sql = Select.match(match, limit);
        return Database.places(sql, limit);
    }

    public static Integer found(String match, List<String> type, List<String> where) throws Exception {
        String sql = Select.found(match);
        return Database.found(sql);
    }

    public static Places near(Place place, Integer distance, Double earthRadius, String formula, Integer limit) throws Exception {
        String sql = Select.near(place.latDegrees(), place.lonDegrees(), limit);
        return Database.places(sql, limit);
    }

    public static Distances distances(Place place, Places places) throws Exception {
        Distances distances = new Distances();
        DistanceCalculator calc = CalculatorFactory.get("vincenty");
        for (int i = 0; i < places.size(); i++) {
            distances.add(calc.between(place, places.get(i), 6371.0));
        }
        return distances;
    }

}
