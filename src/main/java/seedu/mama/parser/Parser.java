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
        if (trimmed.contains("|")) {
            throw new CommandException("Invalid command arguments! No | allowed!");
        }

        // Handles the "bye" command (terminates the program)
        if (trimmed.equals("bye")) {
            return (l, s) -> new CommandResult("Bye. Hope to see you again soon!");
        }

        if (trimmed.equals("help")) {
            return new HelpCommand();
        }

        if (trimmed.equals("dashboard")) {
            return new ViewDashboardCommand();
        }

        // Handles "delete" commands
        if (trimmed.startsWith("delete")) {
            String[] parts = trimmed.split("\\s+");
            if (parts.length == 2 && parts[1].equals("?")) {
                return new DeleteCommand(-1);
            }
            if (parts.length < 2) {
                return (l, s) -> new CommandResult("Usage: delete INDEX");
            }
            try {
                return new DeleteCommand(Integer.parseInt(parts[1]));
            } catch (NumberFormatException e) {
                return (l, s) -> new CommandResult("INDEX must be a number.");
            }
        }
        // Handles "list" command
        if (trimmed.startsWith("list")) {
            String arguments = trimmed.substring("list".length());
            return ListCommandParser.parseListCommand(arguments);
        }

        if (trimmed.startsWith("milk")) {
            return AddMilkCommand.fromInput(trimmed);
        }

        // Handles "workout goal" command, needs to be checked before generic "workout " command
        if (trimmed.toLowerCase().startsWith("workout goal")) {
            String[] parts = trimmed.split("\\s+");
            if (parts.length == 2) {
                // "workout goal" with no minutes input → view current workout goal
                return new ViewWorkoutGoalCommand();
            }
            // otherwise, delegate to the setter parser: "workout goal <minutes>"
            try {
                return SetWorkoutGoalCommand.fromInput(trimmed);
            } catch (CommandException e) {
                return (l, s) -> new CommandResult(e.getMessage());
            }
        }

        // Handles "workout " command
        if (trimmed.toLowerCase().startsWith("workout ")) {
            return AddWorkoutCommand.fromInput(trimmed);
        }

        // Handles "weight" command
        if (trimmed.startsWith("weight")) {
            String[] parts = trimmed.split("\\s+");

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
        if (trimmed.startsWith("meal")) {
            return AddMealCommand.fromInput(trimmed);
        }

        // Handles "measure" command
        if (trimmed.startsWith("measure")) {
            String[] parts = trimmed.split("\\s+");
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
        if (trimmed.equals("calorie goal")) {
            // Just "calorie goal" -> show current goal
            return CalorieGoalQueries.viewCalorieGoal();
        }
        if (trimmed.startsWith("calorie goal ")) {
            // "calorie goal <calorie goal>" -> set new goal
            return CalorieGoalQueries.setCalorieGoal(trimmed);
        }

        return (l, s) -> new CommandResult("Unknown command.");
    }
}
