package com.investo;

public class InvestmentStrategy {
    private double startAmount;
    private double annualInterestRate;
    private double monthlySaveAmount;
    private String lineColor;
    private double extraPaymentAmount;
    private int extraPaymentYear;
    private double monthlyPayout;

    public InvestmentStrategy() {
        this(10000, 3, 0, "#1f77b4");
    }

    public InvestmentStrategy(double startAmount, double annualInterestRate, double monthlySaveAmount, String lineColor) {
        this.startAmount = startAmount;
        this.annualInterestRate = annualInterestRate;
        this.monthlySaveAmount = monthlySaveAmount;
        this.lineColor = lineColor;
        this.extraPaymentAmount = 0;
        this.extraPaymentYear = AppConfig.START_YEAR;
        this.monthlyPayout = 2500.0;
    }

    public double getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(double startAmount) {
        this.startAmount = startAmount;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public double getMonthlySaveAmount() {
        return monthlySaveAmount;
    }

    public void setMonthlySaveAmount(double monthlySaveAmount) {
        this.monthlySaveAmount = monthlySaveAmount;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public double getExtraPaymentAmount() {
        return extraPaymentAmount;
    }

    public void setExtraPaymentAmount(double extraPaymentAmount) {
        this.extraPaymentAmount = extraPaymentAmount;
    }

    public int getExtraPaymentYear() {
        return extraPaymentYear;
    }

    public void setExtraPaymentYear(int extraPaymentYear) {
        this.extraPaymentYear = extraPaymentYear;
    }

    public double getMonthlyPayout() {
        return monthlyPayout;
    }

    public void setMonthlyPayout(double monthlyPayout) {
        this.monthlyPayout = monthlyPayout;
    }
}
