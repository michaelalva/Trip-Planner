package com.tco.misc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;


public class TestDistances {
    
    Distances distances;

    @BeforeEach
    public void init() {
        distances =  new Distances(new ArrayList<Long>(Arrays.asList(100L, 200L, 300L)));
    }

    @Test
    @DisplayName("leoo: Distances are correctly added")
    public void testDistancesAddition() {
        assertEquals(distances.totalDistance(), 600);
    }

    @Test
    @DisplayName("cougar:testing the handling of negaitve value inputs")
    public void testDistancesNegativeValues(){
        Distances distances = new Distances();
        distances.add(-20L);
        distances.add(-30L);
        distances.add(40L);
        assertEquals(-10L,distances.totalDistance());
    }

    @Test
    @DisplayName("cougar:testing multiple elements in a list and have do a sum of all of the elements")
    public void testMultipleElements(){
        Distances distances = new Distances();
        distances.add(20L);
        distances.add(30L);
        distances.add(50L);
        assertEquals(100L, distances.totalDistance());
    }
    @Test
    @DisplayName("cougar: adding test cases for single element")
    public void testSingleElementConstructor(){
        ArrayList<Long> distancesList = new ArrayList<>();
        distancesList.add(42L);
        Distances distances = new Distances(distancesList);
        assertEquals(42, distances.totalDistance());
    }

    @Test
    @DisplayName("cougar: testing for empty constructor")
    public void testForEmptyConstructor(){
        Distances distances = new Distances();
        assertEquals(0L, distances.totalDistance());
    }

    @Test
    @DisplayName("cougar: testing for multiple elements")
    public void testForMultipleElements(){
        ArrayList<Long> distancesList = new ArrayList<>();
        distancesList.add(10L);
        distancesList.add(20L);
        distancesList.add(30L);
        Distances distances = new Distances(distancesList);
        assertEquals(60L, distances.totalDistance());
    }
}
