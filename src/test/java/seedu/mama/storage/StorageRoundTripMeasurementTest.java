package seedu.mama.storage;

import org.junit.jupiter.api.Test;

import seedu.mama.model.BodyMeasurementEntry;
import seedu.mama.model.Entry;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests round-trip serialization for BodyMeasurementEntry with timestamp included.
 */
public class StorageRoundTripMeasurementTest {

    @Test
    void roundTrip_withOptionals_ok() {
        Entry e = new BodyMeasurementEntry(75, 95, 88, null, 30);
        String line = e.toStorageString();

        assertTrue(line.startsWith("MEASURE|75|95|88|-|30|"),
                "Storage string should include timestamp after measurements: " + line);

        Entry back = BodyMeasurementEntry.fromStorage(line);
        String backLine = back.toStorageString();
        assertTrue(backLine.startsWith("MEASURE|75|95|88|-|30|"),
                "Round-tripped line should also contain timestamp.");
        assertEquals(e.toListLine(), back.toListLine(), "List representation should match after round-trip.");
    }

    @Test
    void roundTrip_minimal_ok() {
        Entry e = new BodyMeasurementEntry(80, 100, null, null, null);
        String line = e.toStorageString();

        // ✅ Expect starts-with since timestamp changes each run
        assertTrue(line.startsWith("MEASURE|80|100|-|-|-|"),
                "Storage string should include timestamp after measurements: " + line);

        // ✅ Round-trip check (load and re-serialize)
        Entry back = BodyMeasurementEntry.fromStorage(line);
        String backLine = back.toStorageString();
        assertTrue(backLine.startsWith("MEASURE|80|100|-|-|-|"),
                "Round-tripped line should also contain timestamp.");
        assertEquals(e.toListLine(), back.toListLine(),
                "List representation should match after round-trip.");
    }
}
