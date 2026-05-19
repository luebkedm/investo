package com.investo.ui;

import com.investo.AppConfig;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class InvestmentChartPane extends StackPane {
    private final LineChart<Number, Number> lineChart;
    private final Label hoverLabel;

    public InvestmentChartPane(String title) {
        this.lineChart = createLineChart(title);
        this.hoverLabel = createHoverLabel();

        getChildren().addAll(lineChart, hoverLabel);
        setAlignment(hoverLabel, Pos.TOP_LEFT);

        lineChart.setOnMouseMoved(event -> updateHoverLabel(event.getX(), event.getY()));
        lineChart.setOnMouseExited(event -> hoverLabel.setVisible(false));
    }

    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }

    public void setYearRange(int startYear, int endYear) {
        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setLowerBound(startYear);
        xAxis.setUpperBound(endYear);
        xAxis.setTickUnit(Math.max(1, (endYear - startYear) / 4.0));
    }

    private LineChart<Number, Number> createLineChart(String title) {
        NumberAxis xAxis = new NumberAxis(AppConfig.START_YEAR, AppConfig.END_YEAR, 5);
        xAxis.setLabel("Year");
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (EUR)");
        yAxis.setAutoRanging(true);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setCreateSymbols(false);
        chart.setStyle("-fx-font-size: 12;");

        return chart;
    }

    private Label createHoverLabel() {
        Label label = new Label();
        label.setStyle(
                "-fx-background-color: rgba(255,255,255,0.85); -fx-border-color: #999999; -fx-padding: 6; -fx-border-radius: 4; -fx-background-radius: 4;");
        label.setMouseTransparent(true);
        label.setVisible(false);
        return label;
    }

    private void updateHoverLabel(double mouseX, double mouseY) {
        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();

        Point2D scenePoint = lineChart.localToScene(mouseX, mouseY);
        double xValue = xAxis.getValueForDisplay(xAxis.sceneToLocal(scenePoint).getX()).doubleValue();
        double yValue = yAxis.getValueForDisplay(yAxis.sceneToLocal(scenePoint).getY()).doubleValue();

        String date = formatTimePoint(xValue);
        String amount = String.format("€%,.2f", yValue);
        hoverLabel.setText(date + "\n" + amount);

        double offset = 12;
        double labelWidth = hoverLabel.prefWidth(-1);
        double labelHeight = hoverLabel.prefHeight(-1);
        double chartWidth = lineChart.getWidth();
        double chartHeight = lineChart.getHeight();
        Point2D labelPoint = lineChart.localToParent(mouseX, mouseY);

        double labelX = labelPoint.getX() + offset;
        double labelY = labelPoint.getY() + offset;
        if (labelX + labelWidth > chartWidth) {
            labelX = labelPoint.getX() - labelWidth - offset;
        }
        if (labelY + labelHeight > chartHeight) {
            labelY = labelPoint.getY() - labelHeight - offset;
        }

        hoverLabel.setTranslateX(Math.max(0, labelX));
        hoverLabel.setTranslateY(Math.max(0, labelY));
        hoverLabel.setVisible(true);
    }

    private String formatTimePoint(double timePoint) {
        int year = (int) Math.floor(timePoint);
        int month = (int) Math.round((timePoint - year) * AppConfig.MONTHS_PER_YEAR) + 1;
        if (month < 1) {
            month = 1;
        } else if (month > 12) {
            month = 12;
        }
        return String.format("%d-%02d", year, month);
    }
}
