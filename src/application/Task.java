package application;

public class Task {

    // Name or title of the task
    private String taskTitle;

    // Time required to complete the task (in hours or units)
    private int requiredTime;

    // Productivity value gained from completing this task
    private int productivityValue;

    // Constructor to initialize task data
    public Task(String taskTitle, int requiredTime, int productivityValue) {
        this.taskTitle = taskTitle;
        this.requiredTime = requiredTime;
        this.productivityValue = productivityValue;
    }

    // Returns the task title
    public String getTaskTitle() {
        return taskTitle;
    }

    // Returns the time required for the task
    public int getRequiredTime() {
        return requiredTime;
    }

    // Returns the productivity value of the task
    public int getProductivityValue() {
        return productivityValue;
    }
}
