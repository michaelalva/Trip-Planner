package com.tco.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tco.misc.OptimizerFactory;
import com.tco.misc.TourOptimizer;
import com.tco.misc.BadRequestException;

public class TourRequest extends Request {

    private static final Logger log = LoggerFactory.getLogger(TourRequest.class);

    private Places places;
    private Double earthRadius;
    private String formula;
    private Double response;

    @Override
    public void buildResponse() throws BadRequestException {

        int N  = places.size();
        TourOptimizer optimizer = OptimizerFactory.get(N, this.response);

        this.places = optimizer.construct(this.places, this.earthRadius, this.formula, this.response);

        log.trace("buildResponse{}->", this);
    }
}
