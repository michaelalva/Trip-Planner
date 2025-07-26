package com.tco.misc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tco.requests.Place;

public class TestCosinesCalculator {

    private CosinesCalculator calc;
    private double earthRadius;
    private Place from;
    private Place to;

    @BeforeEach
    public void init() {
        earthRadius = 6378;
        from = new Place();
        from.put("latitude", "100");
        from.put("longitude", "100");
        to = new Place();
        to.put("latitude", "50");
        to.put("longitude", "50");
        calc = new CosinesCalculator();
    }

    @Test
    @DisplayName("leoo: Test to see if between method generates correct number.")
    public void TestCosinesBetween() {
        assertEquals(calc.between(from, to, earthRadius), 5226);
    }



}
