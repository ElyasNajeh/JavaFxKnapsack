package application;

import javafx.scene.control.Alert;

public class AddTask {

    // Alert for error messages
    Alert alert = new Alert(Alert.AlertType.ERROR);

    // Alert for success messages
    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);

    // Reference to the Design class (GUI elements)
    Design design;

    // Constructor receives the GUI reference
    public AddTask(Design design) {
        this.design = design;
    }

    // First validation: checks text input format
    public boolean validations1(String title, String time, String productivity) {

        // Check if title is empty
        if (title == null || title.trim().isEmpty()) {
            alert.setTitle("Error");
            alert.setContentText("Task title Can't be Empty");
            alert.showAndWait();
            return false;
        }

        // Title must contain letters (numbers allowed)
        if (!title.matches("^(?=.*[A-Za-z])[A-Za-z0-9 ]+$")) {
            alert.setTitle("Error");
            alert.setContentText("Task title must contain letters and (numbers optional)");
            alert.showAndWait();
            return false;
        }

        // Check if time field is empty
        if (time == null || time.isEmpty()) {
            alert.setTitle("Error");
            alert.setContentText("Task Time Can't be Empty");
            alert.showAndWait();
            return false;
        }

        // Time must be integer or integer.5
        if (!time.matches("^[0-9]+(\\.5)?$")) {
            alert.setTitle("Error");
            alert.setContentText("Task Time must contain only Numbers or Numbers and Half");
            alert.showAndWait();
            return false;
        }

        // Check if productivity field is empty
        if (productivity == null || productivity.isEmpty()) {
            alert.setTitle("Error");
            alert.setContentText("Task Productivity Can't be Empty");
            alert.showAndWait();
            return false;
        }

        // Productivity must be digits only
        if (!productivity.matches("^[0-9]+$")) {
            alert.setTitle("Error");
            alert.setContentText("Task Productivity must contain only Numbers");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    // Second validation: checks logical values
    public boolean validations2(Float time, int productivity) {

        // Time must be greater than zero
        if (time <= 0) {
            alert.setTitle("Error");
            alert.setContentText("Task time Can't Be Zero or Less");
            alert.showAndWait();
            return false;
        }

        // Productivity must be greater than zero
        if (productivity <= 0) {
            alert.setTitle("Error");
            alert.setContentText("Task Productivity Can't Be Zero or Less");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    // Checks that task title is unique
    public boolean uniqueTitle(String title) {

        // Compare title with existing tasks
        for (int i = 0; i < FileHandler.tasks.getSize(); i++) {
            if (title.equalsIgnoreCase(FileHandler.tasks.get(i).getTaskTitle())) {
                alert.setTitle("Error");
                alert.setContentText("Task Title Must be Unique");
                alert.showAndWait();
                return false;
            }
        }

        return true;
    }

    // Adds a new task to the system
    public void add() {

        // Read input values from GUI fields
        String title = design.title2.getText().trim();
        String time = design.time2.getText().trim();
        String productivity = design.productivity2.getText().trim();

        // First level validation (format check)
        boolean val1 = validations1(title, time, productivity);
        if (!val1)
            return;

        // Convert input values
        float taskTimeF = Float.parseFloat(time);
        int requiredTime = (int) (taskTimeF * 2);
        int taskProductivity = Integer.parseInt(productivity);

        // Second level validation (logical check)
        boolean val2 = validations2(taskTimeF, taskProductivity);
        if (!val2)
            return;

        // Check if title already exists
        boolean val3 = uniqueTitle(title);
        if (!val3)
            return;

        // Create new task object
        Task task = new Task(title, requiredTime, taskProductivity);

        // Add task to table and main task list
        design.obs.add(task);
        FileHandler.tasks.add(task);
        design.table.refresh();

        // Show success message
        alert1.setTitle("Success");
        alert1.setContentText("Task Added Succesfully");
        alert1.show();

        // Clear input fields
        design.title2.clear();
        design.time2.clear();
        design.productivity2.clear();
    }
}
