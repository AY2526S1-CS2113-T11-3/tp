package seedu.mama.command;

import seedu.mama.model.DashboardSummary;
import seedu.mama.model.EntryList;
import seedu.mama.storage.Storage;
import seedu.mama.ui.DashboardFormatter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Displays a combined health dashboard by orchestrating data gathering and formatting.
 */
public class DashboardCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(DashboardCommand.class.getName());

    @Override
    public CommandResult execute(EntryList list, Storage storage) {
        // Assertions for non-null inputs
        assert list != null : "EntryList cannot be null";
        assert storage != null : "Storage cannot be null";

        LOGGER.log(Level.INFO, "Executing DashboardCommand.");

        // 1. Let the model layer gather and calculate data.
        DashboardSummary summary = new DashboardSummary(list, storage);

        // 2. Let the UI layer format the data for display.
        DashboardFormatter formatter = new DashboardFormatter();
        String dashboardText = formatter.format(summary);

        LOGGER.log(Level.INFO, "DashboardCommand executed successfully.");
        // 3. Return the result to be printed by the main loop.
        return new CommandResult(dashboardText);
    }
}
