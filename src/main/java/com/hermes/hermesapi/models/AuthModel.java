package com.hermes.hermesapi.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthModel implements Serializable {

    private static final long serialVersionUID = 2636936156391265891L;

    private String username;
    private String password;

    public AuthModel() {
    }

    public AuthModel(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }
}
