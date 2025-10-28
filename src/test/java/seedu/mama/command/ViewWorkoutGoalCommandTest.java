package seedu.mama.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.mama.model.EntryList;
import seedu.mama.model.WorkoutEntry;
import seedu.mama.model.WorkoutGoalEntry;
import seedu.mama.storage.Storage;
import seedu.mama.util.DateTimeUtil;

import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewWorkoutGoalCommandTest {

    private EntryList list;
    private Storage storage;

    private static LocalDateTime mondayThisWeek() {
        return DateTimeUtil.weekStartMonday(LocalDateTime.now());
    }

    @BeforeEach
    public void setUp() {
        list = new EntryList();
        storage = new Storage(Path.of("build", "test-workout-goal-view.txt"));
    }

    @Test
    public void execute_noGoalThisWeek_showsReminderAndList() throws CommandException {
        // Add a goal last week → should not count as current
        LocalDateTime lastWeek = mondayThisWeek().minusDays(7).withHour(9).withMinute(0);
        list.add(new WorkoutGoalEntry(200, lastWeek));

        ViewWorkoutGoalCommand cmd = new ViewWorkoutGoalCommand();
        CommandResult res = cmd.execute(list, storage);
        String out = res.getFeedbackToUser();

        assertTrue(out.toLowerCase().contains("no workout goal set for this week"),
                "Should remind when no current-week goal.");
        assertTrue(out.contains("Workouts done this week"), "Should include weekly list header.");
    }

    @Test
    public void execute_goalThisWeekAndWorkouts_showsTotalsAndRemaining() throws CommandException {
        LocalDateTime weekStart = mondayThisWeek();
        // Goal: 150 this week
        list.add(new WorkoutGoalEntry(150, weekStart.withHour(9)));

        // Workouts this week: 40 + 25 = 65
        list.add(new WorkoutEntry("run", 40, 4, weekStart.plusDays(1).withHour(7)));   // Tue
        list.add(new WorkoutEntry("swim", 25, 3, weekStart.plusDays(3).withHour(18))); // Thu

        ViewWorkoutGoalCommand cmd = new ViewWorkoutGoalCommand();
        CommandResult res = cmd.execute(list, storage);
        String out = res.getFeedbackToUser();

        assertTrue(out.contains("The weekly goal you set for the week is: 150 minutes."),
                "Should display the current week's goal.");
        // Be tolerant to phrasing; just check the core numbers appear.
        assertTrue(out.contains("You have completed 65"), "Should show completed minutes = 65.");
        assertTrue(out.contains("150"), "Should mention target 150.");
        assertTrue(out.contains("Workouts done this week"), "Should list workouts.");
        assertTrue(out.contains("[Workout] run (40 mins)"), "Should list 'run'.");
        assertTrue(out.contains("[Workout] swim (25 mins)"), "Should list 'swim'.");
    }

    @Test
    public void execute_goalLastWeek_ignoredThisWeek() throws CommandException {
        LocalDateTime weekStart = mondayThisWeek();

        // Goal set last week should not be treated as current.
        list.add(new WorkoutGoalEntry(120, weekStart.minusDays(7).withHour(10)));

        // Add a workout this week anyway
        list.add(new WorkoutEntry("cycle", 30, 5, weekStart.plusDays(2).withHour(6)));

        ViewWorkoutGoalCommand cmd = new ViewWorkoutGoalCommand();
        CommandResult res = cmd.execute(list, storage);
        String out = res.getFeedbackToUser();

        assertTrue(out.toLowerCase().contains("no workout goal set for this week"),
                "Last week's goal should not be considered current.");
        assertTrue(out.contains("[Workout] cycle (30 mins)"), "Should still list this week's workout.");
    }
}
