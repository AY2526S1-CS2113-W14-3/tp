package seedu.fitchasers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class WorkoutManagerTest {
    private WorkoutManager workoutManager;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUp() {
        workoutManager = new WorkoutManager();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(outContent)); // Capture stderr for debugging
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(System.in); // Reset System.in
        outContent.reset();
    }

    @Test
    public void testAddWorkout_validInput_success() {
        String command = "/create_workout n/Morning Run d/22/10/25 t/0900";
        String username = "testUser";
        workoutManager.addWorkout(command, username);

        ArrayList<Workout> workouts = workoutManager.getWorkouts();
        assertEquals(1, workouts.size(), "Workout list should contain one workout");
        Workout workout = workouts.get(0);
        assertEquals("Morning Run", workout.getWorkoutName(), "Workout name should match");
        assertEquals(username, workout.getUsername(), "Username should match");
        assertEquals(LocalDateTime.parse("22/10/25 0900",
                        DateTimeFormatter.ofPattern("dd/MM/yy HHmm")),
                workout.getWorkoutStartDateTime(), "Start date-time should match");
        assertTrue(outContent.toString().contains("New workout sesh incoming!"),
                "Success message should be printed");
    }

    @Test
    public void testAddWorkout_missingDateTime_usesCurrent() {
        String command = "/create_workout n/Evening Yoga";
        String username = "testUser";
        workoutManager.addWorkout(command, username);

        ArrayList<Workout> workouts = workoutManager.getWorkouts();
        assertEquals(1, workouts.size(), "Workout list should contain one workout");
        assertTrue(outContent.toString().contains("No date input detected"),
                "Default date message should be printed");
        assertTrue(outContent.toString().contains("No time input detected"),
                "Default time message should be printed");
    }

    @Test
    public void testAddWorkout_invalidDateTime_formatError() {
        String command = "/create_workout n/Morning Run d/invalid t/0900";
        String username = "testUser";
        workoutManager.addWorkout(command, username);

        assertEquals(0, workoutManager.getWorkoutSize(), "No workout should be added");
        assertTrue(outContent.toString().contains("Invalid date/time format"),
                "Error message should be printed");
    }

    @Test
    public void testAddExercise_validInput_success() {
        workoutManager.addWorkout("/create_workout n/Test Workout", "testUser");
        workoutManager.addExercise("/add_exercise n/Push Up r/10");

        Workout currentWorkout = workoutManager.getWorkouts().get(0);
        ArrayList<Exercise> exercises = currentWorkout.getExercises();
        assertEquals(1, exercises.size(), "One exercise should be added");
        assertEquals("Push Up", exercises.get(0).getName(), "Exercise name should match");
        assertEquals(10, exercises.get(0).getSets().get(0), "Reps should match");
        assertTrue(outContent.toString().contains("Adding that spicy new exercise!"),
                "Success message should be printed");
    }

    @Test
    public void testAddExercise_noActiveWorkout_error() {
        workoutManager.addExercise("/add_exercise n/Push Up r/10");
        assertTrue(outContent.toString().contains("No active workout"),
                "Error message should be printed");
    }

    @Test
    public void testAddSet_validInput_success() {
        workoutManager.addWorkout("/create_workout n/Test Workout", "testUser");
        workoutManager.addExercise("/add_exercise n/Squat r/12");
        workoutManager.addSet("/add_set r/15");

        Workout currentWorkout = workoutManager.getWorkouts().get(0);
        Exercise exercise = currentWorkout.getExercises().get(0);
        assertEquals(2, exercise.getSets().size(), "Two sets should be added");
        assertEquals(15, exercise.getSets().get(1), "Second set reps should match");
        assertTrue(outContent.toString().contains("Adding a new set to your exercise!"),
                "Success message should be printed");
    }

    @Test
    public void testAddSet_noExercise_error() {
        workoutManager.addWorkout("/create_workout n/Test Workout", "testUser");
        workoutManager.addSet("/add_set r/15");
        assertTrue(outContent.toString().contains("No exercise found"),
                "Error message should be printed");
    }

    @Test
    public void testEndWorkout_validInput_success() {
        workoutManager.addWorkout("/create_workout n/Test Workout d/22/10/25 t/0900",
                "testUser");
        String input = "/end_workout d/22/10/25 t/1000";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        workoutManager.endWorkout(scanner, input);

        Workout workout = workoutManager.getWorkouts().get(0);
        assertEquals(60, workout.getDuration(), "Duration should be 60 minutes");
        assertTrue(outContent.toString().contains("Workout wrapped!"),
                "Success message should be printed");
    }

    @Test
    public void testEndWorkout_invalidTime_promptRetry() {
        workoutManager.addWorkout("/create_workout n/Test Workout d/22/10/25 t/0900",
                "testUser");
        String input = "/end_workout d/22/10/25 t/0800\n/end_workout d/22/10/25 t/1000";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        workoutManager.endWorkout(scanner, "/end_workout d/22/10/25 t/0800");

        assertTrue(outContent.toString().contains("End time must be after the start time"),
                "Error message should be printed");
        assertTrue(outContent.toString().contains("Workout wrapped!"),
                "Success message should be printed after retry");
    }

    //    @Test
    //    public void testDeleteWorkoutByIndex_validIndex_yInput_success() {
    //        workoutManager.addWorkout("/create_workout n/Test Workout", "testUser");
    //        String input = "y\n"; // Match interactive input that worked
    //        System.setIn(new ByteArrayInputStream(input.getBytes()));
    //        outContent.reset(); // Clear output before calling method
    //        workoutManager.deleteWorkoutByIndex(0);
    //        System.err.println("Debug Output (y input): " + outContent.toString());
    //        assertEquals(0, workoutManager.getWorkoutSize(),
    //                "Workout should be deleted with 'y' input");
    //        assertTrue(outContent.toString().contains("Deleted workout"),
    //                "Success message should be printed");
    //        assertFalse(outContent.toString().contains("Okay, deletion aborted"),
    //                "Deletion should not be aborted");
    //    }
    //
    //    @Test
    //    public void testDeleteWorkoutByIndex_validIndex_yNoNewline_success() {
    //        workoutManager.addWorkout("/create_workout n/Test Workout", "testUser");
    //        String input = "y"; // Test without newline, in case Scanner.next() is used
    //        System.setIn(new ByteArrayInputStream(input.getBytes()));
    //        outContent.reset();
    //        workoutManager.deleteWorkoutByIndex(0);
    //        System.err.println("Debug Output (y no newline): " + outContent.toString());
    //        assertEquals(0, workoutManager.getWorkoutSize(),
    //                "Workout should be deleted with 'y' input");
    //        assertTrue(outContent.toString().contains("Deleted workout"),
    //                "Success message should be printed");
    //        assertFalse(outContent.toString().contains("Okay, deletion aborted"),
    //                "Deletion should not be aborted");
    //    }
    //
    //    @Test
    //    public void testDeleteWorkoutByName_validName_success() {
    //        workoutManager.addWorkout("/create_workout n/Test Workout", "testUser");
    //        String input = "y\n"; // Match interactive input from console
    //        System.setIn(new ByteArrayInputStream(input.getBytes()));
    //        outContent.reset();
    //        workoutManager.deleteWorkout("Test Workout");
    //        System.err.println("Debug Output (delete by name, y input): " + outContent.toString());
    //        assertEquals(0, workoutManager.getWorkoutSize(),
    //                "Workout should be deleted with 'y' input");
    //        assertTrue(outContent.toString().contains("Workout deleted successfully"),
    //                "Success message should be printed");
    //        assertFalse(outContent.toString().contains("Okay, I didn’t delete it"),
    //                "Deletion should not be aborted");
    //    }
    //
    //    @Test
    //    public void testInteractiveDeleteWorkout_validSelection_success() {
    //        workoutManager.addWorkout("/create_workout n/Test Workout d/22/10/25 t/0900",
    //                "testUser");
    //        String input = "1\n";
    //        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
    //        workoutManager.interactiveDeleteWorkout("/delete_workout d/22/10/25", scanner);
    //
    //        assertEquals(0, workoutManager.getWorkoutSize(), "Workout should be deleted");
    //        assertTrue(outContent.toString().contains("Delete: Test Workout"),
    //                "Success message should be printed");
    //    }
}
