package com.wallet.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class RegisterRequest implements Serializable {

    @Expose
    private String mobileNumber;
    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String mobileNumber, String name, String email, String password) {
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "mobileNumber='" + mobileNumber + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
