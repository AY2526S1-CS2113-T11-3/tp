package seedu.mama;

import seedu.mama.command.Command;
import seedu.mama.command.CommandException;
import seedu.mama.command.CommandResult;
import seedu.mama.model.EntryList;
import seedu.mama.parser.Parser;
import seedu.mama.storage.Storage;
import seedu.mama.ui.Ui;

import java.util.Scanner;
import java.util.logging.Logger;

public class Mama {
    /**
     * Main entry-point for the MAMA app.
     */
    private static final Logger LOG = Logger.getLogger(Mama.class.getName());

    public static void main(String[] args) {

        assert false : "dummy assertion set to fail";
        System.out.println("Hello from MAMA");
        System.out.println("Enter a command (workout <description> /dur <duration>, " +
                "workout goal, " +
                "workout goal <duration in minutes>, " +
                "weight <weight>, " +
                "milk <volume>, " +
                "delete <index>, " +
                "list, " +
                "bye)");

        Storage storage = Storage.defaultStorage();
        EntryList list = storage.loadOrEmpty();

        Scanner sc = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            String userInput = sc.nextLine();
            try {
                Command command = Parser.parse(userInput);
                CommandResult result = command.execute(list, storage);
                System.out.println(result.getFeedbackToUser());
                if (result.isExit()) {
                    Ui.showMessage("Shutting down...");
                    break;
                }
            } catch (CommandException ce) {
                // user-facing (runtime) error, like invalid delete index or save failure
                System.out.println(ce.getMessage());
                LOG.warning("Command failed: " + ce.getMessage());
            } catch (Exception e) {
                // safeguard for any other unforeseen error
                System.out.println("An unexpected error occurred: " + e.getMessage());
                LOG.severe("Unexpected exception: " + e);
            }

            if (userInput.equalsIgnoreCase("bye")) {
                isRunning = false;
            }
        }
        sc.close();
    }
}
