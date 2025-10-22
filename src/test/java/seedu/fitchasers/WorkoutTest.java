package seedu.fitchasers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkoutTest {

    @Test
    public void testWorkoutCreation_withStartDateTime_success() {
        LocalDateTime start = LocalDateTime.parse("22/10/25 0900",
                DateTimeFormatter.ofPattern("dd/MM/yy HHmm"));
        Workout workout = new Workout("Morning Run", start, "testUser");

        assertEquals("Morning Run", workout.getWorkoutName(), "Workout name should match");
        assertEquals(start, workout.getWorkoutStartDateTime(), "Start date-time should match");
        assertEquals("testUser", workout.getUsername(), "Username should match");
        assertEquals(0, workout.getDuration(), "Initial duration should be 0");
    }

    @Test
    public void testAddExercise_success() {
        Workout workout = new Workout("Test Workout", LocalDateTime.now(), "testUser");
        Exercise exercise = new Exercise("Push Up", 10);
        workout.addExercise(exercise);

        assertEquals(1, workout.getExercises().size(), "One exercise should be added");
        assertEquals(exercise, workout.getCurrentExercise(), "Current exercise should match");
    }

    @Test
    public void testCalculateDuration_validStartAndEnd_success() {
        LocalDateTime start = LocalDateTime.parse("22/10/25 0900",
                DateTimeFormatter.ofPattern("dd/MM/yy HHmm"));
        LocalDateTime end = LocalDateTime.parse("22/10/25 1000",
                DateTimeFormatter.ofPattern("dd/MM/yy HHmm"));
        Workout workout = new Workout("Test Workout", start, end, "testUser");

        assertEquals(60, workout.calculateDuration(), "Duration should be 60 minutes");
        assertEquals(60, workout.getDuration(), "Stored duration should match calculated duration");
    }

    @Test
    public void testGetWorkoutDateString_validDate_formattedCorrectly() {
        LocalDateTime start = LocalDateTime.parse("22/10/25 0900",
                DateTimeFormatter.ofPattern("dd/MM/yy HHmm"));
        Workout workout = new Workout("Test Workout", start, "testUser");
        String expected = "Wednesday 22nd of October";
        assertEquals(expected, workout.getWorkoutDateString(),
                "Date string should be formatted correctly");
    }
}
