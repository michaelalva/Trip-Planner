package com.tco.misc;

import com.tco.requests.Places;
import com.tco.requests.Place;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OneOptimizer extends TourOptimizer {

        private static final Logger log = LoggerFactory.getLogger(OneOptimizer.class);


    @Override
    public Places construct(Places places, double radius, String formula, double response) {
        return constructInitialTour(places, radius, formula);
    }

    public static Place nearestNeighbor(Place current, List<Place> unvisited, String formula, double radius) {

        DistanceCalculator calculator;
        try {
            calculator = new CalculatorFactory().get(formula);
        } catch (BadRequestException e) {
            throw new RuntimeException("Invalid formula provided: " + formula, e);
        }

        Place nearest = null;
        long minDistance = Long.MAX_VALUE;

        for (Place place : unvisited) {
            long distance = calculator.between(current, place, radius);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = place;
            }
        }

        return nearest;
    }

    @Override
    public void improve() {
    }
}
