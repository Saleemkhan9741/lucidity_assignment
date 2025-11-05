package com.lucidity.endpoints;

import com.lucidity.restclients.Resource;

public enum MockEndPoints implements Resource {

    GET_USER_SEGMENT("/api/v1/user_segment");
    private String path;

    private MockEndPoints(String path) {
        this.path = path;
    }

    @Override
    public String getResourceURL() {
        return path;
    }
}
