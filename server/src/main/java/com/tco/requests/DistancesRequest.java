package com.tco.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tco.misc.Distances;
import com.tco.misc.DistanceCalculator;
import com.tco.misc.CalculatorFactory;
import com.tco.misc.BadRequestException;

public class DistancesRequest extends Request {

   private static final Logger log = LoggerFactory.getLogger(DistancesRequest.class);

   private Places places;
   private Distances distances;
   private Double earthRadius;
   private String formula;

   @Override
   public void buildResponse() throws BadRequestException {
      distances = new Distances();
      calculateDistances();

      log.trace("Distances computed -> {}", distances);
      log.trace("buildResponse -> {}", this);
   }

   public void calculateDistances() throws BadRequestException {

      CalculatorFactory calculatorFactory = new CalculatorFactory();
      DistanceCalculator calc = calculatorFactory.get(formula);

      for (int i = 0; i < places.size(); i++) {
         distances.add(calc.between(places.get(i), places.get((i + 1) % places.size()), earthRadius));
      }
   }

   // Overloaded method to match test cases
   public void calculateDistances(String formula) throws BadRequestException {
      this.formula = formula;
      calculateDistances();
   }

   // Getter for formula (fixes test case errors)
   public String getFormula() {
      return this.formula;
   }

   // Testing purposes only
   public DistancesRequest() {
   }

   public DistancesRequest(Places places, Double earthRadius) {
      this.requestType = "distances";
      this.places = places;
      this.distances = new Distances();
      this.earthRadius = earthRadius; // Default radius (Earth in km)
      this.formula = "vincenty"; // Default formula
   }

   public boolean distancesNotNull() {
      return this.distances != null;
   }

   public boolean compareDistances(Distances testDistances) {
      return testDistances.equals(distances);
   }
}
