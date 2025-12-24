package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

public class FileHandler {

    // Alert for error messages
    Alert alert = new Alert(Alert.AlertType.ERROR);

    // Alert for information / success messages
    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);

    // File chooser for loading and saving files
    FileChooser fileChooser = new FileChooser();

    // Files used for loading and saving
    File fileChoosen, ff;

    // Static list of tasks shared across the application
    static MyArrayList<Task> tasks = new MyArrayList<>();

    // Number of tasks read from file
    private int numOfTasks;

    // Total available hours
    private float numOfHours;

    // Returns number of tasks
    public int getNumOfTasks() {
        return numOfTasks;
    }

    // Returns available hours
    public float getNumOfHours() {
        return numOfHours;
    }

    // Sets available hours
    public void setNumOfHours(float numOfHours) {
        this.numOfHours = numOfHours;
    }

    // Returns the list of tasks
    public MyArrayList<Task> getTasks() {
        return tasks;
    }

    // Opens file chooser to select a file for loading
    public void loadButton() {

        fileChooser.setTitle("Choose a file to load");
        fileChooser.setInitialDirectory(new File("D:\\Coding\\Java\\FxDpProject"));
        fileChoosen = fileChooser.showOpenDialog(null);

        // If no file is selected, show error
        if (fileChoosen == null) {
            alert.setTitle("Error");
            alert.setHeaderText("No file selected");
            alert.setContentText("Please select a file.");
            alert.showAndWait();
            return;
        }
    }

    // Reads data from the selected file
    public void loadFromFile() {

        // If no file was chosen, do nothing
        if (fileChoosen == null)
            return;

        try (Scanner in = new Scanner(fileChoosen)) {

            // Clear previous data
            tasks.clear();
            Design.obs.clear();

            // Read number of tasks and total hours
            numOfTasks = in.nextInt();
            numOfHours = in.nextFloat();
            in.nextLine();

            // Counter to ensure we read only the expected number of tasks
            int indexOfArrays = 0;

            // Read tasks line by line
            while (in.hasNextLine() && indexOfArrays < numOfTasks) {

                String line = in.nextLine().trim();

                // Skip empty lines
                if (line.isEmpty())
                    continue;

                // Split line by comma
                String[] lineData = line.split(",");

                // Each task must have exactly 3 values
                if (lineData.length != 3)
                    continue;

                try {

                    // Read task data
                    String titleOfTasks = lineData[0];
                    float requiredTimeF = Float.parseFloat(lineData[1]);
                    int requiredTime = (int) (requiredTimeF * 2);
                    int getProductivityTime = Integer.parseInt(lineData[2]);

                    // Validate task title (must contain letters)
                    if (!titleOfTasks.matches("^(?=.*[A-Za-z])[A-Za-z0-9 ]+$"))
                        continue;

                    // Validate values (no negatives)
                    if (requiredTime < 0 || getProductivityTime < 0)
                        continue;

                    // Create task object
                    Task t = new Task(titleOfTasks, requiredTime, getProductivityTime);

                    // Add task to data structures
                    tasks.add(t);
                    Design.obs.add(t);
                    indexOfArrays++;

                } catch (NumberFormatException e) {
                    continue;
                }
            }

            // Show success message
            alert1.setTitle("Confirmation");
            alert1.setContentText("File Readed Succefully");
            alert1.showAndWait();

        } catch (IOException e) {

            // Error while reading file
            alert.setTitle("Error");
            alert.setContentText("Can't read Size of Number Of Tasks or Number of Hours");
            alert.showAndWait();
        }
    }

    // Opens file chooser to select a file for saving
    public void saveButton() {

        FileChooser Chooser = new FileChooser();
        Chooser.setTitle("Choose a file to Save");
        Chooser.setInitialDirectory(new File("D:\\Coding\\Java\\FxDpProject"));
        ff = Chooser.showSaveDialog(null);

        // If no file is selected, show error
        if (ff == null) {
            alert.setTitle("Error");
            alert.setHeaderText("No file selected");
            alert.setContentText("Please select a file.");
            alert.showAndWait();
            return;
        }
    }

    // Writes current tasks to file
    public void saveToFile() {

        try (PrintWriter writer = new PrintWriter(new FileWriter(ff))) {

            // Write number of tasks and available hours
            writer.print(tasks.getSize() + " ");
            writer.println(getNumOfHours());

            // Write each task
            for (int i = 0; i < tasks.getSize(); i++) {
                writer.println(buildData(tasks.get(i)));
            }

            // Show success message
            alert1.setTitle("Success");
            alert1.setHeaderText("File Saved");
            alert1.setContentText("File Saved Successfully!");
            alert1.showAndWait();

        } catch (IOException e) {

            // Error while saving file
            alert.setTitle("Error");
            alert.setHeaderText("Saving Failed");
            alert.setContentText("Failed to save data.");
            alert.showAndWait();
        }
    }

    // Converts task object to file format
    private String buildData(Task task) {

        // Convert required time back to hours
        float requiredTimeF = task.getRequiredTime() / 2.0f;

        return task.getTaskTitle() + "," + requiredTimeF + "," + task.getProductivityValue();
    }
}
