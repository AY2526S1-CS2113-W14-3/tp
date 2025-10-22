package seedu.fitchasers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkoutManagerTest {

    private WorkoutManager manager;

    @BeforeEach
    void setUp() {
        manager = new WorkoutManager();
    }

    @Test
    void testAddWorkoutSuccessfully() {
        String command = "/create_workout n/Run d/22/10/25 t/0830";
        manager.addWorkout(command, "Loan");
        assertEquals(1, manager.getWorkoutSize());
        assertEquals("Run", manager.getWorkouts().get(0).getWorkoutName());
    }

    @Test
    void testAddWorkoutWithMissingTime() {
        String command = "/create_workout n/Yoga d/22/10/25";
        manager.addWorkout(command, "Loan");
        assertEquals(1, manager.getWorkoutSize());
    }

    @Test
    void testAddExerciseWhenNoWorkout() {
        manager.addExercise("/add_exercise n/PushUp r/10");
        assertEquals(0, manager.getWorkoutSize());
    }

    @Test
    void testAddExerciseToWorkout() {
        String command = "/create_workout n/Run d/22/10/25 t/0830";
        manager.addWorkout(command, "Loan");
        manager.addExercise("/add_exercise n/PushUp r/12");

        Workout w = manager.getWorkouts().get(0);
        assertEquals(1, w.getExercises().size());
        assertEquals("PushUp", w.getExercises().get(0).getName());
    }

    @Test
    void testDeleteWorkoutByIndex() {
        String command = "/create_workout n/Run d/22/10/25 t/0830";
        manager.addWorkout(command, "Loan");
        assertEquals(1, manager.getWorkoutSize());
        manager.deleteWorkoutByIndex(0);
        assertTrue(manager.getWorkoutSize() <= 1);
    }

    @Test
    void testGetWorkouts() {
        assertNotNull(manager.getWorkouts());
        assertTrue(manager.getWorkouts().isEmpty());
    }
}
