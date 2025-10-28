package seedu.mama.command;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.storage.Storage;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lists entries to the user, either all or filtered by a specific criteria.
 */
public class ListCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(ListCommand.class.getName());

    private final Predicate<Entry> predicate; // null => show all
    private final String displayType;

    /**
     * Shows all entries.
     */
    public ListCommand() {
        this.predicate = null;   // persist "show all"
        this.displayType = "all";
    }

    /**
     * Shows entries matching the given predicate.
     */
    public ListCommand(Predicate<Entry> predicate, String displayType) {
        assert displayType != null && !displayType.isEmpty() : "Display type cannot be null or empty";
        this.predicate = predicate; // can be non-null
        this.displayType = displayType;
    }

    @Override
    public CommandResult execute(EntryList entries, Storage storage) throws CommandException {
        assert entries != null : "The EntryList object passed to ListCommand should not be null.";
        assert storage != null : "The Storage object passed to ListCommand should not be null.";

        LOGGER.log(Level.INFO, "Executing ListCommand with filter for: " + displayType);

        // Persist the filter as the "last shown" view.
        entries.setFilter(predicate);

        // Build the message from the persisted shown view to match indices the user sees.
        List<Entry> shown = entries.getShownSnapshot();
        String headerType = displayType.equals("all") ? "entries" : displayType + " entries";

        if (shown.isEmpty()) {
            LOGGER.log(Level.INFO, "No entries found for type: " + displayType);
            return new CommandResult("No " + headerType + " found.");
        }

        StringBuilder sb = new StringBuilder("Here are your " + headerType + ":");
        for (int i = 0; i < shown.size(); i++) {
            sb.append(System.lineSeparator())
                    .append(i + 1)
                    .append(". ")
                    .append(shown.get(i).toListLine());
        }

        LOGGER.log(Level.INFO, "Successfully listed " + shown.size() + " entries of type: " + displayType);
        return new CommandResult(sb.toString().trim());
    }
}
