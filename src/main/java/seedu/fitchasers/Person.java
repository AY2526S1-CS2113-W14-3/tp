package seedu.fitchasers;

import java.io.Serializable; // Added for serialization
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Represents a person with a name and a list of weight records.
 */
public class Person implements Serializable { // Added implements Serializable

    private static final long serialVersionUID = 1L; // Added for serialization safety

    private String name;
    private final ArrayList<WeightRecord> weightHistory;
    private final UI ui;

    /**
     * Constructs a new Person with the specified name.
     * Initializes an empty weight history.
     *
     * @param name The name of the person
     */
    public Person(String name) {
        this.name = name;
        this.weightHistory = new ArrayList<>();
        this.ui = new UI();
    }

    /**
     * Returns the name of the person.
     *
     * @return The person's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person.
     *
     * @param name The new name for the person
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a weight record to the person's weight history.
     *
     * @param weightRecord The weight record to add
     */
    public void addWeightRecord(WeightRecord weightRecord) {
        weightHistory.add(weightRecord);
    }

    /**
     * Displays the person's weight history in a formatted manner.
     * If no weight records exist, informs the user.
     */
    public void displayWeightHistory() {
        if (weightHistory.isEmpty()) {
            ui.showMessage("No weight history recorded yet.");
            return;
        }

        for (WeightRecord record : weightHistory) {
            ui.showMessage(record.toString());
        }
    }

    /**
     * Returns the person's weight history.
     *
     * @return The list of weight records
     */
    public ArrayList<WeightRecord> getWeightHistory() {
        return weightHistory;
    }
}