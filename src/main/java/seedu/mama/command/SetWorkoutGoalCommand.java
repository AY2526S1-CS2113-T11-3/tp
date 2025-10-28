package seedu.mama.command;

import java.util.logging.Logger;

import seedu.mama.model.EntryList;
import seedu.mama.model.WorkoutGoalEntry;
import seedu.mama.storage.Storage;

/**
 * Sets the user's weekly workout goal (in minutes) and persists it.
 *
 * Usage: {@code workout goal <minutes>}
 *
 * Behavior:
 * - Parses "{@code workout goal <minutes>}" where {@code <minutes>} is a positive integer.
 * - Appends a new WorkoutGoalEntry to the provided EntryList.
 * - Saves the updated list via Storage.
 * <p>
 * Errors:
 * - Throws CommandException if parsing fails, minutes is non-positive, or storage is not initialized.
 */
public class SetWorkoutGoalCommand implements Command {
    private static final Logger logger = Logger.getLogger(SetWorkoutGoalCommand.class.getName());

    private final int minutesPerWeek;

    /**
     * Creates a command with a validated weekly goal.
     *
     * @param minutesPerWeek minutes per week; must be > 0
     */
    public SetWorkoutGoalCommand(int minutesPerWeek) {
        this.minutesPerWeek = minutesPerWeek;
    }

    /**
     * Parses a user input of the form "{@code workout goal <minutes>}" into a command.
     * Expects exactly three whitespace-separated tokens: "workout", "goal", and a positive integer.
     *
     * @param input full user input string
     * @return a SetWorkoutGoalCommand with the parsed minutes
     * @throws CommandException if usage is incorrect, value is non-positive, or not a whole number
     */
    public static SetWorkoutGoalCommand fromInput(String input) throws CommandException {
        logger.fine("Parsing SetWorkoutGoalCommand from input: " + input);
        // Expected: workout goal <minutes>
        String[] parts = input.split("\\s+");
        if (parts.length != 3) {
            logger.warning("Parse error: expected 3 tokens, got " + parts.length);
            throw new CommandException("Usage: workout goal <minutes>");
        }
        try {
            int minutes = Integer.parseInt(parts[2]);
            if (minutes <= 0) {
                logger.warning("Parse error: non-positive minutes=" + minutes);
                throw new CommandException("Workout goal must be a positive number of minutes.");
            }
            logger.info("Parsed SetWorkoutGoalCommand: minutes=" + minutes);
            return new SetWorkoutGoalCommand(minutes);
        } catch (NumberFormatException e) {
            logger.warning("Parse error: non-numeric minutes token: " + parts[2]);
            throw new CommandException("Workout goal must be specified as whole number minutes.");
        }
    }

    /**
     * Adds a WorkoutGoalEntry with the specified minutesPerWeek and saves the list.
     *
     * @param list    mutable list to append the new goal to
     * @param storage storage to persist the updated list; must not be null
     * @return a CommandResult confirming the new weekly goal
     * @throws CommandException if storage is not initialized or save fails
     */
    @Override
    public CommandResult execute(EntryList list, Storage storage) throws CommandException {
        logger.info("Executing SetWorkoutGoalCommand: minutes=" + minutesPerWeek);
        if (storage == null) {
            logger.severe("Storage is null in SetWorkoutGoalCommand.execute");
            throw new CommandException("Storage not initialized properly.");
        }

        list.add(new WorkoutGoalEntry(minutesPerWeek));
        logger.fine("WorkoutGoalEntry appended to EntryList");

        storage.save(list);
        logger.fine("EntryList saved after setting goal");

        return new CommandResult("Weekly workout goal set to " + minutesPerWeek + " minutes.");
    }
}
