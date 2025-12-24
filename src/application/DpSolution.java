package application;

public class DpSolution {

    long startTime;
    long endTime;
    long actualTime;

    // Output text for DP solution
    String output = "Tasks : \n";

    // Total time used by selected tasks
    float totalTime = 0;

    // Total productivity gained
    int totalProudtivity = 0;

    // Stores selected tasks after backtracking
    MyArrayList<Task> allInformation = new MyArrayList<>(FileHandler.tasks.getSize());

    // Reference to file handler (data source)
    FileHandler fileHandler;

    // Design reference used only to draw DP table
    Design design;

    // Bit-based tracking table to remember chosen tasks
    byte[][] track;

    // DP array: dp[j] = maximum productivity with j half-hour units
    static int[] dp;

    // Constructor receives FileHandler
    public DpSolution(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    // Builds the DP table using 0/1 Knapsack logic
    public void dpSolution() {

        // Available hours in float
        float numOfHours = fileHandler.getNumOfHours();

        // Convert hours to half-hour units (integer)
        int numOfHoursI = (int) (numOfHours * 2);

        // Number of tasks
        int numOfItems = fileHandler.getNumOfTasks();

        // Number of bytes needed to track bits for each DP state
        int requiredBit = (numOfHoursI + 8) / 8;

        // Initialize DP array
        dp = new int[numOfHoursI + 1];

        // Initialize tracking table (items x capacity bits)
        track = new byte[numOfItems + 1][requiredBit];

        // Design object to visualize DP table
        design = new Design(fileHandler);

        // Loop over tasks (1-based index for tracking)
        startTime = System.nanoTime();
        for (int i = 1; i <= FileHandler.tasks.getSize(); i++) {

            // Loop backwards over capacity (0/1 Knapsack)
            for (int j = numOfHoursI; j >= FileHandler.tasks.get(i - 1).getRequiredTime(); j--) {

                // Calculate byte index for bit tracking
                int NumByte = j / 8;

                // Calculate bit position inside the byte
                int NumBit = 7 - (j % 8);

                // Bit mask for this state
                int mask = 1 << NumBit;

                // Task required time (weight)
                int requiredTime = FileHandler.tasks.get(i - 1).getRequiredTime();

                // Task productivity value
                int value = FileHandler.tasks.get(i - 1).getProductivityValue();

                // If taking this task improves productivity
                if (value + dp[j - requiredTime] > dp[j]) {

                    // Update DP value
                    dp[j] = value + dp[j - requiredTime];

                    // Mark this task as taken at this capacity
                    track[i][NumByte] |= mask;
                }
            }

        }
        endTime = System.nanoTime();
        actualTime = endTime - startTime;
        // Draw DP row for visualization

        design.dpSol(dp, FileHandler.tasks.getSize());

    }

    // Backtracking to find which tasks were selected
    public void Information() {

        // Start from last task
        int i = fileHandler.getNumOfTasks();

        // Start from full capacity
        int j = (int) (fileHandler.getNumOfHours() * 2);

        // Trace back while tasks and capacity remain
        while (i > 0 && j > 0) {

            // Locate the byte storing this state
            int byteIndex = j / 8;

            // Locate the bit inside the byte
            int bitIndex = 7 - (j % 8);

            // If bit is set, task i was taken
            if ((track[i][byteIndex] & (1 << bitIndex)) != 0) {

                // Add selected task
                Task t = fileHandler.tasks.get(i - 1);
                allInformation.add(t);

                // Reduce remaining capacity
                j -= t.getRequiredTime();
            }

            // Move to previous task
            i--;
        }
    }

    // Builds the final DP solution output
    public String outDpSolution() {

        // Reset output and counters
        output = "Tasks : \n";
        totalTime = 0;
        totalProudtivity = 0;
        allInformation.clear();

        // Perform backtracking
        Information();

        // Print tasks in correct order
        for (int i = allInformation.getSize() - 1; i >= 0; i--) {

            // Convert time back to hours
            float requiredTimeF = allInformation.get(i).getRequiredTime() / 2.0f;

            int value = allInformation.get(i).getProductivityValue();
            String title = allInformation.get(i).getTaskTitle();

            output += "+ "
                    + title
                    + " (" + requiredTimeF + "h)\n";

            totalTime += requiredTimeF;
            totalProudtivity += value;
        }

        // Append summary
        output += "\nTotal Time: " + totalTime
                + " | Total Productivity: " + totalProudtivity + "| Actual Time :" + actualTime + " in nano seconds";

        return output;
    }
}
