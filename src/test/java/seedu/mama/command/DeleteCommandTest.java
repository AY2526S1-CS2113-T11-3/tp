package seedu.mama.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.mama.model.EntryList;
import seedu.mama.model.WorkoutEntry;
import seedu.mama.storage.Storage;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link DeleteCommand}.
 * Verifies valid deletion, invalid indices, and defensive constructor.
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

        storage = new Storage(Path.of("build", "test-storage.txt"));
    }

    @Test
    public void execute_validIndex_deletesCorrectEntry() throws CommandException {
        DeleteCommand cmd = new DeleteCommand(2);

        CommandResult output = cmd.execute(list, storage);

        assertEquals(2, list.size());
        assertFalse(output.getFeedbackToUser().isEmpty());
        assertTrue(output.getFeedbackToUser().startsWith("Deleted:"), "Should start with 'Deleted:'");
        assertTrue(output.getFeedbackToUser().contains("[Workout] Swim (30 mins, feel 3/5)"),
                "Feedback should include the deleted entry with feel rating");

        String first = list.get(0).toListLine();
        String second = list.get(1).toListLine();
        assertTrue(first.contains("Run") && first.contains("feel 4/5"), first);
        assertTrue(second.contains("Cycle") && second.contains("feel 5/5"), second);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        DeleteCommand cmd = new DeleteCommand(5); // out of bounds

        CommandException ex = assertThrows(
                CommandException.class,
                () -> cmd.execute(list, storage)
        );

        assertTrue(ex.getMessage().startsWith("Invalid delete index (shown view): 5"));
        assertTrue(ex.getMessage().contains("Here are your entries:"));
        assertEquals(3, list.size(), "List should remain unchanged");
    }

    @Test
    public void constructor_illegalIndex_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> new DeleteCommand(-2));
        assertThrows(IllegalArgumentException.class, () -> new DeleteCommand(0));
    }
}
