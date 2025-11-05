package com.lucidity.endpoints;

import com.lucidity.restclients.Resource;

public enum OfferEndPoints implements Resource {

    ADD_OFFER("/api/v1/offer");
    private String path;

    private OfferEndPoints(String path) {
        this.path = path;
    }

    @Override
    public String getResourceURL() {
        return path;
    }
}
