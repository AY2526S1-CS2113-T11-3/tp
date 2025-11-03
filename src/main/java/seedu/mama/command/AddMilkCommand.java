package seedu.mama.command;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.model.MilkEntry;
import seedu.mama.storage.Storage;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adds how much breast milk has been pumped and returns the user
 * the amount of breast milk pumped in ml
 */
public class AddMilkCommand implements Command {
    // 1. Set up logger for this class
    private static final Logger LOG = Logger.getLogger(AddMilkCommand.class.getName());
    private static final int MAX_REASONABLE_VOLUME = 1000;

    public final int milkVolume;

    /**
     * Constructs an AddMilkCommand with a specified milk volume in ml.
     * @param milkVolume the milk volume in ml
     * @throws CommandException if the milk volume is invalid
     */
    public AddMilkCommand(int milkVolume) throws CommandException {
        if (milkVolume < 0) {
            throw new CommandException("milkVolume must be a positive number!");
        }
        if (milkVolume > MAX_REASONABLE_VOLUME) {
            throw new CommandException("milkVolume must be a realistic value! " +
                    "(less than " + MAX_REASONABLE_VOLUME + ")");
        }
        this.milkVolume = milkVolume;
    }

    /**
     * Parses user input and returns a new AddMilkCommand.
     * Expected format: "milk volume"
     */
    public static AddMilkCommand fromInput(String input) throws CommandException {
        String desc = input.substring("milk".length()).trim();

        if (desc.isEmpty()) {
            throw new CommandException("Please specify the milk volume in ml. Example: 'milk 120'");
        }

        try {
            // Use BigInteger to safely handle large numbers before converting
            int milkVolume = getMilkVolume(desc);
            return new AddMilkCommand(milkVolume);

        } catch (NumberFormatException e) {
            throw new CommandException("Volume must be an actual number! Example: 'milk 150'");
        }
    }

    private static int getMilkVolume(String desc) throws CommandException {
        BigInteger bigInt = new BigInteger(desc);

        if (bigInt.compareTo(BigInteger.ZERO) <= 0) {
            throw new CommandException("Milk volume must be a positive number!");
        }
        if (bigInt.compareTo(BigInteger.valueOf(MAX_REASONABLE_VOLUME)) > 0) {
            throw new CommandException("Milk volume too large! Please enter a realistic value (less than "
                    + MAX_REASONABLE_VOLUME + " ml).");
        }

        return bigInt.intValue();
    }

    @Override
    public CommandResult execute(EntryList list, Storage storage) {
        // Log the start of the execution
        LOG.log(Level.INFO, "Executing AddMilkCommand.");

        // Use assertion to check for internal errors
        // Confirms that milkVolume is greater than 0
        assert this.milkVolume > 0 : "The milkVolume must be greater than 0!";

        // increment the total milk volume everytime
        MilkEntry.addTotalMilkVol(milkVolume);

        Entry newMilk = new MilkEntry(milkVolume + "ml");
        list.add(newMilk);
        if (storage != null) {
            storage.save(list);
        }

        LOG.log(Level.INFO, "AddMilkCommand successfully executed, adding: " + milkVolume + "ml");
        return new CommandResult("Breast Milk Pumped: " + newMilk.toListLine() +
                "\n" + MilkEntry.toTotalMilk());
    }
}
