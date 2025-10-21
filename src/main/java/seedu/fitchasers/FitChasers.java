package seedu.fitchasers;

import java.io.IOException;


/**
 * Main entry point for the FitChasers application.
 *
 * Handles user input commands, delegates operations to WorkoutManager,
 * and persists data through FileHandler.
 */
public class FitChasers {
    /**
     * Starts the FitChasers program.
     * Initializes all components, loads saved data if available,
     * and processes user input until the user exits.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        assert false : "dummy assertion set to fail";

        UI ui = new UI();
        WorkoutManager workoutManager = new WorkoutManager();
        FileHandler fileHandler = new FileHandler();
        Person person = new Person("Nary");
        WeightManager weightManager = new WeightManager(person);

        // Attempt to load persistent data
        try {
            fileHandler.loadFileContentArray(workoutManager);
        } catch (IOException e) {
            ui.showError("Could not load saved data. Starting fresh!");
        }

        ui.showGreeting();

        boolean isRunning = true;

        while (isRunning) {
            String input = ui.readCommand();
            if (input == null || input.trim().isEmpty()) {
                continue;
            }

            String[] parts = input.trim().split("\\s+", 2);
            String command = parts[0].toLowerCase();
            String argumentStr = (parts.length > 1) ? parts[1] : "";

            try {
                switch (command) {
                case "/help":
                    ui.showHelp();
                    break;

                case "/add_weight":
                    weightManager.addWeight(argumentStr);
                    // Format: /add_weight w/WEIGHT d/DATE
                    ui.showDivider();
                    break;

                case "/view_weight":
                    weightManager.viewWeights();
                    ui.showDivider();
                    break;

                case "/create_workout":
                    // Format: /create_workout n/NAME d/DD/MM/YY t/HHmm
                    workoutManager.addWorkout(argumentStr);
                    ui.showDivider();
                    break;

                case "/add_exercise":
                    // Format: /add_exercise n/NAME r/REPS
                    workoutManager.addExercise(argumentStr);
                    ui.showDivider();
                    break;

                case "/add_set":
                    // Format: /add_set r/REPS
                    workoutManager.addSet(argumentStr);
                    ui.showDivider();
                    break;

                case "/end_workout":
                    // Format: /end_workout d/DD/MM/YY t/HHmm
                    workoutManager.endWorkout(argumentStr);
                    ui.showDivider();
                    break;

                case "/view_log":
                    workoutManager.viewWorkouts();
                    ui.showDivider();
                    break;

                case "/del_workout":
                    // Format: /del_workout WORKOUT_NAME
                    workoutManager.deleteWorkout(argumentStr);
                    ui.showDivider();
                    break;

                case "/exit":
                    ui.showMessage("Saving your progress...");
                    try {
                        fileHandler.saveFile(workoutManager.getWorkouts());
                        ui.showExitMessage();
                    } catch (IOException e) {
                        ui.showError("Failed to save workouts before exit.");
                    }
                    isRunning = false;
                    break;

                default:
                    ui.showError("That's not a thing, bestie. Try /help for the real moves!");
                    ui.showDivider();
                    break;
                }
            } catch (Exception e) {
                ui.showError("Something went wrong: " + e.getMessage());
                ui.showDivider();
            }
        }
    }
}
