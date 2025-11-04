package seedu.mama;

import seedu.mama.command.Command;
import seedu.mama.command.CommandException;
import seedu.mama.command.CommandResult;
import seedu.mama.model.EntryList;
import seedu.mama.parser.Parser;
import seedu.mama.storage.Storage;
import seedu.mama.ui.Ui;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mama {
    private static final Logger LOG = Logger.getLogger(Mama.class.getName());
    // --- Add Ui instance ---
    private final Ui ui;
    private final Storage storage;
    private final EntryList list;

    /**
     * Constructor for Mama application logic.
     * Initializes components.
     */
    public Mama() {
        ui = new Ui();
        storage = Storage.defaultStorage();
        list = storage.loadOrEmpty();
    }

    /**
     * Runs the main application loop.
     */
    public void run() {
        ui.showWelcome();

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) {
                break; // Handle EOF for tests
            }
            String userInput = sc.nextLine().trim();
            if (userInput.isEmpty()) {
                continue;
            }

            try {
                Command command = Parser.parse(userInput);
                CommandResult result = command.execute(list, storage);
                Ui.showMessage(result.getFeedbackToUser());

                if (result.isExit()) {
                    break;
                }

                // Temporary check for "bye" until CommandResult handles exit
                if (userInput.equalsIgnoreCase("bye")) {
                    break;
                }

            } catch (CommandException ce) {
                LOG.log(Level.WARNING, "Command failed: " + ce.getMessage());
                ui.showError(ce.getMessage()); // <-- Use Ui to show error
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Unexpected exception", e);
                ui.showError("An unexpected error occurred: " + e.getMessage()); // <-- Use Ui to show error
            }
        }
        sc.close();
    }


    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.SEVERE);
        new Mama().run(); // Create instance and run
    }
}
