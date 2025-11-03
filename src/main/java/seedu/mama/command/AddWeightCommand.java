package seedu.mama.command;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.model.WeightEntry;
import seedu.mama.storage.Storage;

/**
 * Represents a command that adds a new weight entry to the list.
 */
public class AddWeightCommand implements Command {
    /** The weight value entered by the user (in kilograms). */
    private final double weightInput;

    /**
     * Constructs a new AddWeightCommand with the specified weight input.
     *
     * @param weightInput The user's weight input.
     * @throws CommandException If the input weight is not greater than zero.
     */
    public AddWeightCommand(double weightInput) throws CommandException {
        if (weightInput <= 0) {
            throw new CommandException("weightInput must be greater that 0!");
        }
        weightInput = roundToTwoDecimalPlaces(weightInput);
        this.weightInput = weightInput;
    }

    /**
     * Rounds a double value to two decimal places.
     *
     * @param value The value to round.
     * @return The value rounded to two decimal places.
     */
    public static double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /**
     * Adds a WeightEntry to the list, persists the list if storage is provided,
     *
     * @param list    mutable list to append the new entry to
     * @param storage optional storage; if non-null, list is saved
     * @return command feedback for the user
     * @throws CommandException if execution fails (e.g., storage error)
     */
    @Override
    public CommandResult execute(EntryList list, Storage storage) {
        // Use an assertion to check for internal errors
        // confirms our assumption that weight entries should never be null
        assert this.weightInput > 0 : "The weight input must be greater than 0!";

        Entry newWeight = new WeightEntry(this.weightInput);
        list.add(newWeight);
        if (storage != null) {
            storage.save(list);
        }
        return new CommandResult("Added new weight entry: " + newWeight.toListLine());
    }
}
