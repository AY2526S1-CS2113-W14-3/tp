package seedu.fitchasers.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single weight record for a person.
 * Each record stores the weight in kilograms and the date of measurement.
 */
public class WeightRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private double weight;
    private final LocalDate date;

    public WeightRecord(double weight, LocalDate date) {
        this.weight = weight;
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("Date: %s | Weight: %.1f kg",
                date.format(DateTimeFormatter.ofPattern("dd/MM/yy")), weight);
    }
}
