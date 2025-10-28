package seedu.mama.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.model.WorkoutEntry;
import seedu.mama.storage.Storage;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddWorkoutCommandTest {

    private EntryList entries;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        entries = new EntryList();
        storage = new Storage(Path.of("build", "test-storage-workout.txt"));
    }

    @Test
    public void execute_validWorkout_addsEntryToList() throws CommandException {
        // include feel rating (e.g. 4)
        AddWorkoutCommand command = new AddWorkoutCommand("running", 40, 4);

        CommandResult result = command.execute(entries, storage);

        assertEquals(1, entries.size(), "One workout entry should be added");
        Entry first = entries.get(0);
        assertTrue(first instanceof WorkoutEntry, "Entry should be a WorkoutEntry");

        WorkoutEntry workout = (WorkoutEntry) first;
        assertEquals("running", workout.getWorkoutType());
        assertEquals(40, workout.getDuration());
        assertEquals(4, workout.getFeel());

        String line = first.toListLine();
        assertTrue(line.contains("running (40 mins, feel 4/5)"), line);
        assertTrue(result.getFeedbackToUser().toLowerCase().contains("logged")
                        || result.getFeedbackToUser().toLowerCase().contains("added"),
                "Result should confirm the workout was recorded");
    }

    @Test
    public void execute_multipleWorkouts_entriesIncrease() throws CommandException {
        AddWorkoutCommand first = new AddWorkoutCommand("yoga", 60, 3);
        AddWorkoutCommand second = new AddWorkoutCommand("cycling", 30, 5);

        first.execute(entries, storage);
        second.execute(entries, storage);

        assertEquals(2, entries.size(), "Two workout entries should be present");

        WorkoutEntry w1 = (WorkoutEntry) entries.get(0);
        WorkoutEntry w2 = (WorkoutEntry) entries.get(1);

        assertEquals("yoga", w1.getWorkoutType());
        assertEquals(60, w1.getDuration());
        assertEquals(3, w1.getFeel());

        assertEquals("cycling", w2.getWorkoutType());
        assertEquals(30, w2.getDuration());
        assertEquals(5, w2.getFeel());

        assertTrue(w1.toListLine().contains("feel 3/5"), "First workout should record feel 3/5");
        assertTrue(w2.toListLine().contains("feel 5/5"), "Second workout should record feel 5/5");
    }

    @Test
    public void execute_invalidFeel_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                        new AddWorkoutCommand("swim", 20, 6),
                "Feel > 5 should be invalid"
        );
        assertThrows(IllegalArgumentException.class, () ->
                        new AddWorkoutCommand("swim", 20, 0),
                "Feel < 1 should be invalid"
        );
    }
}
