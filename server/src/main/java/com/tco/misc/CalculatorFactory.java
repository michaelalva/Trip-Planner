package com.tco.misc;

public class CalculatorFactory {

   public static DistanceCalculator get(String formula) throws BadRequestException {
      if (formula == null) {
         return new VincentyCalculator();
      } else {
         switch (formula.toLowerCase()) {
            case "cosines":
               return new CosinesCalculator();
            case "vincenty":
               return new VincentyCalculator();
            case "haversine":
               return new HaversineCalculator();
            default:
               throw new BadRequestException();
         }
      }
   }

}
