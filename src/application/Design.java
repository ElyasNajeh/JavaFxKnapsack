package application;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class Design {

    // TableView to display tasks
    TableView<Task> table;

    // Main layout container
    BorderPane mainDesign = new BorderPane();

    // TextFields for user input
    TextField title2, time2, productivity2, change;

    // Alerts for success and error messages
    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
    Alert alert2 = new Alert(Alert.AlertType.ERROR);

    // Labels for displaying stats
    Label numOfTasks, numOfHours, l1;

    // Buttons used in the UI
    Button loadButton, saveButton, addButton, calculateButton, advaButton,
            addTas, cancelTas, changeAvilableHours, changeH, cancelCh;

    // Output strings for solutions
    String greedyOutput, dpOutput;

    // Flag to ensure DP header is added once
    boolean headerAdded = false;

    // Solution objects
    DpSolution dp;
    GreedySolution gd;

    // File handler reference
    FileHandler fileHandler;

    // Observable list for TableView (auto-updates UI)
    static ObservableList<Task> obs = FXCollections.observableArrayList();

    // GridPane to display DP table
    static GridPane dpGrid = new GridPane();

    // Constructor receives FileHandler
    public Design(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    // Main application interface
    public void Interface1(Stage stage) {

        VBox topBox1 = new VBox(25);
        HBox topBox2 = new HBox(30);
        HBox bottom = new HBox(30);

        Label welcomLabel = new Label("Intelligent Daily Task Scheduling System");

        // Top buttons
        addButton = new Button("Add Task");
        loadButton = new Button("Load From File");
        saveButton = new Button("Save to File");
        calculateButton = new Button("Calculate Daily Task");
        advaButton = new Button("Avantage of Dynamic Programming");

        topBox2.getChildren().addAll(addButton, loadButton, saveButton, calculateButton, advaButton);
        topBox2.setAlignment(Pos.CENTER);
        topBox1.getChildren().addAll(welcomLabel, topBox2);
        topBox1.setAlignment(Pos.CENTER);

        // Create tasks table
        table = tasksTableView();

        // Bottom info labels
        numOfTasks = new Label("Number of Tasks is : " + FileHandler.tasks.getSize());
        numOfHours = new Label("Number of Avilable Hours is : " + fileHandler.getNumOfHours());
        changeAvilableHours = new Button("Change Avilable Hours");

        bottom.getChildren().addAll(numOfTasks, numOfHours, changeAvilableHours);
        bottom.setAlignment(Pos.CENTER);

        // Attach main actions
        actions1();

        // Build layout
        mainDesign.setTop(topBox1);
        mainDesign.setCenter(table);
        mainDesign.setBottom(bottom);

        Scene scene = new Scene(mainDesign, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Daily Task Scheduling System");
        stage.setMaximized(true);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.show();
    }

    // Builds the TableView for tasks
    private TableView<Task> tasksTableView() {

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Task title column
        TableColumn<Task, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getTaskTitle()));

        // Required time column (converted from internal half-hour units)
        TableColumn<Task, Float> requiredCol = new TableColumn<>("Required Time");
        requiredCol.setCellValueFactory(
                cell -> new SimpleFloatProperty(
                        (float) cell.getValue().getRequiredTime() / 2f).asObject());

        // Productivity column
        TableColumn<Task, Integer> prodCol = new TableColumn<>("Productivity");
        prodCol.setCellValueFactory(
                cell -> new SimpleIntegerProperty(
                        cell.getValue().getProductivityValue()).asObject());

        table.getColumns().addAll(titleCol, requiredCol, prodCol);
        table.setItems(obs);

        return table;
    }

    // Add task popup window
    public void addInterface() {

        VBox main = new VBox(20);
        GridPane inptus = new GridPane();
        HBox buttons = new HBox(20);

        inptus.setVgap(15);
        inptus.setHgap(15);
        inptus.setAlignment(Pos.CENTER);

        Label title1 = new Label("Title Task : ");
        Label time1 = new Label("Time Required");
        Label productivity1 = new Label("Productivity");

        title2 = new TextField();
        time2 = new TextField();
        productivity2 = new TextField();

        inptus.add(title1, 0, 0);
        inptus.add(title2, 1, 0);
        inptus.add(time1, 0, 1);
        inptus.add(time2, 1, 1);
        inptus.add(productivity1, 0, 2);
        inptus.add(productivity2, 1, 2);

        addTas = new Button("Add");
        cancelTas = new Button("Cancel");

        buttons.getChildren().addAll(cancelTas, addTas);
        buttons.setAlignment(Pos.CENTER);

        main.getChildren().addAll(inptus, buttons);
        main.setAlignment(Pos.CENTER);

        // Attach add-task actions
        actions2();

        Stage stage = new Stage();
        Scene scene = new Scene(main, 350, 250);
        stage.setScene(scene);
        stage.setTitle("Add Task");
        stage.setResizable(false);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.show();
    }

    // Change available hours popup
    public void changeHoursInterFace() {

        VBox main = new VBox(20);
        GridPane inputs = new GridPane();
        HBox buttons = new HBox(20);

        inputs.setVgap(15);
        inputs.setHgap(15);
        inputs.setAlignment(Pos.CENTER);

        l1 = new Label("Number of Avilable Hours is : " + fileHandler.getNumOfHours());
        Label l2 = new Label("New Number of Avilable Hours is : ");

        change = new TextField();
        change.setPromptText("Enter new hours");
        change.setMaxWidth(150);

        inputs.add(l1, 0, 0);
        inputs.add(l2, 0, 1);
        inputs.add(change, 1, 1);

        changeH = new Button("Change");
        cancelCh = new Button("Cancel");

        buttons.getChildren().addAll(cancelCh, changeH);
        buttons.setAlignment(Pos.CENTER);

        main.getChildren().addAll(inputs, buttons);
        main.setAlignment(Pos.CENTER);

        // Attach change-hours actions
        actions3();

        Stage stage = new Stage();
        Scene scene = new Scene(main, 450, 250);
        stage.setScene(scene);
        stage.setTitle("Change Avilable Hours");
        stage.setResizable(false);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.show();
    }

    // Results interface (DP table + comparison)
    public void Interface2() {

        ScrollPane dpScroll = new ScrollPane(dpGrid);
        dpScroll.setFitToWidth(true);
        dpScroll.setPannable(true);
        dpScroll.setStyle("-fx-background-color:transparent;");

        dpGrid.setAlignment(Pos.CENTER);

        VBox mmain = new VBox();
        Button back = new Button("Back");
        back.setOnAction(X -> {
            ((Stage) back.getScene().getWindow()).close();
        });

        back.getStyleClass().add("back-btn");

        HBox main = Comparison();
        mmain.getChildren().addAll(main, back);
        mmain.setAlignment(Pos.CENTER);

        BorderPane secondDesign = new BorderPane();
        secondDesign.setCenter(dpScroll);
        secondDesign.setBottom(mmain);

        Stage stage = new Stage();
        Scene scene = new Scene(secondDesign, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Daily Task Scheduling System");
        stage.setMaximized(true);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.show();
    }

    // Draws one DP row in the grid
    public void dpSol(int[] dp, int row) {

        int cols = dp.length;

        // Add header only once
        if (!headerAdded) {
            for (int j = 0; j < cols; j++) {
                float realTime = j / 2.0f;
                Label h = new Label(realTime + "");
                h.getStyleClass().add("dp-cell");
                dpGrid.add(h, j, 0);
            }
            headerAdded = true;
        }

        int rows = dp.length;

        // Add DP values for current row
        for (int i = 0; i < rows; i++) {
            Label cell = new Label(dp[i] + "");
            cell.getStyleClass().add("dp-cell");
            dpGrid.add(cell, i, row);
        }
    }

    // Comparison view between Greedy and DP
    private HBox Comparison() {

        HBox main = new HBox(70);
        main.setPadding(new Insets(20, 0, 40, 0));

        VBox greedy = new VBox(10);
        VBox Dp = new VBox(10);

        Label greedyLabel = new Label("Greedy Solution");
        TextArea greedyField = new TextArea();
        greedyField.setEditable(false);
        greedyField.setWrapText(true);
        greedyField.setText(greedyOutput);

        greedy.getChildren().addAll(greedyLabel, greedyField);
        greedy.setAlignment(Pos.CENTER);

        Label dpLabel = new Label("Dynamic Programming Solution");
        TextArea dpField = new TextArea();
        dpField.setEditable(false);
        dpField.setWrapText(true);
        dpField.setText(dpOutput);

        Dp.getChildren().addAll(dpLabel, dpField);
        Dp.setAlignment(Pos.CENTER);

        main.getChildren().addAll(greedy, Dp);
        main.setAlignment(Pos.CENTER);

        return main;
    }

    // Explains why DP is better than Greedy
    private void Adva() {

        VBox Advantage = new VBox();
        Advantage.setPadding(new Insets(20, 0, 40, 0));

        Label label = new Label("Advantage of Dynamic Programming");
        Label relation = new Label(
                "Initial value: DP starts with the first task.\n"
                        + "If the time is enough, we take it, otherwise we take nothing.\n"
                        + "The Relation is   dp[j] = Math.max(dp[j], value + dp[j - requiredTime])");

        TextArea Explain = new TextArea();
        Explain.setEditable(false);
        Explain.setWrapText(true);

        Explain.setText(
                "What distinguishes Dynamic programming from Greedy ? \n"
                        + " is that it intelligently explores all possibilities,storing the optimal answers and building optimal solutions based on this stored Optimal answers.\n"
                        + "Therefore, there's no need to calculate anything again. It's like seeing everything from afar,ensuring that every path is evaluated. \n"
                        + "Unlike the greedy who only sees the local and currently available option. \n"
                        + "and takes in each step it if it best immediate choice, without knowing that if he takes this answer he may be closing himself off to better options.");

        Advantage.getChildren().addAll(relation, label, Explain);
        Advantage.setAlignment(Pos.CENTER);

        Stage stage = new Stage();
        Scene scene = new Scene(Advantage, 900, 450);
        stage.setScene(scene);
        stage.setTitle("Advantage of Dynamic Programming");
        stage.setResizable(false);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.show();
    }

    // Main button actions
    public void actions1() {

        loadButton.setOnAction(x -> {
            fileHandler.loadButton();
            fileHandler.loadFromFile();
            table.setItems(obs);
            numOfTasks.setText("Number of Tasks is : " + FileHandler.tasks.getSize());
            numOfHours.setText("Number of Avilable Hours is : " + fileHandler.getNumOfHours());
        });

        addButton.setOnAction(x -> addInterface());

        saveButton.setOnAction(x -> {
            fileHandler.saveButton();
            fileHandler.saveToFile();
        });

        calculateButton.setOnAction(x -> {
            dpGrid.getChildren().clear();
            dp = new DpSolution(fileHandler);
            dp.dpSolution();
            dpOutput = dp.outDpSolution();
            gd = new GreedySolution(fileHandler);
            greedyOutput = gd.greedySolution();
            Interface2();
        });

        advaButton.setOnAction(x -> Adva());

        changeAvilableHours.setOnAction(x -> changeHoursInterFace());
    }

    // Add-task actions
    public void actions2() {

        cancelTas.setOnAction(x -> {
            ((Stage) cancelTas.getScene().getWindow()).close();
        });

        addTas.setOnAction(x -> {
            AddTask addTask = new AddTask(this);
            addTask.add();
            numOfTasks.setText("Number of Tasks is : " + FileHandler.tasks.getSize());
            numOfHours.setText("Number of Avilable Hours is : " + fileHandler.getNumOfHours());
        });
    }

    // Change-hours actions
    public void actions3() {

        cancelCh.setOnAction(x -> {
            ((Stage) cancelCh.getScene().getWindow()).close();
        });

        changeH.setOnAction(x -> {

            String txt = change.getText();

            if (txt == null || txt.isEmpty()) {
                alert2.setTitle("Error");
                alert2.setContentText("Avilable Hours Can't Be Empty");
                alert2.showAndWait();
                return;
            }

            if (!txt.matches("^\\d+(\\.5)?$")) {
                alert2.setTitle("Error");
                alert2.setContentText("Please enter a number only or number and half");
                alert2.showAndWait();
                return;
            }

            float newAvilableHoursF = Float.parseFloat(txt);
            fileHandler.setNumOfHours(newAvilableHoursF);

            alert1.setTitle("Success");
            alert1.setContentText("Upgraded Avilable Hours Succesfully");
            alert1.show();

            numOfHours.setText("Number of Available Hours is : " + newAvilableHoursF);
            l1.setText("Number of Available Hours is : " + newAvilableHoursF);
        });
    }
}
