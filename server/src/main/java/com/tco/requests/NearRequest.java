package com.tco.requests;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tco.misc.Distances;
import com.tco.misc.GeographicLocations;

public class NearRequest extends Request {

    private static final Logger log = LoggerFactory.getLogger(NearRequest.class);

    private Place place;
    private Integer distance;
    private Double earthRadius;
    private String formula;
    private Integer limit;
    private Places places;
    private Distances distances;

    @Override
    public void buildResponse() throws Exception {
        this.places = GeographicLocations.near(place, distance, earthRadius, formula, limit);
        this.distances = GeographicLocations.distances(place, places);
        log.trace("buildResponse->{}", this);

    }

    public NearRequest(Integer distance, Place place, Double earthRadius, String formula, Integer limit) {
        this.requestType = "near";
        this.place = place;
        this.distance = distance;
        this.earthRadius = earthRadius;
        this.formula = formula;
        this.limit = limit;
    }

    public Integer getDistance() {
        return this.distance;
    }

    public Distances getDistances() {
        return this.distances;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public Place getPlace() {
        return this.place;
    }

    public Places getPlaces() {
        return this.places;
    }

    public String getFormula() {
        return this.formula;
    }

    public Double getRadius() {
        return this.earthRadius;
    }
}