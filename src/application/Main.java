package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    // Entry point for JavaFX application
    @Override
    public void start(Stage stage) {

        // Handles reading input data (tasks, hours, etc.)
        FileHandler fileHandler = new FileHandler();

        // Creates the GUI and passes the data handler to it
        Design design = new Design(fileHandler);

        // Starts the main interface
        design.Interface1(stage);
    }

    // Standard Java main method
    public static void main(String[] args) {

        // Launches the JavaFX application
        launch();
    }
}
