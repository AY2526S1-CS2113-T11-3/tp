package seedu.mama.command;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import seedu.mama.model.EntryList;
import seedu.mama.model.WorkoutGoalQueries;
import seedu.mama.model.WorkoutEntry;
import seedu.mama.model.WorkoutGoalEntry;
import seedu.mama.storage.Storage;
import seedu.mama.util.DateTimeUtil;

/**
 * Adds a workout entry (type, duration in minutes, feel rating 1–5) to the list
 * and reports weekly goal progress.
 *
 * Usage: workout TYPE /dur DURATION /feel FEEL
 * Also accepts compact forms like: workout run/dur30/feel3
 *
 * Side effects: appends to EntryList; saves via Storage if provided.
 * Logging: INFO for success; WARNING for parse/validation problems; FINE for details.
 */
public final class AddWorkoutCommand implements Command {
    private static final Logger logger = Logger.getLogger(AddWorkoutCommand.class.getName());

    private final String workoutType;
    private final int duration;
    private final int feel;

    /**
     * Creates a command with validated parameters.
     *
     * @param workoutType non-empty workout type (e.g., "running")
     * @param duration    duration in minutes; must be > 0
     * @param feel        feel rating in [1, 5]
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public AddWorkoutCommand(String workoutType, int duration, int feel) {
        if (workoutType == null || workoutType.trim().isEmpty()) {
            logger.warning("Constructor validation failed: empty workout type");
            throw new IllegalArgumentException("Workout type cannot be empty.");
        }
        if (duration <= 0) {
            logger.warning(() -> "Constructor validation failed: non-positive duration=" + duration);
            throw new IllegalArgumentException("Workout duration must be positive minutes.");
        }
        if (feel < 1 || feel > 5) {
            logger.warning(() -> "Constructor validation failed: feel out of range=" + feel);
            throw new IllegalArgumentException("Feel rating must be between 1 and 5.");
        }
        this.workoutType = workoutType;
        this.duration = duration;
        this.feel = feel;
        logger.fine("AddWorkoutCommand created: type=" + workoutType
                + ", duration=" + duration + ", feel=" + feel);
    }

    /**
     * Parses user input starting with the word "workout" into a command instance.
     * Accepts both spaced and compact markers (e.g., "/dur 30" or "/dur30").
     * Requires exactly one /dur and exactly one /feel. Validates duration > 0 and feel in [1,5].
     *
     * @param input full user input including the leading "workout"
     * @return a validated AddWorkoutCommand
     * @throws CommandException if segments are missing, repeated, or values are invalid
     */
    public static AddWorkoutCommand fromInput(String input) throws CommandException {
        logger.fine("Parsing AddWorkoutCommand from input: " + input);
        String after = input.substring("workout".length()).trim();
        if (after.isEmpty()) {
            throw new CommandException("Workout type cannot be empty.\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }

        // ---- Check for multiple /dur or /feel ----
        int durFirst = after.indexOf("/dur");
        int durLast = after.lastIndexOf("/dur");
        int feelFirst = after.indexOf("/feel");
        int feelLast = after.lastIndexOf("/feel");

        if (durFirst == -1) {
            logger.warning("Parse error: missing /dur segment");
            throw new CommandException("Missing '/dur' segment.\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }
        if (feelFirst == -1) {
            logger.warning("Parse error: missing /feel segment");
            throw new CommandException("Missing '/feel' segment.\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }
        if (durFirst != durLast) {
            logger.warning("Parse error: multiple /dur segments detected");
            throw new CommandException("Too many '/dur' segments.\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }
        if (feelFirst != feelLast) {
            logger.warning("Parse error: multiple /feel segments detected");
            throw new CommandException("Too many '/feel' segments.\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }

        // ---- Parse segments ----
        String[] beforeDurSplit = after.split("/dur", 2);
        String type = beforeDurSplit[0].trim();
        if (type.isEmpty()) {
            logger.warning("Parse error: empty workout type before /dur");
            throw new CommandException("Workout type cannot be empty.\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }

        String rhs = beforeDurSplit[1].trim();
        String[] durFeelSplit = rhs.split("/feel", 2);
        if (durFeelSplit.length < 2) {
            logger.warning("Parse error: /feel segment not found after /dur");
            throw new CommandException("Missing '/feel' segment.\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }

        String durPart = durFeelSplit[0].trim();
        String feelPart = durFeelSplit[1].trim();

        // ---- Validate duration ----
        if (durPart.isEmpty()) {
            logger.warning("Parse error: empty duration after /dur");
            throw new CommandException("Missing duration after '/dur'.\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }
        String[] durTokens = durPart.split("\\s+");
        if (durTokens.length > 1) {
            logger.warning("Parse error: unexpected token after duration: " + durTokens[1]);
            throw new CommandException("Unexpected input after duration: '" + durTokens[1] +
                    "'\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }

        int duration;
        try {
            duration = Integer.parseInt(durTokens[0]);
        } catch (NumberFormatException e) {
            logger.warning("Parse error: non-numeric duration token: " + durTokens[0]);
            throw new CommandException("Duration must be a whole number (minutes).", e);
        }
        if (duration <= 0) {
            logger.warning(() -> "Parse error: non-positive duration=" + duration);
            throw new CommandException("Duration must be a positive number of minutes.");
        }

        // ---- Validate feel ----
        if (feelPart.isEmpty()) {
            logger.warning("Parse error: empty feel rating after /feel");
            throw new CommandException("Missing feel rating after '/feel'." +
                    "\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }
        String[] feelTokens = feelPart.split("\\s+");
        if (feelTokens.length > 1) {
            logger.warning("Parse error: unexpected token after feel: " + feelTokens[1]);
            throw new CommandException("Unexpected input after feel: '" + feelTokens[1] +
                    "'\nUsage: workout TYPE /dur DURATION /feel FEEL");
        }

        int feel;
        try {
            feel = Integer.parseInt(feelTokens[0]);
        } catch (NumberFormatException e) {
            logger.warning("Parse error: non-numeric feel token: " + feelTokens[0]);
            throw new CommandException("Feel rating must be a number from 1–5.");
        }
        if (feel < 1 || feel > 5) {
            logger.warning(() -> "Parse error: feel out of range=" + feel);
            throw new CommandException("Feel rating must be between 1 and 5.");
        }

        logger.info("Parsed AddWorkoutCommand: type=" + type + ", duration=" + duration + ", feel=" + feel);
        return new AddWorkoutCommand(type, duration, feel);
    }

    /**
     * Adds a WorkoutEntry to the list, persists the list if storage is provided,
     * and returns a message that includes weekly goal progress.
     *
     * @param list    mutable list to append the new entry to
     * @param storage optional storage; if non-null, list is saved
     * @return command feedback for the user
     * @throws CommandException if execution fails (e.g., storage error)
     */
    @Override
    public CommandResult execute(EntryList list, Storage storage) throws CommandException {
        logger.info("Executing AddWorkoutCommand: type=" + workoutType + ", duration=" + duration + ", feel=" + feel);

        WorkoutEntry entry = new WorkoutEntry(workoutType, duration, feel);
        list.add(entry);
        logger.fine("Workout entry added to EntryList");

        if (storage != null) {
            logger.fine("Saving EntryList to storage...");
            storage.save(list);
            logger.fine("Storage save completed");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = DateTimeUtil.weekStartMonday(now);
        WorkoutGoalEntry goal = WorkoutGoalQueries.currentWeekGoal(list.asList(), weekStart);
        int weekSoFar = WorkoutGoalQueries.sumWorkoutMinutesThisWeek(list.asList(), weekStart);
        int remaining = (goal == null) ? 0 : Math.max(0, goal.getMinutesPerWeek() - weekSoFar);

        logger.fine("Goal snapshot: goalMinutes=" + (goal == null ? null : goal.getMinutesPerWeek())
                + ", weekSoFar=" + weekSoFar + ", remaining=" + remaining);

        StringBuilder sb = new StringBuilder();
        sb.append("Got it. I've logged this workout:\n")
                .append("  ").append(entry.toListLine()).append("\n");

        if (goal == null) {
            sb.append("Reminder: Workout goal for the week is not set yet! ")
                    .append("You can set one with: `workout goal <minutes>`\n");
        } else {
            if (remaining == 0) {
                sb.append("Woohoo! Weekly goal reached!! Great job Mama:)\n");
            } else {
                sb.append("You have ").append(remaining)
                        .append(" minutes left to reach this week's goal (")
                        .append(goal.getMinutesPerWeek()).append(" mins). You can do it Mama!!\n");
            }
        }

        long workoutCount = list.asList().stream()
                .filter(e -> "WORKOUT".equals(e.type()))
                .count();
        logger.info("Workout added successfully. New lifetime total: " + workoutCount);

        sb.append("Great job Mama! You now have a lifetime total of ")
                .append(workoutCount)
                .append(" workouts completed! Lets keep it up!!");

        return new CommandResult(sb.toString());
    }
}
