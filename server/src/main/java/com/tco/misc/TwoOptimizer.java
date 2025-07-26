package com.tco.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tco.requests.Place;
import com.tco.requests.Places;

public class TwoOptimizer extends TourOptimizer {

        private static final Logger log = LoggerFactory.getLogger(TwoOptimizer.class);


    private DistanceCalculator calculator;
    private double radius;
    private Places optimizedTour;

    @Override
    public Places construct(Places places, double radius, String formula, double response) {
        long time = System.currentTimeMillis();
        this.radius = radius;
        this.optimizedTour = constructInitialTour(places, radius, formula);
        try {
            this.calculator = CalculatorFactory.get(formula);
        } catch (BadRequestException e) {
            return null;
        }
        improve();
        time = System.currentTimeMillis() - time;
        log.info("TwoOpt time: " + time);
        log.info("Empty = ", this.optimizedTour.isEmpty());
        return this.optimizedTour;
    }
    
    @Override
    public void improve() {
        if (optimizedTour == null || optimizedTour.size() < 3) {
            return;
        }
        twoOpt(optimizedTour);
    }


    public void twoOpt(Places route) {
        // log.info("Two Opt reached");
        boolean improvement = true;
        int n = route.size();
        while (improvement) {
            improvement = false;
            for (int i = 0; i <= n - 3; i++) {
                for (int k = i + 2; k <= n - 1; k++) {
                    if (twoOptImproves(route, i, k)) {
                        twoOptReverse(route, i + 1, k);
                        improvement = true;
                    }
                }
            }
        }
    }

    public boolean twoOptImproves(Places route, int i, int k) { // is new leg distances less than current leg distance
        // log.info("Two Opt Improve reached");
        return (legDistances(route, i, k) + legDistances(route, i + 1, k + 1))
            < (legDistances(route, i, i + 1) + legDistances(route, k, k + 1));
    }

    private double legDistances(Places route, int i, int k){ // Used ChatGPT
        if (i < 0 || k < 0 || i >= route.size() || k >= route.size()) {
            return Double.MAX_VALUE; // Prevent out-of-bounds errors
        }
        // log.info("Reached here!");
        Place place1 = route.get(i);
        Place place2 = route.get(k);

        return calculator.between(place1, place2, radius);
    }

    public static void twoOptReverse(Places route, int i, int k) { // reverse in place
        while (i < k) {
            Place temp = route.get(i);
            route.set(i, route.get(k));
            route.set(k, temp);
            i++;
            k--;
        }
    }

}

