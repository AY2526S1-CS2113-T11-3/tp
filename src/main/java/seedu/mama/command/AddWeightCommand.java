package seedu.mama.command;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.model.WeightEntry;
import seedu.mama.storage.Storage;

/**
 * Adds the user weight to the list and returns the user's weight in kg
 */
public class AddWeightCommand implements Command {
    private final int weightInput;

    // weight must be a positive number
    // if not positive, we throw an exception handled by calling code
    public AddWeightCommand(int weightInput) throws CommandException {
        if (weightInput <= 0) {
            throw new CommandException("weightInput must be greater that 0!");
        }
        this.weightInput = weightInput;
    }

    private static String preview(EntryList list) {
        if (list.size() == 0) {
            return "No entries yet.";
        }
        StringBuilder sb = new StringBuilder("Your past weights:");
        for (int i = 0; i < list.size(); i++) {
            Entry e = list.get(i);
            if (e.type().equals("WEIGHT")) {
                sb.append(System.lineSeparator()).append(e.toListLine());
            }
        }
        return sb.toString();
    }

    @Override
    public CommandResult execute(EntryList list, Storage storage) {
        // Use an assertion to check for internal errors
        // confirms our assumption that weight entries should never be null
        assert this.weightInput > 0 : "The weight input must be greater than 0!";

        Entry newWeight = new WeightEntry(weightInput + "kg");
        list.add(newWeight);
        if (storage != null) {
            storage.save(list);
        }
        return new CommandResult("Added new weight entry: " + newWeight.toListLine());
    }
}
