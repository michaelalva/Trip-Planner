package com.tco.misc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import com.tco.requests.Places;

public class TestNoOptimizer {
    
    @Test 
    @DisplayName("bryce24: test if the correct places gets returned.")
    void testPlacesGettingReturned()throws Exception{
        Places testplace = new Places();
        NoOptimizer testnoopt = new NoOptimizer();
        Places result = testnoopt.construct(testplace, 312, null, 0);
        assertEquals(testplace,result);

    }





}
