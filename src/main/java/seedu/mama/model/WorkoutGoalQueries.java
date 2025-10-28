// src/main/java/seedu/mama/model/WorkoutGoalQueries.java
package seedu.mama.model;

import seedu.mama.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Utility methods for weekly workout aggregation and resolving the active weekly workout goal.
 *
 * Definitions:
 * - "Week" means the 7-day window starting at weekStart (inclusive) and ending before weekStart + 7 days (exclusive).
 * - weekStart should be the Monday 00:00 boundary produced by DateTimeUtil.weekStartMonday(...).
 *
 * Only timestamps of TimestampedEntry are considered; non-workout entries are ignored in minute sums.
 */
public final class WorkoutGoalQueries {
    private WorkoutGoalQueries() {
    }

    /**
     * Returns the total workout minutes recorded during the target week.
     *
     * The target week is [weekStart, weekStart + 7 days). Only entries of type WorkoutEntry
     * whose timestamp falls within that window are counted.
     *
     * @param all        all entries (workouts, goals, etc.); only WorkoutEntry are summed
     * @param weekStart  start of the target week (typically Monday 00:00)
     * @return non-negative total number of minutes of workouts in the target week
     * @see DateTimeUtil#inSameWeek(LocalDateTime, LocalDateTime)
     */
    public static int sumWorkoutMinutesThisWeek(List<Entry> all, LocalDateTime weekStart) {
        int sum = 0;
        for (Entry e : all) {
            if (e instanceof WorkoutEntry we) {
                LocalDateTime ts = ((TimestampedEntry) we).timestamp();
                if (DateTimeUtil.inSameWeek(ts, weekStart)) {
                    sum += we.getDuration();
                }
            }
        }
        return sum;
    }

    /**
     * Returns the workout goal that applies to the target week, or null if none exists.
     *
     * Among all WorkoutGoalEntry entries whose timestamp falls within the target week
     * [weekStart, weekStart + 7 days), the one with the latest timestamp is returned.
     * If no goal was set during that week, this returns null.
     *
     * @param all        all entries (workouts, goals, etc.)
     * @param weekStart  start of the target week (typically Monday 00:00)
     * @return the latest WorkoutGoalEntry set in the target week, or null if no goal was set that week
     * @see DateTimeUtil#inSameWeek(LocalDateTime, LocalDateTime)
     */
    public static WorkoutGoalEntry currentWeekGoal(List<Entry> all, LocalDateTime weekStart) {
        WorkoutGoalEntry latestThisWeek = null;

        for (Entry e : all) {
            if (e instanceof WorkoutGoalEntry g) {
                LocalDateTime ts = ((TimestampedEntry) g).timestamp();
                if (DateTimeUtil.inSameWeek(ts, weekStart)) {
                    if (latestThisWeek == null || ts.isAfter(((TimestampedEntry) latestThisWeek).timestamp())) {
                        latestThisWeek = g;
                    }
                }
            }
        }
        return latestThisWeek;
    }
}
