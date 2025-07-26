package com.tco.misc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestOptimizerFactory {
    
    @Test
    @DisplayName("leoo: Test get() with NoOptimizer")
    void testNoOpt() throws BadRequestException {
        TourOptimizer optimizer = OptimizerFactory.get(5000, 0.0d);
        assertTrue(optimizer instanceof NoOptimizer);
    }

    @Test
    @DisplayName("leoo: Test get() with OneOptimizer")
    void testOneOpt() throws BadRequestException {
        TourOptimizer optimizer = OptimizerFactory.get(1000,1.0d);
        assertTrue(optimizer instanceof OneOptimizer);
    }

    @Test
    @DisplayName("duyalva: Test get() with TwoOptimizer")
    void testTwoOpt() throws BadRequestException {
        TourOptimizer optimizer = OptimizerFactory.get(100, 2.0d);
        assertTrue(optimizer instanceof TwoOptimizer);
    }

    // @Test
    // @DisplayName("duyalva: Test get() with ThreeOptimizer")
    // void testThreeOpt() throws BadRequestException {
    //     TourOptimizer optimizer = OptimizerFactory.get(10, 3.0d);
    //     assertTrue(optimizer instanceof ThreeOptimizer);
    // }


    @Test
    @DisplayName("duyalva: Test get() with Bad Request")
    void testBadRequestLargeN() throws BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            OptimizerFactory.get(-10, 1.0d);
        });
    }
}
