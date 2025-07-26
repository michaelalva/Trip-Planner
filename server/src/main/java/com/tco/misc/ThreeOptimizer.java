package com.tco.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tco.requests.Place;
import com.tco.requests.Places;

public class ThreeOptimizer extends TourOptimizer {

    private static final Logger log = LoggerFactory.getLogger(ThreeOptimizer.class);


    private double radius;
    private Places optimizedTour;
    private String formula;

    @Override
    public Places construct(Places places, double radius, String formula, double response) {
        long time = System.currentTimeMillis();
        this.radius = radius;
        this.formula = formula;
        this.optimizedTour = constructInitialTour(places, radius, formula);
        improve();
        time = System.currentTimeMillis() - time;
        log.info("Time for ThreeOptimizer: " + time);
        return this.optimizedTour;
    }


    @Override
    public void improve() {
        if (optimizedTour == null || optimizedTour.size() < 4) {
            return;
        }
        threeOpt(optimizedTour);
    }

    public void threeOpt(Places route) {
        log.info("Three Opt reached");
        boolean improvement = true;
        int n = route.size();
        while (improvement) {
            improvement = false;
            for (int i = 0; i <= n - 3; i++) {
                for (int j = i + 1; j < n - 2; j++) {
                    for (int k = j + 1; k <= n - 1; k++) {
                        int reversals = threeOptReversals(route, i, j, k);
                        if ((reversals & 0b001) > 0) {
                            TwoOptimizer.twoOptReverse(route, i + 1, j);
                            improvement = true;
                        }
                        if ((reversals & 0b010) > 0) {
                            TwoOptimizer.twoOptReverse(route, j + 1, k);
                            improvement = true;
                        }
                        if ((reversals & 0b100) > 0) {
                            TwoOptimizer.twoOptReverse(route, i + 1, k);
                            improvement = true;
                        }
                    }
                }
            }
        }
    }  

    private long calculateDistance(Place p1, Place p2) {
        DistanceCalculator calculator;
        try {
            new CalculatorFactory();
            calculator = CalculatorFactory.get(formula);
        } catch (BadRequestException e) {
            throw new RuntimeException("Invalid formula provided: " + formula, e);
        }
        return calculator.between(p1, p2, radius);
    }

    private int threeOptReversals(Places route, int i, int j, int k) {
        Place a = route.get(i);
        Place b = route.get(i + 1);
        Place c = route.get(j);
        Place d = route.get(j + 1);
        Place e = route.get(k);
        Place f = route.get((k + 1) % route.size());

        long originalDistance = calculateDistance(a, b) +
                                calculateDistance(c, d) +
                                calculateDistance(e, f);
        int bestMask = 0;
        long bestDistance = originalDistance;

        for (int mask = 1; mask < 8; mask++) {
            Places tempRoute = applyReversals(new Places(route), i, j, k, mask);

            Place newA = tempRoute.get(i);
            Place newB = tempRoute.get(i + 1);
            Place newC = tempRoute.get(j);
            Place newD = tempRoute.get(j + 1);
            Place newE = tempRoute.get(k);
            Place newF = tempRoute.get((k + 1) % tempRoute.size());

            long newDistance = calculateDistance(newA, newB) +
                               calculateDistance(newC, newD) +
                               calculateDistance(newE, newF);

            if (newDistance < bestDistance) {
                bestDistance = newDistance;
                bestMask = mask;
            }
        }
        return bestMask;
    }

    private Places applyReversals(Places currentRoute, int i, int j, int k, int mask) {
        Places modifiedRoute = new Places(currentRoute);
        if ((mask & 0b001) > 0) {
            TwoOptimizer.twoOptReverse(modifiedRoute, i + 1, j);
        }
        if ((mask & 0b010) > 0) {
            TwoOptimizer.twoOptReverse(modifiedRoute, j + 1, k);
        }
        if ((mask & 0b100) > 0) {
            TwoOptimizer.twoOptReverse(modifiedRoute, i + 1, k);
        }
        return modifiedRoute;
    }
}