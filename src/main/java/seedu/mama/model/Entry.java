package seedu.mama.model;

import java.util.Objects;

/**
 * Base type for all entries (Meal, Pump, Weight, Workout, Note, ...).
 */
public abstract class Entry {
    private final String type;         // e.g., MEAL, NOTE
    private final String description;  // human-readable

    protected Entry(String type, String description) {
        this.type = Objects.requireNonNull(type);
        this.description = Objects.requireNonNull(description);
    }

    public String type() {
        return type;
    }

    public String description() {
        return description;
    }

    /**
     * One-line string for previews/lists.
     */
    public String toListLine() {
        return "[" + type + "] " + description;
    }

    public Boolean contains(String keyword) {
        return this.description().contains(keyword);
    }

    /**
     * Stable storage form; subclasses may append fields.
     */
    public abstract String toStorageString();

    /**
     * Factory from storage line; dispatch by type token.
     */
    public static Entry fromStorageString(String line) {
        String[] parts = line.split("\\|", 2); // TYPE|payload
        if (parts.length == 0) {
            throw new IllegalArgumentException("Bad line: " + line);
        }
        switch (parts[0]) {
        case "NOTE":
            return NoteEntry.fromStorage(line);
        case "WORKOUT":
            return WorkoutEntry.fromStorage(line);
        // future: case "MEAL": return MealEntry.fromStorage(line);
        case "WEIGHT":
            return WeightEntry.fromStorage(line);
        default:
            throw new IllegalArgumentException("Unknown type: " + parts[0]);
        }
    }
}
