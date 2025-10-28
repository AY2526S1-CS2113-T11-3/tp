package seedu.mama.model;

import seedu.mama.util.DateTimeUtil;

import java.time.LocalDateTime;

/**
 * Workout entry with timestamp, workout type, duration in minutes, and feel rating (1–5).
 *
 * Storage format: WORKOUT|type|duration|feel|timestamp
 */
public final class WorkoutEntry extends TimestampedEntry {

    private final String workoutType; //e.g .,"running","yoga"
    private final int durationMins; // duration in minutes
    private final int feel; // user mood/recovery scale 1–5

    /**
     * Constructor for new user-created workouts (timestamp = now()).
     */
    public WorkoutEntry(String workoutType, int durationMins, int feel) {
        super("WORKOUT", workoutType.trim());
        if (durationMins <= 0) {
            throw new IllegalArgumentException("Workout duration must be positive minutes.");
        }
        this.workoutType = workoutType.trim();
        this.durationMins = durationMins;
        this.feel = feel;
    }

    /**
     * Constructor used when reading from storage (explicit timestamp).
     */
    public WorkoutEntry(String workoutType, int durationMins, int feel, LocalDateTime when) {
        super("WORKOUT", workoutType.trim(), when);
        this.workoutType = workoutType.trim();
        this.durationMins = durationMins;
        this.feel = feel;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public int getDuration() {
        return durationMins;
    }

    public int getFeel() {
        return feel;
    }

    /**
     * Returns a single-line display string, e.g.:
     * "[Workout] running (40 mins, feel 4/5) (2025-10-28T09:15)"
     */
    @Override
    public String toListLine() {
        return "[Workout] " + workoutType + " (" + durationMins + " mins, feel " + feel + "/5) "
                + "(" + timestampString() + ")";
    }

    /**
     * Returns the storage line in the format:
     * WORKOUT|type|duration|feel|timestamp
     */
    @Override
    public String toStorageString() {
        return withTimestamp("WORKOUT|" + workoutType + "|" + durationMins + "|" + feel);
    }

    /**
     * Parses a stored WORKOUT line back into a WorkoutEntry.
     *
     * @param line storage line in the expected format
     * @return a WorkoutEntry parsed from the line
     * @throws IllegalArgumentException if the line is malformed
     */
    public static WorkoutEntry fromStorage(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 5 || !"WORKOUT".equals(parts[0])) {
            throw new IllegalArgumentException("Bad WORKOUT line (expect 5 parts): " + line);
        }

        String type = parts[1].trim();
        int mins = Integer.parseInt(parts[2].trim());
        int feel = Integer.parseInt(parts[3].trim());
        LocalDateTime ts = DateTimeUtil.parse(parts[4].trim());
        return new WorkoutEntry(type, mins, feel, ts);
    }
}
