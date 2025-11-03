package seedu.mama.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.mama.model.EntryList;
import seedu.mama.storage.Storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SetCalorieGoalCommandTest {

    private EntryList entryList;
    private Storage storage;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        entryList = new EntryList();
        tempFile = Files.createTempFile("test", ".txt");
        storage = new Storage(Path.of(tempFile.toString()));
    }

    @Test
    void execute_validGoal_setsGoal() throws CommandException {
        SetCalorieGoalCommand command = new SetCalorieGoalCommand(2000);
        CommandResult result = command.execute(entryList, storage);

        assertEquals("Calorie goal set to 2000 kcal.", result.getFeedbackToUser());
        assertEquals(2000, storage.loadGoal());
    }

    @Test
    void execute_negativeGoal_returnsErrorMessage() throws CommandException {
        SetCalorieGoalCommand command = new SetCalorieGoalCommand(-100);
        CommandResult result = command.execute(entryList, storage);

        assertEquals("Calorie goal cannot be less than 0!", result.getFeedbackToUser());
    }

    @Test
    void execute_tooHighGoal_returnsErrorMessage() throws CommandException {
        SetCalorieGoalCommand command = new SetCalorieGoalCommand(20000);
        CommandResult result = command.execute(entryList, storage);

        assertEquals("Calorie goal too high", result.getFeedbackToUser());
    }

    @Test
    void execute_nullStorage_throwsCommandException() {
        SetCalorieGoalCommand command = new SetCalorieGoalCommand(2000);

        assertThrows(CommandException.class, () -> command.execute(entryList, null));
    }
}
