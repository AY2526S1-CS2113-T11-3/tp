package seedu.mama.ui;

import seedu.mama.command.CommandType;

/**
 * Handles all user interface interactions.
 * <p>
 * Responsible for displaying messages, prompts, and visual separators
 * to the user in the console.
 */
public class Ui {

    /**
     * Line separator used to format console output.
     */
    private final String line;

    /**
     * Constructs a {@code Ui} instance with a default line separator.
     */
    public Ui() {
        this.line = "____________________________________________________________";
    }

    /**
     * Prints a horizontal line separator to the console.
     */
    private void printLine() {
        System.out.println(line);
    }
    /**
     * Displays the welcome message and available commands to the user.
     */
    public void showWelcome() {
        showMessage("Hello from MAMA");
        showMessage("Enter a command (" + CommandType.getAllUsageString() + ")");
    }

    /**
     * Displays a generic message to the user.
     * @param message The message to display.
     */
    public static void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user.
     * @param errorMessage The error message to display.
     */
    public void showError(String errorMessage) {
        showMessage("Error: " + errorMessage);
    }
}
