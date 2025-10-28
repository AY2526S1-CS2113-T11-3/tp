package seedu.mama.command;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.storage.Storage;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteCommand implements Command {
    private static final Logger LOG = Logger.getLogger(DeleteCommand.class.getName());
    private final int indexOneBased;

    public DeleteCommand(int indexOneBased) {
        if (indexOneBased <= 0) {
            throw new IllegalArgumentException("indexOneBased must be greater than 0");
        }
        this.indexOneBased = indexOneBased;
    }

    @Override
    public CommandResult execute(EntryList list, Storage storage) throws CommandException {
        Objects.requireNonNull(list, "EntryList is null");
        Objects.requireNonNull(storage, "Storage is null");
        assert list.shownSize() >= 0 : "Shown size must be non-negative";

        // Validate against the SHOWN view, not the full list.
        if (indexOneBased > list.shownSize()) {
            LOG.warning(() -> "Invalid delete index (shown view): " + indexOneBased);
            throw new CommandException("Invalid delete index (shown view): " +
                    indexOneBased + System.lineSeparator() + previewShown(list));
        }

        int zeroBasedShown = indexOneBased - 1;

        Entry removed = list.deleteByShownIndex(zeroBasedShown);
        try {
            storage.save(list);
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Failed to persist after delete index=" + indexOneBased, e);
            throw new CommandException("Failed to save after delete", e);
        }
        LOG.info(() -> "Deleted (shown view) index " + indexOneBased + ": " + removed.toListLine());

        return new CommandResult("Deleted: " + removed.toListLine(), false);
    }


    private static String previewShown(EntryList list) {
        if (list.shownSize() == 0) {
            // You can keep whatever you like here; the failing test doesn't hit this path.
            return "Here are your entries:\n(none)";
        }
        StringBuilder sb = new StringBuilder("Here are your entries:");
        for (int i = 0; i < list.shownSize(); i++) {
            sb.append(System.lineSeparator()).append(i + 1).append(". ").append(list.getShown(i).toListLine());
        }
        return sb.toString();
    }
}
