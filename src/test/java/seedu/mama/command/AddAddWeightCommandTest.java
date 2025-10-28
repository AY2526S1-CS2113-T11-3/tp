package seedu.mama.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.mama.model.EntryList;
import seedu.mama.model.WeightEntry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AddAddWeightCommandTest {
    private EntryList entries;


    @BeforeEach
    public void setUp() {
        entries = new EntryList();
    }

    @Test
    public void execute_validWeight_addsEntryToList() throws CommandException {
        AddWeightCommand command = new AddWeightCommand(80);
        CommandResult result = command.execute(entries, null);

        assertEquals(1, entries.size());
        assertEquals("80kg", ((WeightEntry) entries.get(0)).getWeight());
        assertTrue(result.getFeedbackToUser().contains("Added new weight entry"));
        assertTrue(result.getFeedbackToUser().contains("80"));
    }

    @Test
    public void execute_multipleWeight_entriesIncrease() throws CommandException {
        AddWeightCommand first = new AddWeightCommand(100);
        AddWeightCommand second = new AddWeightCommand(50);
        first.execute(entries, null);
        second.execute(entries, null);

        assertEquals(2, entries.size());
    }
}
