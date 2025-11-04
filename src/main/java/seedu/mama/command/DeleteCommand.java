package seedu.mama.command;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.storage.Storage;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deletes an entry from the currently shown list by its index.
 *
 * <p>The index is validated against the filtered (shown) view, not the full backing list.
 * Invalid indices (zero, negatives, or greater than shown size) will result in a {@link CommandException}.</p>
 */
public class DeleteCommand implements Command {

    /**
     * Usage string for help/invalid syntax messages.
     */
    public static final String MESSAGE_USAGE = "Usage: delete INDEX\n"
            + "Deletes the entry at INDEX from the currently shown list.\n"
            + "• INDEX must be a positive whole number (1, 2, 3, ...).";

    private static final Logger LOG = Logger.getLogger(DeleteCommand.class.getName());

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
        if (indexOneBased <= 0) {
            throw new IllegalArgumentException("indexOneBased must be greater than 0");
        }
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
        String[] parts = trimmed.split("\\s+", 2);

        if (parts.length < 2 || parts[1].isBlank()) {
            return (l, s) -> new CommandResult(withUsage("Missing index."));
        }

        String arg = parts[1].trim();

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
            return (l, s) -> new CommandResult(withUsage("Index is too large."));
        }
    }

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

        // Out-of-bounds (print the current SHOWN list)
        if (indexOneBased <= 0 || indexOneBased > shownSize) {
            LOG.info(() -> "Delete index out of bounds (shown list): " + indexOneBased + " / size=" + shownSize);
            String reason = String.format("Index %d is out of bounds (shown list). %s",
                    indexOneBased, formatValidRange(shownSize));
            throw new CommandException(reasonWithPreview(reason, list));
        }

        final int zeroBasedShown = indexOneBased - 1;

        try {
            Entry removed = list.deleteByShownIndex(zeroBasedShown);
            storage.save(list);

            LOG.info(() -> "Deleted (shown view) index " + indexOneBased + ": " + removed.toListLine());
            return new CommandResult("Deleted: " + removed.toListLine(), false);

        } catch (IndexOutOfBoundsException e) {
            // Filter/view changed mid-execution: re-check and print preview
            int sizeNow = list.shownSize();
            LOG.info(() -> "Delete index went out of range during execution: "
                    + indexOneBased + " / size=" + sizeNow);
            if (sizeNow == 0) {
                throw new CommandException(withUsage("There are no items to delete. The shown list is empty."));
            }
            String reason = String.format("Index %d is out of bounds (shown list). %s",
                    indexOneBased, formatValidRange(sizeNow));
            throw new CommandException(reasonWithPreview(reason, list));
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Failed to persist after delete index=" + indexOneBased, e);
            throw new CommandException(withUsage(
                    "Failed to save updated data to disk. Please check your file permissions or try again."
            ), e);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Helpers (messages + shown preview)
    // ─────────────────────────────────────────────────────────────────────

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

    /**
     * Builds the final reason + usage + shown-list preview string for OOB cases.
     */
    private static String reasonWithPreview(String reason, EntryList list) {
        return withUsage(reason) +
                "\n" +
                previewShown(list);
    }

    /**
     * Pretty-prints the current SHOWN list exactly as the user sees it.
     */
    private static String previewShown(EntryList list) {
        int n = list.shownSize();
        if (n == 0) {
            return "Here are your entries:\n(none)";
        }
        StringBuilder sb = new StringBuilder("Here are your entries:");
        for (int i = 0; i < n; i++) {
            sb.append("\n").append(i + 1).append(". ").append(list.getShown(i).toListLine());
        }
        return sb.toString();
    }
}
