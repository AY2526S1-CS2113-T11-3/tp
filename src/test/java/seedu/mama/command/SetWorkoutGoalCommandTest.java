package seedu.mama.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.mama.model.Entry;
import seedu.mama.model.EntryList;
import seedu.mama.model.WorkoutGoalEntry;
import seedu.mama.storage.Storage;
import seedu.mama.util.DateTimeUtil;

import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for workout goal feature:
 * - SetWorkoutGoalCommand parsing + execution
 * - Integration with AddWorkoutCommand (reminders, remaining minutes)
 */
public class SetWorkoutGoalCommandTest {

    private EntryList list;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        list = new EntryList();
        storage = new Storage(Path.of("build", "test-workout-goal-feature.txt"));
    }

    @Test
    public void execute_validGoal_addsWorkoutGoalEntry() throws CommandException {
        SetWorkoutGoalCommand cmd = new SetWorkoutGoalCommand(150);
        CommandResult result = cmd.execute(list, storage);

        assertNotNull(result);
        assertTrue(result.getFeedbackToUser().contains("Weekly workout goal set to 150 minutes."));
        assertEquals(1, list.size());
        Entry e = list.get(0);
        assertEquals("WORKOUT_GOAL", e.type());
        assertTrue(e instanceof WorkoutGoalEntry);
        assertEquals(150, ((WorkoutGoalEntry) e).getMinutesPerWeek());
    }

    @Test
    public void fromInput_validString_parsesCorrectly() throws CommandException {
        SetWorkoutGoalCommand cmd = SetWorkoutGoalCommand.fromInput("workout goal 200");
        CommandResult result = cmd.execute(list, storage);

        assertTrue(result.getFeedbackToUser().contains("200"));
        assertEquals(1, list.size());
        assertEquals(200, ((WorkoutGoalEntry) list.get(0)).getMinutesPerWeek());
    }

    @Test
    public void fromInput_missingMinutes_throws() {
        assertThrows(CommandException.class,
                () -> SetWorkoutGoalCommand.fromInput("workout goal"));
    }

    @Test
    public void fromInput_nonNumeric_throws() {
        assertThrows(CommandException.class,
                () -> SetWorkoutGoalCommand.fromInput("workout goal abc"));
    }

    @Test
    public void fromInput_nonPositive_throws() {
        assertThrows(CommandException.class,
                () -> SetWorkoutGoalCommand.fromInput("workout goal 0"));
        assertThrows(CommandException.class,
                () -> SetWorkoutGoalCommand.fromInput("workout goal -50"));
    }

    @Test
    public void addWorkout_noGoal_showsReminder() throws CommandException {
        // include neutral feel = 3 for constructor
        AddWorkoutCommand cmd = new AddWorkoutCommand("running", 40, 3);

        CommandResult res = cmd.execute(list, storage);
        String out = res.getFeedbackToUser().toLowerCase();

        assertTrue(out.contains("reminder") && out.contains("workout goal"),
                "Should remind that weekly workout goal is not set.");
        assertEquals(1, list.size(), "Workout entry should still be added");
        assertTrue(out.contains("40"), "Should mention duration in output");
    }

    @Test
    public void addWorkout_withGoal_showsRemaining() throws CommandException {
        // Set a weekly goal for THIS week
        LocalDateTime weekStart = DateTimeUtil.weekStartMonday(LocalDateTime.now());
        list.add(new WorkoutGoalEntry(100, weekStart.withHour(8))); // 100-minute goal

        // Add a 30-min workout with feel = 4
        AddWorkoutCommand cmd = new AddWorkoutCommand("yoga", 30, 4);
        CommandResult res = cmd.execute(list, storage);
        String out = res.getFeedbackToUser();

        assertTrue(out.contains("30"), "Should mention the 30-minute workout.");
        assertTrue(out.contains("100"), "Should mention the 100-minute goal.");
        assertTrue(out.toLowerCase().contains("minutes left") || out.toLowerCase().contains("more minutes"),
                "Should show remaining minutes toward the goal.");
        assertTrue(out.contains("feel 4/5"), "Should include feel rating in feedback");

        // Now list should have 1 goal + 1 workout
        assertEquals(2, list.size());
    }
}
