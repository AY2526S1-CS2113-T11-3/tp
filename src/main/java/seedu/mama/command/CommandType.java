package seedu.mama.command;

import seedu.mama.model.EntryType; // Import EntryType

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Defines the available commands and their basic usage format for display.
 * This enum provides a single source of truth for command syntax.
 */
public enum CommandType {
    HELP("help"),
    WORKOUT("workout <description> /dur <duration (mins)> /feel <feeling (out of 5)>"),
    MEAL("meal <meal description> /cal <calories> [/protein <protein>] [/carbs <carbs>] [/fat <fat>]"),
    WEIGHT("weight <weight>"),
    MILK("milk <volume>"),
    MEASURE("measure waist/<cm> hips/<cm> [chest/<cm>] [thigh/<cm>] [arm/<cm>]"),
    DELETE("delete <index>"),
    LIST("list [/t " + EntryType.getValidTypesString() + "]"),
    DASHBOARD("dashboard"),
    WORKOUT_GOAL_SET("workout goal <minutes>"),
    WORKOUT_GOAL_VIEW("workout goal"),
    CALORIE_GOAL_SET("calorie goal <calories>"),
    CALORIE_GOAL_VIEW("calorie goal"),
    BYE("bye");

    private final String usage;

    CommandType(String usage) {
        this.usage = usage;
    }

    public String getUsage() {
        return usage;
    }

    /**
     * Generates a formatted string of all command usages for the help command.
     * @return A multi-line string with each command on a new line.
     */
    public static String getFormattedUsage() {
        return Arrays.stream(CommandType.values())
                .map(CommandType::getUsage)
                .collect(Collectors.joining("\n  - ")); // Format as a bulleted list
    }
}
