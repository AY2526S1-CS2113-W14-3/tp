package seedu.fitchasers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person("Loan");
    }

    @Test
    void testGetAndSetName() {
        assertEquals("Loan", person.getName());
        person.setName("Mai");
        assertEquals("Mai", person.getName());
    }

    @Test
    void testAddWeightRecord() {
        WeightRecord record = new WeightRecord(50.5, LocalDate.parse("2025-10-22"));
        person.addWeightRecord(record);
        ArrayList<WeightRecord> history = person.getWeightHistory();
        assertEquals(1, history.size());
        assertEquals(record, history.get(0));
    }

    @Test
    void testDisplayWeightHistoryEmpty() {
        // kiểm tra trường hợp không có lịch sử cân nặng
        assertTrue(person.getWeightHistory().isEmpty());
    }
}
