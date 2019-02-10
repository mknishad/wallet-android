package com.wallet.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class UserLogin implements Serializable {

    @Expose
    private String token;
    @Expose
    private User user;

    @Override
    public String toString() {
        return "UserLogin{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
