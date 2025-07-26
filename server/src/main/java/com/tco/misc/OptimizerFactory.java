package com.tco.misc;

public class OptimizerFactory {

   public static TourOptimizer get(int N, double response) throws BadRequestException {
      if (response == 0.0 || N > 1200) {
         return new NoOptimizer();
      } else if (N > 250) {
         return new OneOptimizer();
      } else if (N >= 0) { //I think for 2 250 is a good N
         return new TwoOptimizer();
      } else {
         throw new BadRequestException();
      }
   }

   public static TourOptimizer getCustomOptimizer(String optimizer) {
      if (optimizer.equals("one")) return new OneOptimizer();
      if (optimizer.equals("two")) return new TwoOptimizer();
      if (optimizer.equals("three")) return new ThreeOptimizer();
      return new NoOptimizer();
   }
}
