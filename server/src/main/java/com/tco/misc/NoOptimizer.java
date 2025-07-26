package com.tco.misc;

import com.tco.requests.Places;

public class NoOptimizer extends TourOptimizer {

    public Places construct(Places places, double radius, String formula, double response) {
        return places;
    }

    public void improve() {

    }

}
