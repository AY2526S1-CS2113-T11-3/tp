package seedu.mama.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.model.WorkoutEntry;
import seedu.mama.model.WorkoutGoalEntry;
import seedu.mama.model.WorkoutGoalQueries;
import seedu.mama.storage.Storage;
import seedu.mama.util.DateTimeUtil;

/**
 * Shows the user's workout goal for the current week and the workouts completed this week,
 * including total minutes and remaining minutes to reach the goal.
 *
 * Week is determined using DateTimeUtil.weekStartMonday(now), i.e., Monday 00:00 to Sunday 23:59:59.
 * This command does not modify state; it gathers data from the current EntryList and formats it for display.
 */
public final class ViewWorkoutGoalCommand implements Command {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

    private static final Logger logger = Logger.getLogger(ViewWorkoutGoalCommand.class.getName());

    /**
     * Builds a summary of this week's workout goal and workouts, and returns it as CommandResult.
     *
     * Behavior:
     * - Determines current week using weekStartMonday(now)
     * - Finds the latest goal set in this week (if any)
     * - Lists workouts done this week and sums their minutes
     * - If a goal exists, includes remaining minutes; otherwise, reminds user to set a goal
     *
     * @param list    entries to read from
     * @param storage unused for this command (may be null)
     * @return a CommandResult containing the formatted weekly goal and progress
     * @throws CommandException never thrown in current implementation
     */
    @Override
    public CommandResult execute(EntryList list, Storage storage) throws CommandException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = DateTimeUtil.weekStartMonday(now);
        logger.info("ViewWorkoutGoalCommand: executing for week starting " + weekStart.format(FMT));

        // 1) Find this week's goal (latest goal set within [Mon..Sun))
        WorkoutGoalEntry goal = WorkoutGoalQueries.currentWeekGoal(list.asList(), weekStart);
        logger.fine("Weekly goal lookup: " + (goal == null ? "none" : (goal.getMinutesPerWeek() + " mins/week")));

        // 2) Collect this week's workouts and total minutes
        int minutesThisWeek = 0;
        List<String> thisWeeksWorkouts = new ArrayList<>();

        for (Entry e : list.asList()) {
            if (e instanceof WorkoutEntry w) {
                LocalDateTime ts = w.getTimestamp(); // <-- use real timestamp
                if (DateTimeUtil.inSameWeek(ts, weekStart)) {
                    minutesThisWeek += w.getDuration();
                    thisWeeksWorkouts.add(
                            "[Workout] " + w.description() +
                                    " (" + w.getDuration() + " mins) (" + w.timestampString() + ")"
                    );
                }
            }
        }
        logger.fine("Completed " + thisWeeksWorkouts.size() + " workouts this week; total minutes=" + minutesThisWeek);

        // 3) Build output
        StringBuilder sb = new StringBuilder();

        if (goal == null) {
            logger.info("No weekly goal set for this week.");
            sb.append("No workout goal set for this week. ")
                    .append("You can set one with: `workout goal <minutes>`\n");
            sb.append("Workouts done this week:\n");
            if (thisWeeksWorkouts.isEmpty()) {
                sb.append("  (none yet)\n");
            } else {
                for (int i = 0; i < thisWeeksWorkouts.size(); i++) {
                    sb.append(i + 1).append(") ").append(thisWeeksWorkouts.get(i)).append("\n");
                }
            }
            sb.append("You have completed ").append(minutesThisWeek)
                    .append(" minute").append(minutesThisWeek == 1 ? "" : "s")
                    .append(" so far this week.");
            return new CommandResult(sb.toString());
        }

        // With a goal
        int target = goal.getMinutesPerWeek();
        int remaining = Math.max(0, target - minutesThisWeek);
        logger.info("Weekly goal present: target=" + target + " mins; completed=" + minutesThisWeek
                + "; remaining=" + remaining);

        sb.append("The weekly goal you set for the week is: ")
                .append(target).append(" minutes.\n");

        sb.append("Workouts done this week:\n");
        if (thisWeeksWorkouts.isEmpty()) {
            sb.append("  (none yet)\n");
        } else {
            for (int i = 0; i < thisWeeksWorkouts.size(); i++) {
                sb.append(i + 1).append(") ").append(thisWeeksWorkouts.get(i)).append("\n");
            }
        }

        if (remaining == 0) {
            sb.append("You have completed ").append(minutesThisWeek)
                    .append(" out of the required ").append(target)
                    .append(" minutes this week. Goal reached—awesome job!");
        } else {
            sb.append("You have completed ").append(minutesThisWeek)
                    .append(" out of the required ").append(target)
                    .append(" minutes this week, ")
                    .append(remaining).append(" more minutes to go! ")
                    .append("Lets get to work Mama!!");
        }

        return new CommandResult(sb.toString());
    }
}
