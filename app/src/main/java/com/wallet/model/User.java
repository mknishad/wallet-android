package com.wallet.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class User implements Serializable {

    @Expose
    private String _id;
    @Expose
    private double balance;
    @Expose
    private String mobileNumber;
    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", balance=" + balance +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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
