package seedu.mama.parser;

import seedu.mama.command.CommandException;
import seedu.mama.command.CommandResult;
import seedu.mama.command.HelpCommand;
import seedu.mama.command.ViewDashboardCommand;
import seedu.mama.command.SetWorkoutGoalCommand;
import seedu.mama.command.ViewWorkoutGoalCommand;
import seedu.mama.command.AddWeightCommand;
import seedu.mama.model.CalorieGoalQueries;


import seedu.mama.command.AddMealCommand;

import seedu.mama.command.Command;
import seedu.mama.command.DeleteCommand;
import seedu.mama.command.AddWorkoutCommand;
import seedu.mama.command.AddMilkCommand;

/**
 * Parses raw user input strings into the appropriate {@link Command} objects.
 * <p>
 * The {@code Parser} is responsible for interpreting user commands and returning
 * executable {@code Command} instances. It performs basic input validation and
 * provides feedback through {@link CommandResult} messages when invalid syntax
 * or arguments are detected.
 * <p>
 * Example usages:
 * <ul>
 *     <li>{@code delete 2} → returns a {@link DeleteCommand}</li>
 *     <li>{@code milk 150} → returns a {@link AddMilkCommand}</li>
 *     <li>{@code weight 5} → returns a {@link AddWeightCommand}</li>
 * </ul>
 */
public class Parser {

    /**
     * Parses the given raw user input and returns the corresponding {@link Command}.
     * <p>
     * The parser identifies the command keyword (e.g., {@code delete}, {@code list}, {@code milk})
     * and constructs an appropriate {@code Command} instance. If the command is invalid or
     * arguments are missing, the parser returns a command that produces a {@link CommandResult}
     * containing an error message.
     *
     * @param input Raw user input entered by the user.
     * @return A {@link Command} representing the parsed user intent.
     * @throws CommandException If a parsing error occurs that cannot be handled internally.
     */
    public static Command parse(String input) throws CommandException {
        String trimmed = input.trim();
        String lower = trimmed.toLowerCase();
        if (lower.contains("|")) {
            throw new CommandException("Invalid command arguments! No | allowed!");
        }

        // Handles the "bye" command (terminates the program)
        if (lower.equals("bye")) {
            return (l, s) -> new CommandResult("Bye. Hope to see you again soon!");
        }

        if (lower.equals("help")) {
            return new HelpCommand();
        }

        if (lower.equals("dashboard")) {
            return new ViewDashboardCommand();
        }

        // Handles "delete" commands
        if (lower.startsWith("delete")) {
            return DeleteCommand.fromInput(lower);
        }

        // Handles "list" command
        if (lower.startsWith("list")) {
            String arguments = lower.substring("list".length());
            return ListCommandParser.parseListCommand(arguments);
        }

        if (lower.startsWith("milk")) {
            return AddMilkCommand.fromInput(lower);
        }

        // Handles "workout goal" command, needs to be checked before generic "workout " command
        if (lower.toLowerCase().startsWith("workout goal")) {
            String[] parts = lower.split("\\s+");
            if (parts.length == 2) {
                // "workout goal" with no minutes input → view current workout goal
                return new ViewWorkoutGoalCommand();
            }
            // otherwise, delegate to the setter parser: "workout goal <minutes>"
            try {
                return SetWorkoutGoalCommand.fromInput(lower);
            } catch (CommandException e) {
                return (l, s) -> new CommandResult(e.getMessage());
            }
        }

        // Handles "workout " command
        if (lower.startsWith("workout ")) {
            return AddWorkoutCommand.fromInput(lower);
        }

        // Handles "weight" command
        if (lower.startsWith("weight")) {
            String[] parts = lower.split("\\s+");

            if (parts.length < 2) {
                return (l, s) -> new CommandResult("Weight must be a number. " +
                        "Try `weight`+ 'value of weight'");
            }
            try {
                return new AddWeightCommand(Double.parseDouble(parts[1]));
            } catch (NumberFormatException e) {
                return (l, s) -> new CommandResult("Weight must be a number. " +
                        "Try `weight`+ 'value of weight'");
            }
        }

        // Handles "meal" command
        if (lower.startsWith("meal")) {
            return AddMealCommand.fromInput(lower);
        }

        // Handles "measure" command
        if (lower.startsWith("measure")) {
            String[] parts = lower.split("\\s+");
            if (parts.length == 2 && "?".equals(parts[1])) {
                return (l, s) -> new CommandResult(
                        "Usage: measure waist/<cm> hips/<cm> [chest/<cm>] [thigh/<cm>] [arm/<cm>]");
            }

            Integer waist = null;
            Integer hips = null;
            Integer chest = null;
            Integer thigh = null;
            Integer arm = null;

            for (int i = 1; i < parts.length; i++) {
                String p = parts[i].trim().toLowerCase();
                try {
                    if (p.startsWith("waist/")) {
                        waist = Integer.parseInt(p.substring(6));
                    } else if (p.startsWith("hips/")) {
                        hips = Integer.parseInt(p.substring(5));
                    } else if (p.startsWith("chest/")) {
                        chest = Integer.parseInt(p.substring(6));
                    } else if (p.startsWith("thigh/")) {
                        thigh = Integer.parseInt(p.substring(6));
                    } else if (p.startsWith("arm/")) {
                        arm = Integer.parseInt(p.substring(4));
                    } else {
                        return (l, s) -> new CommandResult("Unknown field: " + p);
                    }
                } catch (NumberFormatException e) {
                    return (l, s) -> new CommandResult("Invalid number format for: " + p);
                }
            }

            try {
                return new seedu.mama.command.AddMeasurementCommand(waist, hips, chest, thigh, arm);
            } catch (seedu.mama.command.CommandException e) {
                return (l, s) -> new CommandResult(e.getMessage());
            }
        }

        // Handles "calorie goal" command
        if (lower.equals("calorie goal")) {
            // Just "calorie goal" -> show current goal
            return CalorieGoalQueries.viewCalorieGoal();
        }
        if (lower.startsWith("calorie goal ")) {
            // "calorie goal <calorie goal>" -> set new goal
            return CalorieGoalQueries.setCalorieGoal(lower);
        }

        return (l, s) -> new CommandResult("Unknown command.");
    }
}
