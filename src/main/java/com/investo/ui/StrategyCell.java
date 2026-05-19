package com.investo.ui;

import com.investo.InvestmentStrategy;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class StrategyCell extends ListCell<InvestmentStrategy> {
    private final TextField startAmountField = new TextField();
    private final TextField interestRateField = new TextField();
    private final TextField monthlySaveField = new TextField();
    private final TextField monthlyPayoutField = new TextField();
    private final TextField extraAmountField = new TextField();
    private final TextField extraYearField = new TextField();
    private final Button lineColorButton = new Button("Color");
    private final ColorPicker lineColorPicker = new ColorPicker();
    private final HBox content;
    private final Runnable refreshCallback;

    public StrategyCell(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;

        startAmountField.setPrefWidth(90);
        interestRateField.setPrefWidth(80);
        monthlySaveField.setPrefWidth(80);
        monthlyPayoutField.setPrefWidth(80);
        extraAmountField.setPrefWidth(80);
        extraYearField.setPrefWidth(60);
        lineColorButton.setPrefWidth(90);
        startAmountField.setStyle("-fx-font-size: 12; -fx-padding: 4;");
        interestRateField.setStyle("-fx-font-size: 12; -fx-padding: 4;");
        monthlySaveField.setStyle("-fx-font-size: 12; -fx-padding: 4;");
        monthlyPayoutField.setStyle("-fx-font-size: 12; -fx-padding: 4;");
        extraAmountField.setStyle("-fx-font-size: 12; -fx-padding: 4;");
        extraYearField.setStyle("-fx-font-size: 12; -fx-padding: 4;");
        lineColorButton.setStyle("-fx-font-size: 12; -fx-padding: 4;");

        lineColorPicker.setVisible(false);
        lineColorPicker.setManaged(false);

        HBox.setHgrow(startAmountField, Priority.NEVER);
        HBox.setHgrow(interestRateField, Priority.NEVER);
        HBox.setHgrow(monthlySaveField, Priority.NEVER);
        HBox.setHgrow(monthlyPayoutField, Priority.NEVER);
        HBox.setHgrow(extraAmountField, Priority.NEVER);
        HBox.setHgrow(extraYearField, Priority.NEVER);
        HBox.setHgrow(lineColorButton, Priority.NEVER);

        Label extraLabel = new Label("Extra:");
        HBox.setMargin(extraLabel, new Insets(0, 0, 0, 30));

        content = new HBox(10,
                new Label("Start:"), startAmountField,
                new Label("Rate:"), interestRateField,
                new Label("Save:"), monthlySaveField,
                new Label("Payout:"), monthlyPayoutField,
                extraLabel, extraAmountField,
                new Label("at Yr:"), extraYearField,
                new Label("Color:"), lineColorButton, lineColorPicker);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(6));
        content.setStyle("-fx-border-color: #d0d0d0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #d0d0d0; -fx-text-fill: black;");

        startAmountField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                updateItemValues();
            }
        });
        startAmountField.setOnAction(e -> updateItemValues());

        interestRateField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                updateItemValues();
            }
        });
        interestRateField.setOnAction(e -> updateItemValues());

        monthlySaveField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                updateItemValues();
            }
        });
        monthlySaveField.setOnAction(e -> updateItemValues());

        monthlyPayoutField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                updateItemValues();
            }
        });
        monthlyPayoutField.setOnAction(e -> updateItemValues());

        extraAmountField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                updateItemValues();
            }
        });
        extraAmountField.setOnAction(e -> updateItemValues());

        extraYearField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                updateItemValues();
            }
        });
        extraYearField.setOnAction(e -> updateItemValues());

        lineColorButton.setOnAction(e -> lineColorPicker.show());
        lineColorPicker.setOnAction(e -> {
            updateItemValues();
            updateButtonColor();
        });
    }

    @Override
    protected void updateItem(InvestmentStrategy strategy, boolean empty) {
        super.updateItem(strategy, empty);
        if (empty || strategy == null) {
            setGraphic(null);
            setText(null);
        } else {
            startAmountField.setText(String.valueOf(strategy.getStartAmount()));
            interestRateField.setText(String.valueOf(strategy.getAnnualInterestRate()));
            monthlySaveField.setText(String.valueOf(strategy.getMonthlySaveAmount()));
            monthlyPayoutField.setText(String.valueOf(strategy.getMonthlyPayout()));
            extraAmountField.setText(String.valueOf(strategy.getExtraPaymentAmount()));
            extraYearField.setText(String.valueOf(strategy.getExtraPaymentYear()));
            lineColorPicker.setValue(parseColor(strategy.getLineColor()));
            updateButtonColor();
            setGraphic(content);
        }
    }

    private void updateItemValues() {
        InvestmentStrategy strategy = getItem();
        if (strategy == null) {
            return;
        }
        try {
            strategy.setStartAmount(Double.parseDouble(startAmountField.getText()));
            strategy.setAnnualInterestRate(Double.parseDouble(interestRateField.getText()));
            strategy.setMonthlySaveAmount(Double.parseDouble(monthlySaveField.getText()));
            strategy.setMonthlyPayout(Double.parseDouble(monthlyPayoutField.getText()));
            strategy.setExtraPaymentAmount(Double.parseDouble(extraAmountField.getText()));
            strategy.setExtraPaymentYear(Integer.parseInt(extraYearField.getText()));
            strategy.setLineColor(toHexColor(lineColorPicker.getValue()));
            refreshCallback.run();
        } catch (NumberFormatException ignored) {
            // Ignore invalid entry until corrected
        }
    }

    private void updateButtonColor() {
        Color color = lineColorPicker.getValue();
        if (color != null) {
            lineColorButton.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-font-size: 12; -fx-padding: 4;",
                    toHexColor(color), color.getBrightness() < 0.5 ? "white" : "black"));
        }
    }

    private Color parseColor(String raw) {
        try {
            return Color.web(raw);
        } catch (Exception e) {
            return Color.BLACK;
        }
    }

    private String toHexColor(Color color) {
        return String.format("#%02X%02X%02X",
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255));
    }
}
