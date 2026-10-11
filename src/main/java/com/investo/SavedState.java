package com.investo;

import java.util.ArrayList;
import java.util.List;

public class SavedState {
    private List<InvestmentStrategy> strategies;
    private double pension;

    public SavedState() {
        this.strategies = new ArrayList<>();
        this.pension = 0.0;
    }

    public List<InvestmentStrategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<InvestmentStrategy> strategies) {
        this.strategies = strategies;
    }

    public double getPension() {
        return pension;
    }

    public void setPension(double pension) {
        this.pension = pension;
    }
}
