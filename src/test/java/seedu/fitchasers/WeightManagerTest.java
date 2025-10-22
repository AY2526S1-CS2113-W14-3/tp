package seedu.fitchasers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Unit tests for {@link WeightManager}.
 */
public class WeightManagerTest {

    private TestUI testUI;
    private TestFileHandler testFileHandler;
    private Person testPerson;
    private WeightManager weightManager;

    /** Mock UI để ghi lại thông báo */
    private static class TestUI extends UI {
        private final ArrayList<String> messages = new ArrayList<>();

        @Override
        public void showMessage(String message) {
            messages.add(message);
        }

        public ArrayList<String> getMessages() {
            return messages;
        }
    }

    /** Mock FileHandler để tránh đọc/ghi file */
    private static class TestFileHandler extends FileHandler {
        @Override
        public ArrayList<WeightRecord> loadWeightHistory(String name) {
            return new ArrayList<>();
        }

        @Override
        public void saveWeightHistory(String name, ArrayList<WeightRecord> history) {
            // không làm gì cả
        }
    }

    @BeforeEach
    public void setUp() {
        testUI = new TestUI();
        testFileHandler = new TestFileHandler();
        testPerson = new Person("Loan");
        weightManager = new WeightManager(testPerson, testUI, testFileHandler);
    }

    @Test
    public void addWeight_validCommand_addsRecordSuccessfully() {
        weightManager.addWeight("/add_weight w/50.5 d/22/10/25");
        assertEquals(1, testPerson.getWeightHistory().size());
        assertEquals(50.5, testPerson.getWeightHistory().get(0).getWeight());
        assertTrue(testUI.getMessages().stream().anyMatch(m -> m.contains("Logging your weight")));
    }

    @Test
    public void addWeight_invalidWeight_showsErrorMessage() {
        weightManager.addWeight("/add_weight w/abc d/22/10/25");
        assertTrue(testUI.getMessages().contains("Invalid weight. Please enter a number."));
        assertEquals(0, testPerson.getWeightHistory().size());
    }

    @Test
    public void addWeight_invalidDate_showsErrorMessage() {
        weightManager.addWeight("/add_weight w/55.2 d/2025-10-22");
        assertTrue(testUI.getMessages().contains("Invalid date format. Use dd/MM/yy."));
        assertEquals(0, testPerson.getWeightHistory().size());
    }

    @Test
    public void addWeight_missingFields_showsInvalidInputMessage() {
        weightManager.addWeight("/add_weight w/55.2");
        assertTrue(testUI.getMessages().contains("Invalid input. Correct format: /add_weight w/WEIGHT d/DATE"));
        assertEquals(0, testPerson.getWeightHistory().size());
    }

    @Test
    public void viewWeights_showsHistory() {
        testPerson.addWeightRecord(new WeightRecord(50.5, LocalDate.now()));
        weightManager.viewWeights();
        assertFalse(testPerson.getWeightHistory().isEmpty());
    }
}
