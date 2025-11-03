package seedu.mama.model;

import java.time.LocalDateTime;

import seedu.mama.util.DateTimeUtil;

/**
 * Represents a breast milk pumping session entry.
 */
public class MilkEntry extends TimestampedEntry {

    // 1) Constants (optional but nice to have)
    public static final String TYPE = "MILK";

    // 2) Static fields
    // Back-compat static counters used elsewhere (e.g., AddMilkCommand, EntryList)
    private static int totalMilkVol = 0;

    // 3) Instance fields
    private static int volumeMl = 0;

    // 4) Constructors (ALL ctors before any methods)

    /**
     * Constructs a new MilkEntry from user input.
     * Timestamp defaults to now().
     * @param userInput e.g., "150"
     */
    public MilkEntry(String userInput) {
        super(TYPE, normalizeVolume(userInput));
        volumeMl = parseVolumeMl(userInput);
    }

    /**
     * Deserialization path with explicit timestamp.
     */
    public MilkEntry(String userInput, LocalDateTime when) {
        super(TYPE, normalizeVolume(userInput), when);
        volumeMl = parseVolumeMl(userInput);
    }

    // 5) Methods (static or instance)

    // ---- Static counters (back-compat) ----
    public static void addTotalMilkVol(int milkVol) {
        totalMilkVol += milkVol;
    }

    public static void minusTotalMilkVol(int milkVol) {
        totalMilkVol -= milkVol;
    }

    public static String toTotalMilk() {
        return "Total breast milk pumped: " + totalMilkVol + "ml";
    }

    /**
     * Creates a MilkEntry from a storage string.
     *
     * @param line The line from storage, e.g., "MILK|150ml|28/10/25 01:14"
     * @return MilkEntry object
     */
    public static MilkEntry fromStorage(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 3 || !TYPE.equals(parts[0])) {
            throw new IllegalArgumentException("Invalid MILK entry line: " + line);
        }
        String volume = parts[1];
        LocalDateTime ts = DateTimeUtil.parse(parts[2].trim());

        // keep your running total behavior during load
        addTotalMilkVol(parseVolumeMl(volume));

        return new MilkEntry(volume, ts);
    }

    /** Returns the description of milk volume */
    public String getMilk() {
        return this.description();
    }

    // in MilkEntry
    public static int volumeMl() {
        return volumeMl;
    }

    @Override
    public String toListLine() {
        // Example: [MILK] 150ml (28/10/25 01:14)
        return "[" + type() + "] " + description() + " (" + timestampString() + ")";
    }

    @Override
    public String toStorageString() {
        // MILK|150ml|28/10/25 01:14
        return withTimestamp(TYPE + "|" + description());
    }

    // -------- helpers --------
    /**
     * Converts a string input into an integer volume in ml.
     * @return volume in ml
     */
    private static int parseVolumeMl(String input) {
        String s = input.trim().toLowerCase();
        if (s.endsWith("ml")) {                           // <- braces added/kept to satisfy NeedBraces
            s = s.substring(0, s.length() - 2);
        }
        return Integer.parseInt(s);
    }

    /**
     * Ensures the volume string ends with "ml".
     *
     * @param input User input
     * @return normalized string e.g., "150ml"
     */
    private static String normalizeVolume(String input) {
        String s = input.trim().toLowerCase();
        return s.endsWith("ml") ? s : s + "ml";
    }
}
