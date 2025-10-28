package seedu.mama.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.mama.model.EntryList;
import seedu.mama.model.MealEntry;
import seedu.mama.model.MilkEntry;
import seedu.mama.model.WorkoutEntry;
import seedu.mama.storage.Storage;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardCommandTest {

    private EntryList entries;
    private Storage storageStub;

    private static class StorageStub extends Storage {
        public StorageStub() {
            super(null);
        }

        // For this test, we want to simulate that no goal is loaded.
        @Override
        public Integer loadGoal() {
            return null; // Return null to simulate no goal being set.
        }
    }

    @BeforeEach
    public void setUp() {
        entries = new EntryList();
        storageStub = new StorageStub();
    }

    @Test
    public void execute_dashboardCommand_displaysAllSections() throws CommandException {
        // Arrange: Add some data
        entries.add(new MealEntry("Lunch", 500));
        entries.add(new MilkEntry("120ml", LocalDateTime.now()));
        entries.add(new WorkoutEntry("Run", 30, 4, LocalDateTime.now()));

        // Act
        DashboardCommand command = new DashboardCommand();
        CommandResult result = command.execute(entries, storageStub);
        String output = result.getFeedbackToUser();

        // Assert
        assertTrue(output.contains("--- Your Daily Health Dashboard ---"));
        assertTrue(output.contains("DIET (Today)"));
        assertTrue(output.contains("Calories Consumed: 500 kcal"));
        assertTrue(output.contains("MILK (Today)"));
        assertTrue(output.contains("Total Pumped: 120 ml"));
        assertTrue(output.contains("FITNESS (This Week)"));
        assertTrue(output.contains("Workout Minutes: 30 mins"));
    }
}
