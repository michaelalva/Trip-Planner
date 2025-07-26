package com.tco.requests;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tco.misc.GeographicLocations;

public class FindRequest extends Request {

    private static final Logger log = LoggerFactory.getLogger(DistancesRequest.class);

    private String match;
    private List<String> type;
    private List<String> where;
    private Integer limit;
    private Integer found;
    private Places places;

    public void buildResponse() throws Exception {
        this.places = GeographicLocations.find(match, type, where, limit);
        this.found = GeographicLocations.found(match, type, where);
        log.trace("buildResponse->{}", this);
    }

    // Testing purposes only
    public FindRequest() {}

    public FindRequest(String match, List<String> type, List<String> where, Integer limit) {
        this.requestType = "find";
        this.match = match;
        this.type = type;
        this.where = where;
        this.limit = limit;
    }

    public Integer getFound() {
        return this.found;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public Places getPlaces() {
        return this.places;
    }

    public String getMatch() {
        return this.match;
    }

    public List<String> getType() {
        return this.type;
    }

    public List<String> getWhere() {
        return this.where;
    }

}
