package seedu.mama.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.mama.model.EntryList;
import seedu.mama.testutil.TestStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddMeasurementCommandTest {

    private EntryList list;
    private TestStorage.Spy storage;

    @BeforeEach
    public void setUp() {
        list = new EntryList();
        storage = new TestStorage.Spy();
    }

    @Test
    void execute_minimal_addsEntryAndSaves() throws Exception {
        AddMeasurementCommand cmd = new AddMeasurementCommand(75, 95, null, null, null);
        CommandResult res = cmd.execute(list, storage);

        assertTrue(res.getFeedbackToUser().startsWith("Added"));
        assertEquals(1, list.size());
        assertTrue(storage.saved);
        String line = list.get(0).toListLine();
        assertTrue(line.contains("waist=75cm"));
        assertTrue(line.contains("hips=95cm"));
    }

    @Test
    void execute_withOptionals_allPresent() throws Exception {
        AddMeasurementCommand cmd = new AddMeasurementCommand(76, 96, 88, 55, 30);
        cmd.execute(list, storage);
        String line = list.get(0).toListLine();
        assertTrue(line.contains("chest=88cm"));
        assertTrue(line.contains("thigh=55cm"));
        assertTrue(line.contains("arm=30cm"));
    }

    @Test
    void constructor_missingRequired_throws() {
        assertThrows(CommandException.class, () -> new AddMeasurementCommand(null, 95, null, null, null));
        assertThrows(CommandException.class, () -> new AddMeasurementCommand(75, null, null, null, null));
    }

    @Test
    void constructor_invalidValues_throws() {
        assertThrows(CommandException.class, () -> new AddMeasurementCommand(0, 95, null, null, null));
        assertThrows(CommandException.class, () -> new AddMeasurementCommand(75, 95, -1, null, null));
        assertThrows(CommandException.class, () -> new AddMeasurementCommand(75, 95, null, -5, null));
        assertThrows(CommandException.class, () -> new AddMeasurementCommand(75, 95, null, null, 0));
    }
}
