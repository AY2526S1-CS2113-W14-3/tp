package seedu.fitchasers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkoutTest {

    private Workout workout;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.of(2025, 10, 22, 10, 0);
        end = LocalDateTime.of(2025, 10, 22, 11, 30);
        workout = new Workout("Morning Run", start, end, "Loan");
    }

    @Test
    void testWorkoutInitialization() {
        assertEquals("Morning Run", workout.getWorkoutName());
        assertEquals("Loan", workout.getUsername());
        assertEquals(90, workout.getDuration());
    }

    @Test
    void testAddExercise() {
        Exercise ex = new Exercise("Push Up", 10);
        workout.addExercise(ex);
        assertEquals(1, workout.getExercises().size());
        assertEquals(ex, workout.getCurrentExercise());
    }

    @Test
    void testTagsSetterGetter() {
        Set<String> tags = Set.of("cardio", "morning");
        workout.setTags(tags);
        assertEquals(2, workout.getTags().size());
        assertTrue(workout.getTags().contains("cardio"));
    }

    @Test
    void testToStringOutput() {
        String result = workout.toString();
        assertTrue(result.contains("Workout Name: Morning Run"));
        assertTrue(result.contains("Duration: 90"));
    }
}
