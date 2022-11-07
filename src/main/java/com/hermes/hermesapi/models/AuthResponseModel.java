package com.hermes.hermesapi.models;

import java.io.Serializable;

public class AuthResponseModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String token;

    public AuthResponseModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
