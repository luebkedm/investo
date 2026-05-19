package com.investo;

import javafx.application.Application;
import javafx.stage.Stage;
import com.investo.ui.InvestmentController;

public class InvestoApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        InvestmentController controller = new InvestmentController();
        controller.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
