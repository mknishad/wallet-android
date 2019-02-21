package com.wallet.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class QrTransfer implements Serializable {

    @Expose
    private String mobileNumber;
    @Expose
    private String amount;
    @Expose
    private String pinCode;
    @Expose
    private String message;
    @Expose
    private String qrCode;
    @Expose
    private double balance;

    @Override
    public String toString() {
        return "QrTransfer{" +
                "mobileNumber='" + mobileNumber + '\'' +
                ", amount='" + amount + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", message='" + message + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", balance=" + balance +
                '}';
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
