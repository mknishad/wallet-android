package com.wallet.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Transfer implements Serializable {

    @Expose
    private String amount;
    @Expose
    private String pinCode;
    @Expose
    private String message;
    @Expose
    private double balance;

    @Override
    public String toString() {
        return "Transfer{" +
                "amount='" + amount + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", message='" + message + '\'' +
                ", balance=" + balance +
                '}';
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
