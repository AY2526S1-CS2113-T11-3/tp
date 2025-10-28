package seedu.mama.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that AddWorkoutCommand.fromInput() throws CommandException
 * for invalid or malformed input strings.
 */
public class AddWorkoutCommandExceptionTest {

    @Test
    public void fromInput_missingDurSegment_throws() {
        CommandException ex = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout run /feel 4")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("missing '/dur' segment"));
    }

    @Test
    public void fromInput_missingFeelSegment_throws() {
        CommandException ex = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout run /dur 30")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("missing '/feel' segment"));
    }

    @Test
    public void fromInput_multipleDurSegments_throws() {
        CommandException ex = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout run /dur 30 /dur 40 /feel 3")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("too many '/dur' segments"));
    }

    @Test
    public void fromInput_multipleFeelSegments_throws() {
        CommandException ex = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout run /dur 30 /feel 3 /feel 4")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("too many '/feel' segments"));
    }

    @Test
    public void fromInput_missingType_throws() {
        CommandException ex = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout /dur 30 /feel 4")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("workout type cannot be empty"));
    }

    @Test
    public void fromInput_nonNumericDuration_throws() {
        CommandException ex = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout run /dur forty /feel 4")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("duration must be a whole number"));
    }

    @Test
    public void fromInput_nonNumericFeel_throws() {
        CommandException ex = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout run /dur 30 /feel happy")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("feel rating must be a number"));
    }

    @Test
    public void fromInput_feelOutOfRange_throws() {
        CommandException exHigh = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout run /dur 30 /feel 6")
        );
        assertTrue(exHigh.getMessage().toLowerCase().contains("between 1 and 5"));

        CommandException exLow = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout run /dur 30 /feel 0")
        );
        assertTrue(exLow.getMessage().toLowerCase().contains("between 1 and 5"));
    }

    @Test
    public void fromInput_extraTokensAfterDuration_throws() {
        CommandException ex = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout run /dur 30 mins /feel 3")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("unexpected input after duration"));
    }

    @Test
    public void fromInput_extraTokensAfterFeel_throws() {
        CommandException ex = assertThrows(
                CommandException.class,
                () -> AddWorkoutCommand.fromInput("workout run /dur 30 /feel 4 great")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("unexpected input after feel"));
    }
}
