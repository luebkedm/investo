package com.investo.model;

import com.investo.AppConfig;
import java.util.ArrayList;
import java.util.List;

public class Investment {
    private double startAmount;
    private double annualInterestRate;
    private double monthlySaveAmount;
    private String lineColor;
    private double extraPaymentAmount;
    private int extraPaymentYear;
    private double taxRate = 0.25;

    public Investment(double startAmount, double annualInterestRate) {
        this.startAmount = startAmount;
        this.annualInterestRate = annualInterestRate / 100.0;
        this.monthlySaveAmount = 0;
        this.lineColor = "#1f77b4";
    }

    public Investment(double startAmount, double annualInterestRate, String lineColor) {
        this.startAmount = startAmount;
        this.annualInterestRate = annualInterestRate / 100.0;
        this.monthlySaveAmount = 0;
        this.lineColor = lineColor;
    }

    public List<InvestmentDataPoint> calculateGrowth() {
        return calculateGrowth(AppConfig.END_YEAR);
    }

    public List<InvestmentDataPoint> calculateGrowth(int endYear) {
        List<InvestmentDataPoint> dataPoints = new ArrayList<>();
        double currentAmount = startAmount;

        for (int year = AppConfig.START_YEAR; year <= endYear; year++) {
            if (year == extraPaymentYear) {
                currentAmount += extraPaymentAmount;
            }
            for (int month = 1; month <= AppConfig.MONTHS_PER_YEAR; month++) {
                double timePoint = year + (month - 1) / (double) AppConfig.MONTHS_PER_YEAR;
                dataPoints.add(new InvestmentDataPoint(timePoint, currentAmount));
                
                // Add monthly savings
                currentAmount += monthlySaveAmount;
                
                // At the end of the year, calculate interest
                if (month == AppConfig.MONTHS_PER_YEAR) {
                    double interest = currentAmount * annualInterestRate;
                    
                    // Apply tax on the interest gained
                    double taxOnInterest = interest * taxRate;
                    double netInterest = interest - taxOnInterest;
                    
                    // Add net interest to current amount
                    currentAmount += netInterest;
                }
            }
        }

        return dataPoints;
    }

    public List<InvestmentDataPoint> calculatePayout(int startYear, int endYear, double initialAmount, double payoutAmount) {
        List<InvestmentDataPoint> dataPoints = new ArrayList<>();
        double currentAmount = initialAmount;

        for (int year = startYear; year <= endYear; year++) {
            for (int month = 1; month <= AppConfig.MONTHS_PER_YEAR; month++) {
                double timePoint = year + (month - 1) / (double) AppConfig.MONTHS_PER_YEAR;
                dataPoints.add(new InvestmentDataPoint(timePoint, currentAmount));

                currentAmount -= payoutAmount;
                if (currentAmount < 0) currentAmount = 0;

                if (month == AppConfig.MONTHS_PER_YEAR) {
                    double interest = currentAmount * annualInterestRate;
                    double taxOnInterest = interest * taxRate;
                    double netInterest = interest - taxOnInterest;
                    currentAmount += netInterest;
                }
            }
        }
        return dataPoints;
    }

    public void setStartAmount(double startAmount) {
        this.startAmount = startAmount;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate / 100.0;
    }

    public void setMonthlySaveAmount(double monthlySaveAmount) {
        this.monthlySaveAmount = monthlySaveAmount;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public String getLineColor() {
        return lineColor;
    }

    public double getStartAmount() {
        return startAmount;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate * 100;
    }

    public double getMonthlySaveAmount() {
        return monthlySaveAmount;
    }

    public void setExtraPaymentAmount(double extraPaymentAmount) {
        this.extraPaymentAmount = extraPaymentAmount;
    }

    public void setExtraPaymentYear(int extraPaymentYear) {
        this.extraPaymentYear = extraPaymentYear;
    }

    public void setTaxRate(double taxRatePercent) {
        this.taxRate = taxRatePercent / 100.0;
    }

    public double getTaxRate() {
        return taxRate * 100.0;
    }
}
