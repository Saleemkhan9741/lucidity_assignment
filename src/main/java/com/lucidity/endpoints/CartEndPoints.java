package com.lucidity.endpoints;

import com.lucidity.restclients.Resource;

public enum CartEndPoints implements Resource {


    APPLY_OFFER("/api/v1/cart/apply_offer");
    private String path;

    private CartEndPoints(String path) {
        this.path = path;
    }

    @Override
    public String getResourceURL() {
        return path;
    }
}
