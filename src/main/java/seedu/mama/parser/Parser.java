package seedu.mama.parser;

import seedu.mama.command.CommandException;
import seedu.mama.command.CommandResult;
import seedu.mama.command.SetGoalCommand;
import seedu.mama.command.SetWorkoutGoalCommand;
import seedu.mama.command.ViewWorkoutGoalCommand;
import seedu.mama.command.AddWeightCommand;

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

        // Handles the "bye" command (terminates the program)
        if (trimmed.equals("bye")) {
            return (l, s) -> new CommandResult("Bye. Hope to see you again soon!");
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
            if (parts.length == 2 && parts[1].equals("?")) {
                return new AddWeightCommand(-1);
            }
            if (parts.length < 2) {
                return (l, s) -> new CommandResult("Weight must be a number. Try `weight`+ 'value of weight'");
            }
            try {
                return new AddWeightCommand(Integer.parseInt(parts[1]));
            } catch (NumberFormatException e) {
                return (l, s) -> new CommandResult("Weight must be a number. Try `weight`+ 'value of weight'");
            }
        }

        // Handles "meal" command
        if (trimmed.startsWith("meal")) {
            return AddMealCommand.fromInput(trimmed);
        }

        // Handles "goal" command
        if (trimmed.startsWith("goal ")) {
            // "goal <calorie goal>" -> set new goal
            String desc = trimmed.substring("goal".length()).trim();

            if (desc.isEmpty()) {
                throw new CommandException("Invalid format! Try: goal <calories>");
            }

            try {
                int goal = Integer.parseInt(desc.split("\\s+")[0]);
                return new SetGoalCommand(goal);
            } catch (NumberFormatException e) {
                throw new CommandException("Calorie goal must be a number.");
            }
        }

        if (trimmed.equals("goal")) {
            // Just "goal" -> show current goal
            return (list, storage) -> {
                Integer goalValue = (storage != null) ? storage.loadGoal() : null;
                if (goalValue == null) {
                    return new CommandResult("No calorie goal set yet. Use: goal <calories>");
                }

                // show how many calories logged
                int totalCal = list.asList().stream()
                        .filter(e -> e.type().equals("MEAL"))
                        .mapToInt(e -> ((seedu.mama.model.MealEntry) e).getCalories())
                        .sum();

                String progress = "Your current calorie goal is: " + goalValue + " kcal."
                        + " | Progress: " + totalCal + " kcal logged.";
                return new CommandResult(progress);
            };
        }


        return (l, s) -> new CommandResult("Unknown command.");
    }
}
