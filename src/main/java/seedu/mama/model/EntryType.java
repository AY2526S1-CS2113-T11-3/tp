package seedu.mama.model;

/**
 * Defines the valid types of entries in the application.
 */
public enum EntryType {
    MEAL(MealEntry.class),
    WORKOUT(WorkoutEntry.class),
    MILK(MilkEntry.class),
    WEIGHT(WeightEntry.class),
    MEASURE(BodyMeasurementEntry.class),
    WORKOUT_GOAL(WorkoutGoalEntry.class);

    // This field can be used for more advanced logic later if needed
    public final Class<? extends Entry> entryClass;

    EntryType(Class<? extends Entry> entryClass) {
        this.entryClass = entryClass;
    }

    /**
     * Returns a comma-separated list of all valid type names.
     * e.g., "meal, workout, milk, weight"
     */
    public static String getValidTypesString() {
        return String.join(", ",
                java.util.Arrays.stream(EntryType.values())
                        .map(et -> et.name().toLowerCase())
                        .toArray(String[]::new));
    }
}
