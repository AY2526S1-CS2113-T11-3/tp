package seedu.mama.model;

import seedu.mama.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BodyMeasurementEntry extends TimestampedEntry {
    private static final Logger LOG = Logger.getLogger(BodyMeasurementEntry.class.getName());

    private final int waistCm;
    private final int hipsCm;
    private final Integer chestCm; // optional
    private final Integer thighCm; // optional
    private final Integer armCm;   // optional

    /**
     * New entries (now).
     */
    public BodyMeasurementEntry(int waistCm, int hipsCm, Integer chestCm, Integer thighCm, Integer armCm) {
        super("MEASURE", "Body measurements");
        if (waistCm <= 0 || hipsCm <= 0) {
            throw new IllegalArgumentException("Waist/hips must be > 0");
        }
        if (chestCm != null && chestCm <= 0) {
            throw new IllegalArgumentException("Chest must be > 0");
        }
        if (thighCm != null && thighCm <= 0) {
            throw new IllegalArgumentException("Thigh must be > 0");
        }
        if (armCm != null && armCm <= 0) {
            throw new IllegalArgumentException("Arm must be > 0");
        }
        this.waistCm = waistCm;
        this.hipsCm = hipsCm;
        this.chestCm = chestCm;
        this.thighCm = thighCm;
        this.armCm = armCm;
    }

    /**
     * Deserialization (exact timestamp).
     */
    private BodyMeasurementEntry(int w, int h, Integer c, Integer t, Integer a, LocalDateTime when) {
        super("MEASURE", "Body measurements", when);
        this.waistCm = w;
        this.hipsCm = h;
        this.chestCm = c;
        this.thighCm = t;
        this.armCm = a;
    }

    private static String v(Integer x) {
        return x == null ? "-" : String.valueOf(x);
    }

    @Override
    public String toListLine() {
        List<String> parts = new ArrayList<>();
        parts.add("waist=" + waistCm + "cm");
        parts.add("hips=" + hipsCm + "cm");
        if (chestCm != null) {
            parts.add("chest=" + chestCm + "cm");
        }
        if (thighCm != null) {
            parts.add("thigh=" + thighCm + "cm");
        }
        if (armCm != null) {
            parts.add("arm=" + armCm + "cm");
        }
        return "[MEASURE] " + String.join(", ", parts) + " (" + timestampString() + ")";
    }

    @Override
    public String toStorageString() {
        return withTimestamp(String.join("|", "MEASURE",
                String.valueOf(waistCm),
                String.valueOf(hipsCm),
                v(chestCm),
                v(thighCm),
                v(armCm)));
    }

    /**
     * STRICT: MEASURE|waist|hips|chestOr-|thighOr-|armOr-|timestamp
     */
    public static BodyMeasurementEntry fromStorage(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length != 7 || !"MEASURE".equals(p[0])) {
            throw new IllegalArgumentException("Bad MEASURE line (expect 7 parts): " + line);
        }
        int waist = Integer.parseInt(p[1]);
        int hips = Integer.parseInt(p[2]);
        Integer chest = parseNullable(p[3]);
        Integer thigh = parseNullable(p[4]);
        Integer arm = parseNullable(p[5]);
        LocalDateTime ts = DateTimeUtil.parse(p[6].trim());
        return new BodyMeasurementEntry(waist, hips, chest, thigh, arm, ts);
    }

    private static Integer parseNullable(String s) {
        return (s == null || s.isEmpty() || "-".equals(s)) ? null : Integer.valueOf(s);
    }
}
