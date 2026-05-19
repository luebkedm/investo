package com.investo.ui;

import com.investo.InvestmentStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.function.Consumer;

public class StrategyListView extends ListView<InvestmentStrategy> {
    private final ObservableList<InvestmentStrategy> strategies;

    public StrategyListView(Runnable refreshCallback, Consumer<InvestmentStrategy> onSelectionChanged) {
        this.strategies = FXCollections.observableArrayList();
        this.setItems(strategies);

        this.setCellFactory(param -> new StrategyCell(refreshCallback));
        this.setPrefHeight(220);

        this.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            onSelectionChanged.accept(newValue);
        });

        this.getSelectionModel().selectFirst();
    }

    public ObservableList<InvestmentStrategy> getStrategies() {
        return strategies;
    }

    public InvestmentStrategy getSelectedStrategy() {
        return getSelectionModel().getSelectedItem();
    }
}