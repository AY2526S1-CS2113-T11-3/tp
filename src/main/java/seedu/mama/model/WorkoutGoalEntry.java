// src/main/java/seedu/mama/model/WorkoutGoalEntry.java
package seedu.mama.model;

import seedu.mama.util.DateTimeUtil;

import java.time.LocalDateTime;

/**
 * Represents a weekly workout goal set by the user.
 * Immutable value object extending TimestampedEntry.
 *
 * Storage format: WORKOUT_GOAL|minutes|timestamp
 * - minutes is a positive integer (minutes per week)
 * - timestamp is the recorded time for when this goal was set
 */
public final class WorkoutGoalEntry extends TimestampedEntry {
    private final int minutesPerWeek;

    /**
     * Creates a new goal from user input.
     * Timestamp is set to now().
     *
     * @param minutesPerWeek positive number of minutes per week
     * @throws IllegalArgumentException if minutesPerWeek <= 0
     */
    public WorkoutGoalEntry(int minutesPerWeek) {
        super("WORKOUT_GOAL", minutesPerWeek + " mins per week");
        if (minutesPerWeek <= 0) {
            throw new IllegalArgumentException("Goal must be positive minutes per week.");
        }
        this.minutesPerWeek = minutesPerWeek;
    }

    /**
     * Creates a goal with an explicit timestamp (used during deserialization).
     * Assumes minutesPerWeek has already been validated by the caller.
     *
     * @param minutesPerWeek minutes per week (should be > 0)
     * @param when           timestamp to associate with this goal
     */
    public WorkoutGoalEntry(int minutesPerWeek, LocalDateTime when) {
        super("WORKOUT_GOAL", minutesPerWeek + " mins per week", when);
        this.minutesPerWeek = minutesPerWeek;
    }

    /**
     * Returns the goal in minutes per week (alias of minutesPerWeek()).
     *
     * @return minutes per week
     */
    public int getMinutesPerWeek() {
        return minutesPerWeek();
    }

    /**
     * Returns the goal in minutes per week.
     *
     * @return minutes per week
     */
    public int minutesPerWeek() {
        return minutesPerWeek;
    }

    public String getDate() {
        return timestampString();
    }

    /**
     * Returns the user-facing line for list views.
     * Example: [GOAL] 150 mins/week (28/10/25 23:59)
     *
     * @return display string for this entry
     */
    @Override
    public String toListLine() {
        // [GOAL] 150 mins/week (28/10/25 23:59)
        return "[GOAL] " + minutesPerWeek + " mins/week (" + timestampString() + ")";
    }

    /**
     * Returns the storage line for this entry.
     * Format: WORKOUT_GOAL|minutes|timestamp
     *
     * @return storage string
     */
    @Override
    public String toStorageString() {
        // WORKOUT_GOAL|150|28/10/25 23:59
        return withTimestamp("WORKOUT_GOAL|" + minutesPerWeek);
    }

    /**
     * Parses a stored line into a WorkoutGoalEntry.
     * Strictly expects: WORKOUT_GOAL|minutes|timestamp
     *
     * @param line storage line
     * @return a WorkoutGoalEntry parsed from the line
     * @throws IllegalArgumentException if the line is malformed or minutes is not an integer
     */
    public static WorkoutGoalEntry fromStorage(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length != 3 || !"WORKOUT_GOAL".equals(p[0])) {
            throw new IllegalArgumentException("Bad WORKOUT_GOAL line (expect 3 parts): " + line);
        }
        int mins = Integer.parseInt(p[1].trim());
        LocalDateTime ts = DateTimeUtil.parse(p[2].trim());
        return new WorkoutGoalEntry(mins, ts);
    }
}
