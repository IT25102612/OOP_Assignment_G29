package com.tranquility.models;

public class Payment {

    private String paymentId;
    private String reservationId;
    private double amount;
    private String method;
    private String status;
    private String paymentDate;

    public Payment(String paymentId, String reservationId, double amount,
                   String method, String status, String paymentDate) {

        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toFileString() {
        return paymentId + "," + reservationId + "," + amount + "," +
                method + "," + status + "," + paymentDate;
    }
}