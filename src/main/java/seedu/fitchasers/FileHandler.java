package seedu.fitchasers;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the permanent storage of workout and exercise data.
 * Saves to and loads from: data/save.txt
 *
 * If the "data" folder does not exist, it will be created automatically.
 *
 * File format:
 * USER | Name
 * WORKOUT | Name | Duration
 * EXERCISE | Name | reps,reps,reps
 * END_WORKOUT
 */
public class FileHandler {

    private static final Path FILE_PATH = Paths.get("data", "save.txt");
    private final UI ui;
    private final Scanner scanner;


    /**
     * Constructs a FileHandler with a reference to the UI and Scanner for user feedback/input.
     *
     * @param ui Scanner for writing messages
     * @param scanner Scanner for reading interactive input
     */
    public FileHandler(UI ui, Scanner scanner) {
        this.ui = ui;
        this.scanner = scanner;
    }


    /**
     * Ensures that the save file and its parent directory exist.
     * If the file did not exist and was created, it will attempt to read the user's name
     * interactively and store a USER header. If the process is non-interactive (no console),
     * a default username is written so automated runs won't hang.
     *
     * @throws IOException if directory or file creation fails
     */
    private void ensureFile(Person person) throws IOException {
        Files.createDirectories(FILE_PATH.getParent());
        if (Files.notExists(FILE_PATH)) {
            Files.createFile(FILE_PATH); // Create empty save file

            String nameToWrite = "Nary"; // default
            boolean forcePrompt = Boolean.getBoolean("fitchaser.forcePrompt")
                    || "true".equalsIgnoreCase(System.getenv("FITCHASER_FORCE_PROMPT"));

            if (System.console() != null || forcePrompt) {
                ui.showMessage("It looks like this is your first time running FitChasers.");
                ui.showMessage("Please enter your name:");
                try {
                    String input = scanner.nextLine().trim();
                    if (!input.isEmpty()) {
                        nameToWrite = input;
                    }
                } catch (Exception e) {
                    // fallback to default
                }
            } else {
                ui.showMessage("No existing save file found. Using default user name: " + nameToWrite);
            }

            // Write the USER header into the newly created file
            try (FileWriter fw = new FileWriter(FILE_PATH.toFile(), true)) {
                fw.write("USER | " + nameToWrite + "\n");
            }

            // set the person name in memory as well
            if (person != null) {
                person.setName(nameToWrite);
            }
        }
    }

    /**
     * Loads all workout and exercise data from save.txt into the given WorkoutManager.
     *
     * Expected format:
     * USER | Name
     * WORKOUT | Name | Duration
     * EXERCISE | Name | reps,reps,reps
     * END_WORKOUT
     *
     * @param workoutManager the WorkoutManager to populate
     * @param person the Person whose name may be read from file and set
     * @throws IOException if reading the save file fails
     */
    public void loadFileContentArray(WorkoutManager workoutManager, Person person) throws IOException {
        ensureFile(person);
        List<String> lines = Files.readAllLines(FILE_PATH);

        Workout currentWorkout = null;

        for (String line : lines) {
            if (line.startsWith("USER")) {
                try {
                    String[] parts = line.split("\\|");
                    String name = parts[1].trim();
                    if (person != null && !name.isEmpty()) {
                        person.setName(name);
                    }
                } catch (Exception e) {
                    ui.showMessage("Skipping malformed USER entry: " + line);
                }
            } else if (line.startsWith("WORKOUT")) {
                try {
                    String[] parts = line.split("\\|");
                    String name = parts[1].trim();
                    int duration = Integer.parseInt(parts[2].trim());
                    currentWorkout = new Workout(name, duration);
                    workoutManager.getWorkouts().add(currentWorkout);
                } catch (Exception e) {
                    ui.showMessage("Skipping malformed workout entry: " + line);
                }


            } else if (line.startsWith("EXERCISE") && currentWorkout != null) {
                try {
                    String[] parts = line.split("\\|");
                    String exName = parts[1].trim();
                    String[] repsList = parts[2].trim().split(",");


                    Exercise exercise = new Exercise(exName, Integer.parseInt(repsList[0]));
                    for (int i = 1; i < repsList.length; i++) {
                        exercise.addSet(Integer.parseInt(repsList[i]));
                    }
                    currentWorkout.addExercise(exercise);
                } catch (Exception e) {
                    ui.showMessage("Skipping malformed exercise entry: " + line);
                }


            } else if (line.startsWith("END_WORKOUT")) {
                currentWorkout = null;
            }
        }
        ui.showMessage("Loaded " + workoutManager.getWorkouts().size() + " workout(s) from file.");
    }


    /**
     * Saves all workout data to save.txt in the specified format.
     *
     * Each save overwrites the entire file. The USER header is written first.
     *
     * @param workouts list of workouts to be saved
     * @param person the person whose name will be saved as USER header
     * @throws IOException if writing fails
     */
    public void saveFile(List<Workout> workouts, Person person) throws IOException {
        ensureFile(person);
        try (FileWriter fw = new FileWriter(FILE_PATH.toFile())) {
            // write USER header
            String userName = (person != null && person.getName() != null) ? person.getName() : "Nary";
            fw.write("USER | " + userName + "\n");
            for (Workout w : workouts) {
                fw.write("WORKOUT | " + w.getWorkoutName() + " | " + w.getDuration() + "\n");
                for (Exercise ex : w.getExercises()) {
                    // join all reps from each set with commas
                    StringBuilder setsStr = new StringBuilder();
                    for (int i = 0; i < ex.getSets().size(); i++) {
                        setsStr.append(ex.getSets().get(i));
                        if (i < ex.getSets().size() - 1) {
                            setsStr.append(",");
                        }
                    }
                    fw.write("EXERCISE | " + ex.getName() + " | " + setsStr + "\n");
                }
                fw.write("END_WORKOUT\n");
            }
        }
        ui.showMessage("Successfully saved " + workouts.size() + " workout(s) to file.");
    }
}
