package application;

public class GreedySolution {

    // Reference to FileHandler to access tasks and available hours
    FileHandler fileHandler;

    // Stores the result text to be displayed
    String output = "Tasks :\n";

    // Total time used by selected tasks
    float totalTime = 0;

    // Total productivity gained
    int totalProudtivity = 0;

    // Constructor receives the data handler
    public GreedySolution(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    // Applies a greedy approach to select tasks
    public String greedySolution() {

        // Reset output and counters before calculation
        output = "Tasks : \n";
        totalTime = 0;
        totalProudtivity = 0;

        // Total available hours
        float numOfHours = fileHandler.getNumOfHours();

        long startTime = System.nanoTime();
        // Iterate through all tasks
        for (int i = 0; i < FileHandler.tasks.getSize(); i++) {

            // Convert required time to hours (each unit = 0.5 hour)
            float requiredTimeF = FileHandler.tasks.get(i).getRequiredTime() / 2.0f;

            // Productivity value of the task
            int value = fileHandler.tasks.get(i).getProductivityValue();

            // Task title
            String title = fileHandler.tasks.get(i).getTaskTitle();

            // Select task if it fits in remaining time
            if (requiredTimeF <= numOfHours) {

                // Add task to output
                output += "+ "
                        + title
                        + " (" + requiredTimeF + "h)\n";

                // Update total time and productivity
                totalTime += requiredTimeF;
                totalProudtivity += value;

                // Reduce remaining hours
                numOfHours -= requiredTimeF;
            }
        }

        long endTime = System.nanoTime();
        long actualTime = endTime - startTime;
        // Append final summary
        output += "\nTotal Time: " + totalTime
                + " | Total Productivity: " + totalProudtivity + "| Actual Time :" + actualTime + " in nano seconds";

        return output;
    }
}
