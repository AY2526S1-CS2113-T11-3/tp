package seedu.mama.command;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Defines the available commands and their basic usage format for display.
 */
public enum CommandType {
    WORKOUT("workout <description> /dur <duration (mins)> /feel <feeling (out of 5)>"),
    MEAL("meal <meal description> /cal <calories>"),
    WEIGHT("weight <weight>"),
    MILK("milk <volume>"),
    MEASURE("measure waist/<cm> hips/<cm> [chest/<cm>] [thigh/<cm>] [arm/<cm>]"),
    DELETE("delete <index>"),
    LIST("list [/t TYPE]"),
    DASHBOARD("dashboard"),
    WORKOUT_GOAL_SET("workout goal <minutes>"),
    WORKOUT_GOAL_VIEW("workout goal"), // Separate entry for viewing
    GOAL_SET("goal <calories>"),
    GOAL_VIEW("goal"),
    BYE("bye");

    private final String usage;

    CommandType(String usage) {
        this.usage = usage;
    }

    public String getUsage() {
        return usage;
    }

    /**
     * Generates a string listing all command usages, separated by commas.
     * @return A string like "(command1, command2, ...)"
     */
    public static String getAllUsageString() {
        return Arrays.stream(CommandType.values())
                .map(CommandType::getUsage)
                .collect(Collectors.joining(", "));
    }
}
