package seedu.mama.command;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.storage.Storage;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deletes an entry from the currently shown list by its index.
 * <p>
 * The index is validated against the filtered (shown) view, not the full backing list.
 * Invalid indices, including zero, negatives, or numbers greater than the shown size,
 * will result in a {@link CommandException}.
 */
public class DeleteCommand implements Command {

    private static final Logger LOG = Logger.getLogger(DeleteCommand.class.getName());

    /**
     * Usage string for help/invalid syntax messages.
     */
    public static final String MESSAGE_USAGE = "Usage: delete INDEX\n" + "Deletes the entry at INDEX from the currently shown list.\n" + "• INDEX must be a positive whole number (1, 2, 3, ...).";

    /**
     * One-based index of the entry to delete in the shown list.
     */
    private final int indexOneBased;

    /**
     * Creates a {@code DeleteCommand} for the given one-based index in the shown list.
     *
     * @param indexOneBased one-based index (1..N) into the currently shown list
     */
    public DeleteCommand(int indexOneBased) {
        this.indexOneBased = indexOneBased;
    }

    /**
     * Parses raw user input containing {@code delete} and returns a {@link Command}.
     * Performs only syntax/format checks (missing index, non-numeric, zero/negative, overflow).
     * Range checks against the shown list occur in {@link #execute(EntryList, Storage)}.
     *
     * @param trimmed full user input, e.g. {@code "delete 3"}
     * @return a {@code DeleteCommand} if arguments are valid; otherwise a command that prints usage
     */
    public static Command fromInput(String trimmed) {
        // split "delete ..." into ["delete", "<arg>"] (at most 2 parts)
        String[] parts = trimmed.split("\\s+", 2);

        // Missing index
        if (parts.length < 2 || parts[1].isBlank()) {
            return (l, s) -> new CommandResult(withUsage("Missing index."));
        }

        String arg = parts[1].trim();

        // Non-numeric (strict digits only)
        if (!arg.matches("\\d+")) {
            return (l, s) -> new CommandResult(withUsage("Index must be a positive whole number."));
        }

        try {
            int idx = Integer.parseInt(arg);
            if (idx <= 0) {
                return (l, s) -> new CommandResult(withUsage("Index must be greater than 0."));
            }
            return new DeleteCommand(idx);
        } catch (NumberFormatException e) {
            // Extremely large number (overflow beyond int)
            return (l, s) -> new CommandResult(withUsage("Index is too large."));
        }
    }

    /**
     * Executes the delete operation against the provided model and persists the result.
     *
     * <p>Validates that the shown list is not empty and the index is within range {@code [1..shownSize]}.
     * On success, deletes the target entry and saves via {@link Storage#save(EntryList)}.</p>
     *
     * @param list    the {@link EntryList} containing entries (non-null)
     * @param storage the {@link Storage} used to persist changes (non-null)
     * @return a {@link CommandResult} describing the outcome
     * @throws CommandException if the shown list is empty, the index is out of bounds, or persistence fails
     */
    @Override
    public CommandResult execute(EntryList list, Storage storage) throws CommandException {
        Objects.requireNonNull(list, "EntryList is null");
        Objects.requireNonNull(storage, "Storage is null");
        assert list.shownSize() >= 0 : "Shown size must be non-negative";

        final int shownSize = list.shownSize();

        // Empty shown view
        if (shownSize == 0) {
            LOG.info("Delete attempted on empty shown list.");
            throw new CommandException(withUsage("There are no items to delete. The shown list is empty."));
        }

        // Out-of-bounds (0/negative handled in fromInput; keep for defense)
        if (indexOneBased <= 0 || indexOneBased > shownSize) {
            LOG.info(() -> "Delete index out of bounds (shown list): " + indexOneBased + " / size=" + shownSize);
            throw new CommandException(withUsage(String.format("Index %d is out of bounds (shown list). %s", indexOneBased, formatValidRange(shownSize))));
        }

        final int zeroBasedShown = indexOneBased - 1;

        try {
            Entry removed = list.deleteByShownIndex(zeroBasedShown);
            storage.save(list);

            LOG.info(() -> "Deleted (shown view) index " + indexOneBased + ": " + removed.toListLine());
            return new CommandResult("Deleted: " + removed.toListLine(), false);

        } catch (IndexOutOfBoundsException e) {
            // Filter/view changed mid-execution
            int sizeNow = list.shownSize();
            LOG.info(() -> "Delete index went out of range during execution: " + indexOneBased + " / size=" + sizeNow);
            if (sizeNow == 0) {
                throw new CommandException(withUsage("There are no items to delete. The shown list is empty."));
            }
            throw new CommandException(withUsage(String.format("Index %d is out of bounds (shown list). %s", indexOneBased, formatValidRange(sizeNow))));
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Failed to persist after delete index=" + indexOneBased, e);
            throw new CommandException(withUsage("Failed to save updated data to disk. Please check your file permissions or try again."), e);
        }
    }

    /**
     * Formats the valid range sentence.
     */
    private static String formatValidRange(int shownSize) {
        return (shownSize == 1) ? "Valid index: 1." : "Valid range: 1.." + shownSize + ".";
    }

    /**
     * Appends usage with a single newline.
     */
    private static String withUsage(String reason) {
        return reason + "\n" + MESSAGE_USAGE;
    }
}
