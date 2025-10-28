package seedu.mama.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.model.MealEntry;
import seedu.mama.model.WorkoutEntry;
import seedu.mama.storage.Storage;


import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListCommandTest {

    private EntryList entries;
    private Storage storageStub;

    /**
     * A simple stub for the Storage class that does nothing.
     * It's used to provide a non-null Storage object to commands during testing,
     * satisfying assertions without needing a real storage file.
     */
    private static class StorageStub extends Storage {
        public StorageStub() {
            super(null);
        }
        // This stub class is empty because the ListCommand does not call any Storage methods.
        // It only needs a non-null Storage object to exist.
    }

    @BeforeEach
    public void setUp() {
        entries = new EntryList();
        storageStub = new StorageStub();
    }

    @Test
    public void execute_emptyList_returnsNoEntriesMessage() throws CommandException {
        ListCommand command = new ListCommand();
        CommandResult result = command.execute(entries, storageStub);

        assertEquals("No entries found.", result.getFeedbackToUser());
    }

    @Test
    public void execute_multipleEntries_returnsNumberedList() throws CommandException {
        entries.add(new MealEntry("Chicken Rice", 500));
        entries.add(new WorkoutEntry("Evening Run", 300, 4));

        ListCommand command = new ListCommand();
        CommandResult result = command.execute(entries, storageStub);

        assertTrue(result.getFeedbackToUser().startsWith("Here are your entries:"));

        String[] lines = result.getFeedbackToUser().split(System.lineSeparator());
        assertEquals(3, lines.length, "Output should have a header and two entry lines");
        assertTrue(lines[1].startsWith("1. "));
        assertTrue(lines[2].startsWith("2. "));
        assertTrue(lines[1].contains("Chicken Rice"));
        assertTrue(lines[2].contains("Evening Run"));
    }

    @Test
    public void execute_filterByType_returnsFilteredList() throws CommandException {
        entries.add(new MealEntry("Salad", 350));
        entries.add(new WorkoutEntry("Morning Yoga", 150, 5));
        entries.add(new MealEntry("Noodles", 600));

        Predicate<Entry> mealPredicate = entry -> entry instanceof MealEntry;
        ListCommand command = new ListCommand(mealPredicate, "meal");
        CommandResult result = command.execute(entries, storageStub);

        assertTrue(result.getFeedbackToUser().contains("Here are your meal entries:"));
        assertTrue(result.getFeedbackToUser().contains("Salad"));
        assertTrue(result.getFeedbackToUser().contains("Noodles"));
        assertFalse(result.getFeedbackToUser().contains("Morning Yoga"), "Workout entry should not be in the list");

        String[] lines = result.getFeedbackToUser().split(System.lineSeparator());
        assertEquals(3, lines.length);
        assertTrue(lines[1].startsWith("1. "));
        assertTrue(lines[2].startsWith("2. "));
    }
}
