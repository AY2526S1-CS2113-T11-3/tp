package seedu.mama.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.mama.model.EntryList;
import seedu.mama.model.WorkoutEntry;
import seedu.mama.storage.Storage;

/**
 * Tests for {@link DeleteCommand}.
 * Verifies valid deletion, invalid indices (with preview), and constructor guard.
 */
public class DeleteCommandTest {

    private EntryList list;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        list = new EntryList();
        list.add(new WorkoutEntry("Run", 30, 4));
        list.add(new WorkoutEntry("Swim", 30, 3));
        list.add(new WorkoutEntry("Cycle", 30, 5));

        // Use a temp test file under build/
        storage = new Storage(Path.of("build", "test-storage.txt"));
    }

    @Test
    public void execute_validIndex_deletesCorrectEntry() throws CommandException {
        DeleteCommand cmd = new DeleteCommand(2); // delete "Swim"

        CommandResult output = cmd.execute(list, storage);

        // list size reduced
        assertEquals(2, list.size());

        // feedback looks right
        String msg = output.getFeedbackToUser();
        assertFalse(msg.isEmpty());
        assertTrue(msg.startsWith("Deleted:"), "Should start with 'Deleted:'");
        assertTrue(msg.contains("[Workout] Swim (30 mins, feel 3/5)"),
                "Feedback should include the deleted entry with feel rating");

        // remaining entries in order
        String first = list.get(0).toListLine();
        String second = list.get(1).toListLine();
        assertTrue(first.contains("Run") && first.contains("feel 4/5"), first);
        assertTrue(second.contains("Cycle") && second.contains("feel 5/5"), second);
    }

    @Test
    public void execute_invalidIndex_showsPreview() {
        // shown size is 3, so 5 is OOB
        DeleteCommand cmd = new DeleteCommand(5);

        CommandException ex = assertThrows(
                CommandException.class,
                () -> cmd.execute(list, storage)
        );

        String msg = ex.getMessage();
        // New format used by DeleteCommand
        assertTrue(msg.startsWith("Index 5 is out of bounds (shown list)."),
                "Message should start with 'Index 5 is out of bounds (shown list).' but was:\n" + msg);
        assertTrue(msg.contains("Here are your entries:"),
                "Message should include preview list:\n" + msg);
        assertTrue(msg.contains("1. "),
                "Message should enumerate entries (contain '1. '):\n" + msg);

        // list remains unchanged
        assertEquals(3, list.size(), "List should remain unchanged");
    }

    @Test
    public void execute_emptyShownList_throwsUsageMessage() {
        // Clear the list so shown is empty
        // (You can either clear via internal API or recreate)
        list = new EntryList(); // fresh, empty shown view
        DeleteCommand cmd = new DeleteCommand(1);

        CommandException ex = assertThrows(
                CommandException.class,
                () -> cmd.execute(list, storage)
        );

        String msg = ex.getMessage();
        assertTrue(msg.contains("There are no items to delete. The shown list is empty."),
                "Empty-state message missing:\n" + msg);
        assertTrue(msg.contains(DeleteCommand.MESSAGE_USAGE),
                "Usage block should be appended:\n" + msg);
    }

    @Test
    public void constructor_illegalIndex_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> new DeleteCommand(0));
        assertThrows(IllegalArgumentException.class, () -> new DeleteCommand(-2));
    }
}
