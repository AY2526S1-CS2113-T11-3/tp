package seedu.mama.command;

import seedu.mama.model.BodyMeasurementEntry;
import seedu.mama.model.EntryList;
import seedu.mama.storage.Storage;

import java.util.logging.Logger;

/**
 * Usage example:
 * {@code measure waist/<cm> hips/<cm> [chest/<cm>] [thigh/<cm>] [arm/<cm>]}
 */
public class AddMeasurementCommand implements Command {
    private static final Logger LOG = Logger.getLogger(AddMeasurementCommand.class.getName());

    private final Integer waist;
    private final Integer hips;
    private final Integer chest;
    private final Integer thigh;
    private final Integer arm;

    public AddMeasurementCommand(Integer waist, Integer hips,
                                 Integer chest, Integer thigh, Integer arm) throws CommandException {
        if (waist == null || hips == null) {
            LOG.warning("Missing required fields: waist or hips is null");
            throw new CommandException("Waist and hips are required. Use waist/<cm> and hips/<cm>.");
        }
        if (waist <= 0 || hips <= 0) {
            LOG.warning(() -> "Non-positive required values: waist=" + waist + ", hips=" + hips);
            throw new CommandException("Waist and hips must be positive integers (cm).");
        }
        if (chest != null && chest <= 0) {
            throw new CommandException("Chest must be positive (cm).");
        }
        if (thigh != null && thigh <= 0) {
            throw new CommandException("Thigh must be positive (cm).");
        }
        if (arm != null && arm <= 0) {
            throw new CommandException("Arm must be positive (cm).");
        }

        this.waist = waist;
        this.hips = hips;
        this.chest = chest;
        this.thigh = thigh;
        this.arm = arm;
        LOG.fine(() -> String.format("AddMeasurementCommand w=%d h=%d c=%s t=%s a=%s",
                waist, hips, v(chest), v(thigh), v(arm)));
    }

    private static String v(Integer x) {
        return x == null ? "-" : String.valueOf(x);
    }

    @Override
    public CommandResult execute(EntryList list, Storage storage) {
        assert list != null : "EntryList must not be null";

        BodyMeasurementEntry entry = new BodyMeasurementEntry(waist, hips, chest, thigh, arm);
        list.add(entry);
        LOG.info(() -> "Added measurement: " + entry.toListLine());

        if (storage != null) {
            storage.save(list);
            LOG.fine("Storage.save called after adding measurement");
        } else {
            LOG.warning("Storage is null; changes not persisted");
        }
        return new CommandResult("Added: " + entry.toListLine());
    }
}
