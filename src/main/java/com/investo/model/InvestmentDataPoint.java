package com.investo.model;

public class InvestmentDataPoint {
    private double timePoint;
    private double amount;

    public InvestmentDataPoint(double timePoint, double amount) {
        this.timePoint = timePoint;
        this.amount = amount;
    }

    public double getTimePoint() {
        return timePoint;
    }

    public double getAmount() {
        return amount;
    }
}
