package com.tco.misc;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tco.requests.Place;
import com.tco.requests.Places;

public abstract class TourOptimizer {

    private static final Logger log = LoggerFactory.getLogger(TourOptimizer.class);

    public abstract Places construct(Places places, double radius, String formula, double response);

    public abstract void improve();

    
    protected Places constructInitialTour(Places places, double radius, String formula) {
        long time = System.currentTimeMillis();
        if (places == null || places.isEmpty()) {
            return places;
        }

        Place start = places.get(0);
        List<Place> unvisited = new ArrayList<>(places);
        Places optimizedTour = new Places();
        Place current = start;
        optimizedTour.add(current);
        unvisited.remove(current);

        while (!unvisited.isEmpty()) {
            Place nearest = OneOptimizer.nearestNeighbor(current, unvisited, formula, radius);
            optimizedTour.add(nearest);
            unvisited.remove(nearest);
            current = nearest;
        }
        time = System.currentTimeMillis() - time;
        log.info("OneOpt time: " + time);
        return optimizedTour;
    }
}
