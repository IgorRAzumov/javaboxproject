package ru.geekbrains.common.model;

import java.io.Serializable;

public class UserData implements Serializable {
    private String name;
    private float freeDiscSpace;
    private String token;

    public UserData(String name, float freeDiscSpace, String token) {
        this.name = name;
        this.freeDiscSpace = freeDiscSpace;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFreeDiscSpace() {
        return freeDiscSpace;
    }

    public void setFreeDiscSpace(float freeDiscSpace) {
        this.freeDiscSpace = freeDiscSpace;
    }

}
