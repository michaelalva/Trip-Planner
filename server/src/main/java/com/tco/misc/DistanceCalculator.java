package com.tco.misc;

public interface DistanceCalculator {
    
    public long between(GeographicCoordinate from, GeographicCoordinate to, double earthRadius);

}