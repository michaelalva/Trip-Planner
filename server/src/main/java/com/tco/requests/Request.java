package com.tco.requests;


public abstract class Request {

    protected String requestType;

    public String getRequestType() {
        return requestType;
    }

    // Overrideable Methods
    public abstract void buildResponse() throws Exception;
}