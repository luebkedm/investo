package com.investo.ui;

import com.investo.AppConfig;
import com.investo.InvestmentStrategy;
import com.investo.model.Investment;
import com.investo.model.InvestmentDataPoint;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InvestmentController implements Initializable {
    private InvestmentChartPane chartPane;
    private LineChart<Number, Number> lineChart;
    private InvestmentChartPane payoutChartPane;
    private LineChart<Number, Number> payoutLineChart;
    private StrategyListView strategyListView;
    private Button deleteStrategyButton;
    private Label finalAmountLabel;
    private TextField endYearField;
    private TextField phase2EndYearField;
    private TextField taxRateField;

    private static final Path STORAGE_PATH = Paths.get(System.getProperty("user.home"), ".investo", "strategies.xml");

    public void show(Stage primaryStage) {
        VBox mainLayout = new VBox();
        mainLayout.setStyle("-fx-font-family: 'Segoe UI', Arial; -fx-font-size: 12;");
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(15));

        HBox titleBar = createTitleBar();

        HBox chartsBox = new HBox(10);
        chartPane = new InvestmentChartPane("Accumulation Phase");
        lineChart = chartPane.getLineChart();
        payoutChartPane = new InvestmentChartPane("Payout Phase");
        payoutLineChart = payoutChartPane.getLineChart();
        chartsBox.getChildren().addAll(chartPane, payoutChartPane);
        HBox.setHgrow(chartPane, Priority.ALWAYS);
        HBox.setHgrow(payoutChartPane, Priority.ALWAYS);

        HBox controlButtons = createStrategyButtons();
        strategyListView = new StrategyListView(this::updateChart, this::handleSelectionChanged);
        loadStrategies();

        mainLayout.getChildren().addAll(titleBar, chartsBox, controlButtons, strategyListView);
        VBox.setVgrow(chartsBox, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, 1400, 900);
        primaryStage.setTitle("Investo - Investment Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSelectionChanged(InvestmentStrategy strategy) {
        if (deleteStrategyButton != null) {
            deleteStrategyButton.setDisable(strategy == null);
        }
        updateFinalAmountLabel(strategy);
    }

    private HBox createTitleBar() {
        HBox titleBar = new HBox();
        titleBar.setSpacing(10);
        titleBar.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Investo");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label taxLabel = new Label("Tax (%):");
        taxRateField = new TextField("25.0");
        taxRateField.setPrefWidth(60);
        taxRateField.setStyle("-fx-font-size: 12; -fx-padding: 4;");
        taxRateField.setOnAction(e -> updateChart());
        taxRateField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                updateChart();
            }
        });

        Label endYearLabel = new Label("End Year:");
        endYearField = new TextField(String.valueOf(AppConfig.END_YEAR));
        endYearField.setPrefWidth(80);
        endYearField.setStyle("-fx-font-size: 12; -fx-padding: 4;");
        endYearField.setOnAction(e -> updateChart());
        endYearField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                updateChart();
            }
        });

        Label p2EndYearLabel = new Label("P2 End Year:");
        phase2EndYearField = new TextField("2065");
        phase2EndYearField.setPrefWidth(80);
        phase2EndYearField.setStyle("-fx-font-size: 12; -fx-padding: 4;");
        phase2EndYearField.setOnAction(e -> updateChart());
        phase2EndYearField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                updateChart();
            }
        });

        titleBar.getChildren().addAll(titleLabel, spacer, taxLabel, taxRateField, endYearLabel, endYearField, p2EndYearLabel, phase2EndYearField);
        return titleBar;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        updateChart();
    }

    private HBox createStrategyButtons() {
        HBox buttonsBox = new HBox();
        buttonsBox.setSpacing(20);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setAlignment(Pos.CENTER_LEFT);
        buttonsBox.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-padding: 10;");

        Button addStrategyButton = new Button("Add Strategy");
        addStrategyButton.setOnAction(e -> addStrategy());
        addStrategyButton.setStyle("-fx-font-size: 12; -fx-padding: 8 20;");

        deleteStrategyButton = new Button("Delete Strategy");
        deleteStrategyButton.setDisable(true);
        deleteStrategyButton.setOnAction(e -> deleteSelectedStrategy());
        deleteStrategyButton.setStyle("-fx-font-size: 12; -fx-padding: 8 20;");

        finalAmountLabel = new Label("Final Amount: €0.00");
        finalAmountLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        finalAmountLabel.setMinWidth(220);

        buttonsBox.getChildren().addAll(addStrategyButton, deleteStrategyButton, finalAmountLabel);
        return buttonsBox;
    }

    private void updateChart() {
        try {
            int endYear = AppConfig.END_YEAR;
            try {
                endYear = Integer.parseInt(endYearField.getText());
            } catch (NumberFormatException ignored) {
                showErrorAlert("End year must be a valid integer");
                return;
            }
            int phase2EndYear = 2065;
            try {
                phase2EndYear = Integer.parseInt(phase2EndYearField.getText());
            } catch (NumberFormatException ignored) {
                showErrorAlert("P2 End year must be a valid integer");
                return;
            }
            if (endYear < AppConfig.START_YEAR) {
                showErrorAlert("End year must be >= " + AppConfig.START_YEAR);
                return;
            }
            if (phase2EndYear < endYear) {
                showErrorAlert("P2 End year must be >= Phase 1 End year");
                return;
            }
            double taxRate;
            try {
                taxRate = Double.parseDouble(taxRateField.getText());
            } catch (NumberFormatException ignored) {
                showErrorAlert("Tax rate must be a valid number");
                return;
            }

            chartPane.setYearRange(AppConfig.START_YEAR, endYear);
            payoutChartPane.setYearRange(endYear, phase2EndYear);

            lineChart.getData().clear();
            payoutLineChart.getData().clear();

            for (InvestmentStrategy strategy : strategyListView.getStrategies()) {
                Investment investment = new Investment(strategy.getStartAmount(), strategy.getAnnualInterestRate(), strategy.getLineColor());
                investment.setMonthlySaveAmount(strategy.getMonthlySaveAmount());
                investment.setExtraPaymentAmount(strategy.getExtraPaymentAmount());
                investment.setExtraPaymentYear(strategy.getExtraPaymentYear());
                investment.setTaxRate(taxRate);

                List<InvestmentDataPoint> dataPoints = investment.calculateGrowth(endYear);
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(String.format("€%.0f @ %.2f%% + €%.0f/mo", strategy.getStartAmount(),
                        strategy.getAnnualInterestRate(), strategy.getMonthlySaveAmount()));

                for (InvestmentDataPoint point : dataPoints) {
                    series.getData().add(new XYChart.Data<>(point.getTimePoint(), point.getAmount()));
                }

                String lineColor = normalizeColor(strategy.getLineColor());
                series.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle(String.format("-fx-stroke: %s; -fx-stroke-width: 2px;", lineColor));
                    }
                });
                lineChart.getData().add(series);

                // Calculate Phase 2
                double p2StartAmount = dataPoints.isEmpty() ? strategy.getStartAmount() : dataPoints.get(dataPoints.size() - 1).getAmount();
                List<InvestmentDataPoint> p2Points = investment.calculatePayout(endYear, phase2EndYear, p2StartAmount, strategy.getMonthlyPayout());
                
                XYChart.Series<Number, Number> p2Series = new XYChart.Series<>();
                p2Series.setName(String.format("Payout: €%.0f/mo", strategy.getMonthlyPayout()));
                
                for (InvestmentDataPoint point : p2Points) {
                    p2Series.getData().add(new XYChart.Data<>(point.getTimePoint(), point.getAmount()));
                }

                p2Series.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle(String.format("-fx-stroke: %s; -fx-stroke-width: 2px;", lineColor));
                    }
                });
                payoutLineChart.getData().add(p2Series);
            }

            updateFinalAmountLabel(strategyListView.getSelectedStrategy());
            saveStrategies();

        } catch (NumberFormatException e) {
            showErrorAlert("Please enter valid numbers");
        }
    }

    private void addStrategy() {
        String[] defaultColors = { "#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd", "#8c564b" };
        String color = defaultColors[strategyListView.getStrategies().size() % defaultColors.length];
        InvestmentStrategy strategy = new InvestmentStrategy(10000, 3, 0, color);
        strategyListView.getStrategies().add(strategy);
        strategyListView.getSelectionModel().select(strategy);
        updateChart();
    }

    private void deleteSelectedStrategy() {
        InvestmentStrategy selected = strategyListView.getSelectedStrategy();
        if (selected != null) {
            strategyListView.getStrategies().remove(selected);
            deleteStrategyButton.setDisable(strategyListView.getStrategies().isEmpty());
            updateChart();
        }
    }

    private void updateFinalAmountLabel(InvestmentStrategy strategy) {
        if (strategy == null) {
            finalAmountLabel.setText("Final Amount: €0.00");
            return;
        }
        Investment investment = new Investment(strategy.getStartAmount(), strategy.getAnnualInterestRate(), strategy.getLineColor());
        investment.setMonthlySaveAmount(strategy.getMonthlySaveAmount());
        investment.setExtraPaymentAmount(strategy.getExtraPaymentAmount());
        investment.setExtraPaymentYear(strategy.getExtraPaymentYear());
        int endYear = AppConfig.END_YEAR;
        try {
            endYear = Integer.parseInt(endYearField.getText());
        } catch (NumberFormatException ignored) {
            // keep default if invalid
        }
        double taxRate = 25.0;
        try {
            taxRate = Double.parseDouble(taxRateField.getText());
        } catch (NumberFormatException ignored) {
            // keep default if invalid
        }
        investment.setTaxRate(taxRate);

        List<InvestmentDataPoint> points = investment.calculateGrowth(endYear);
        if (points.isEmpty()) {
            finalAmountLabel.setText("Final Amount: €0.00");
        } else {
            finalAmountLabel.setText(String.format("Final Amount: €%.2f", points.get(points.size() - 1).getAmount()));
        }
    }

    private String normalizeColor(String rawColor) {
        if (rawColor == null || rawColor.isBlank()) {
            return "black";
        }
        return rawColor.trim();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveStrategies() {
        try {
            Files.createDirectories(STORAGE_PATH.getParent());
            try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(STORAGE_PATH.toFile())))) {
                encoder.writeObject(new ArrayList<>(strategyListView.getStrategies()));
            }
        } catch (IOException e) {
            System.err.println("Failed to save strategies: " + e.getMessage());
        }
    }

    private void loadStrategies() {
        if (!Files.exists(STORAGE_PATH)) return;
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(STORAGE_PATH.toFile())))) {
            Object result = decoder.readObject();
            if (result instanceof List) {
                @SuppressWarnings("unchecked")
                List<InvestmentStrategy> loaded = (List<InvestmentStrategy>) result;
                if (!loaded.isEmpty()) {
                    strategyListView.getStrategies().setAll(loaded);
                    strategyListView.getSelectionModel().selectFirst();
                    updateChart();
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load strategies: " + e.getMessage());
        }
    }
}
