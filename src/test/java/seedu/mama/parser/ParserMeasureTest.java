package seedu.mama.parser;

import org.junit.jupiter.api.Test;

import seedu.mama.command.Command;
import seedu.mama.command.CommandResult;
import seedu.mama.model.EntryList;
import seedu.mama.testutil.TestStorage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserMeasureTest {

    @Test
    void parse_minimal_ok() throws Exception {
        Command cmd = Parser.parse("measure waist/75 hips/95");
        CommandResult res = cmd.execute(new EntryList(), new TestStorage.Noop());
        assertTrue(res.getFeedbackToUser().contains("Added"));
    }

    @Test
    void parse_anyOrder_ok() throws Exception {
        Command cmd = Parser.parse("measure hips/96 chest/88 waist/76 arm/30");
        CommandResult res = cmd.execute(new EntryList(), new TestStorage.Noop());
        assertTrue(res.getFeedbackToUser().contains("waist=76cm"));
        assertTrue(res.getFeedbackToUser().contains("hips=96cm"));
        assertTrue(res.getFeedbackToUser().contains("chest=88cm"));
        assertTrue(res.getFeedbackToUser().contains("arm=30cm"));
    }

    @Test
    void parse_missingRequired_showsError() throws Exception {
        Command cmd = Parser.parse("measure waist/75 chest/88");
        CommandResult res = cmd.execute(new EntryList(), new TestStorage.Noop());
        assertTrue(res.getFeedbackToUser().toLowerCase().contains("waist and hips are required"));
    }

    @Test
    void parse_invalidNumber_showsError() throws Exception {
        Command cmd = Parser.parse("measure waist/abc hips/95");
        CommandResult res = cmd.execute(new EntryList(), new TestStorage.Noop());
        assertTrue(res.getFeedbackToUser().toLowerCase().contains("invalid number"));
    }

    @Test
    void parse_help_showsUsage() throws Exception {
        Command cmd = Parser.parse("measure ?");
        CommandResult res = cmd.execute(new EntryList(), new TestStorage.Noop());
        assertTrue(res.getFeedbackToUser().toLowerCase().contains("usage"));
    }
}
