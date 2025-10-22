package seedu.fitchasers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PersonTest {
    private Person person;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

    @BeforeEach
    public void setUp() {
        person = new Person("testUser");
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    public void testAddWeightRecord_success() {
        LocalDate date = LocalDate.parse("22/10/25", formatter);
        WeightRecord record = new WeightRecord(70.5, date);
        person.addWeightRecord(record);

        assertEquals(1, person.getWeightHistory().size(), "One weight record should be added");
        assertEquals(record, person.getWeightHistory().get(0), "Weight record should match");
    }

    @Test
    public void testDisplayWeightHistory_emptyHistory_showsEmptyMessage() {
        person.displayWeightHistory();
        assertTrue(outContent.toString().contains("No weight history recorded yet"), "Empty history message should be printed");
    }

    @Test
    public void testDisplayWeightHistory_withRecords_displaysCorrectly() {
        LocalDate date = LocalDate.parse("22/10/25", formatter);
        WeightRecord record = new WeightRecord(70.5, date);
        person.addWeightRecord(record);
        person.displayWeightHistory();
        assertTrue(outContent.toString().contains(record.toString()), "Weight record should be displayed");
    }

    @Test
    public void testSetAndGetName_success() {
        person.setName("newUser");
        assertEquals("newUser", person.getName(), "Name should be updated");
    }
}
