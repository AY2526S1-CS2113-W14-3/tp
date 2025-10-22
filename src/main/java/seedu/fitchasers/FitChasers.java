package seedu.fitchasers;

//import java.io.IOException;
import seedu.fitchasers.exceptions.FileNonexistent;
import seedu.fitchasers.exceptions.InvalidCommandException;

import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Scanner;

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
    public static void main(String[] args) throws IOException {
        UI ui = new UI();
        WorkoutManager workoutManager = new WorkoutManager();
        FileHandler fileHandler = new FileHandler();
        UserManager userManager = new UserManager(); // Added UserManager
        WeightManager weightManager = new WeightManager(userManager.getCurrentUser());
        Scanner scanner = new Scanner(System.in);
        YearMonth currentMonth = YearMonth.now();
        ViewLog viewLog;

        // Attempt to load persistent data by month for the default user
        // #TODO add select month #TODO need to add separate month to current month check!
        try {
            workoutManager.setWorkouts(fileHandler.loadMonthList(userManager.getCurrentUser().getName(), currentMonth));
            ui.showMessage("Loaded " + currentMonth + " workouts for " + userManager.getCurrentUser().getName());
        } catch (FileNonexistent e) {
            ui.showError("Seems like this is a new month for " + userManager.getCurrentUser().getName() +
                    "!\n Would you like to create new workouts for this month? (Y/N)");
            if (ui.confirmationMessage()) {
                fileHandler.saveMonthList(userManager.getCurrentUser().getName(), currentMonth, new ArrayList<>());
                workoutManager.setWorkouts(new ArrayList<>());
            }
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        viewLog = new ViewLog(ui, workoutManager);
        ui.showGreeting();

        boolean isRunning = true;

        while (isRunning) {
            String input = ui.readCommand();
            if (input == null) {
                break;
            }
            if (input.trim().isEmpty()) {
                continue;
            }

            String[] parts = input.trim().split("\\s+", 2);
            String command = parts[0].toLowerCase();
            String argumentStr = (parts.length > 1) ? parts[1].trim() : "";

            try {
                switch (command) {
                case "/help":
                    ui.showHelp();
                    break;

                case "/my_name": {
                    if (argumentStr == null || !argumentStr.startsWith("n/")) {
                        ui.showMessage("Usage: /my_name n/YourName");
                        ui.showDivider();
                        break;
                    }
                    String newName = argumentStr.substring(2).trim();
                    if (newName.isEmpty()) {
                        ui.showMessage("Usage: /my_name n/YourName");
                        ui.showDivider();
                        break;
                    }
                    userManager.setCurrentUser(newName);
                    weightManager = new WeightManager(userManager.getCurrentUser());
                    try {
                        workoutManager.setWorkouts(fileHandler.loadMonthList(newName, currentMonth));
                        ui.showMessage("Switched to user: " + newName);
                    } catch (FileNonexistent e) {
                        ui.showMessage("New user! Create new data for " + newName + "? (Y/N)");
                        if (ui.confirmationMessage()) {
                            fileHandler.saveMonthList(newName, currentMonth, new ArrayList<>());
                            workoutManager.setWorkouts(new ArrayList<>());
                        }
                    } catch (IOException e) {
                        ui.showError("Failed to load data for " + newName + ": " + e.getMessage());
                    }
                    ui.showDivider();
                    break;
                }

                case "/add_weight":
                    ui.showMessage("Logging your weight... don't lie to me!");
                    weightManager.addWeight(argumentStr);
                    // Format: /add_weight w/WEIGHT d/DATE
                    ui.showDivider();
                    break;

                case "/view_weight":
                    ui.showMessage("Here's your weight, you've been killin' it lately!");
                    weightManager.viewWeights();
                    ui.showDivider();
                    break;

                case "/create_workout":
                    ui.showMessage("New workout sesh incoming!");
                    // Format: /create_workout n/NAME d/DD/MM/YY t/HHmm
                    workoutManager.addWorkout(argumentStr, userManager.getCurrentUser().getName());
                    ui.showDivider();
                    break;

                case "/add_exercise":
                    ui.showMessage("Adding that spicy new exercise!");
                    // Format: /add_exercise n/NAME r/REPS
                    workoutManager.addExercise(argumentStr);
                    ui.showDivider();
                    break;

                case "/add_set":
                    ui.showMessage("Adding a new set to your exercise!");
                    // Format: /add_set r/REPS
                    workoutManager.addSet(argumentStr);
                    ui.showDivider();
                    break;

                case "/end_workout":
                    ui.showMessage("Workout wrapped! Time to refuel!");
                    // Format: /end_workout d/DD/MM/YY t/HHmm
                    workoutManager.endWorkout(scanner, argumentStr);
                    ui.showDivider();
                    break;

                case "vl":
                case "/view_log":
                    try {
                        viewLog.render(argumentStr); //#TODO select detailed or not
                    } catch (IndexOutOfBoundsException e) {
                        ui.showError(e.getMessage());
                    }
                    ui.showDivider();
                    break;

                case "/open":
                    viewLog.openByIndex(Integer.parseInt(argumentStr));
                    break;

                case "/del_workout":
                    // Format: /del_workout WORKOUT_NAME
                    if (argumentStr.isEmpty()) {
                        throw new InvalidCommandException("Workout deletion command requires a workout name or date. " +
                                "Please enter a valid command.");
                    } else if (argumentStr.contains("d/")) {
                        workoutManager.interactiveDeleteWorkout(argumentStr, scanner);
                    } else {
                        workoutManager.deleteWorkout(argumentStr);
                    }
                    break;

                case "/exit":
                    ui.showMessage("Saving your progress...");
                    try {
                        fileHandler.saveMonthList(userManager.getCurrentUser().getName(), currentMonth, workoutManager.getWorkouts());
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