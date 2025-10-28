package seedu.mama.testutil;

import java.nio.file.Path;

import seedu.mama.model.EntryList;
import seedu.mama.storage.Storage;

public final class TestStorage {
    private TestStorage() {
    }

    /**
     * Storage that never writes.
     */
    public static class Noop extends Storage {
        public Noop() {
            super(Path.of("build", "noop.txt"));
        }

        @Override
        public void save(EntryList list) { /* no-op */ }
    }

    /**
     * Storage that records if save() was called.
     */
    public static class Spy extends Storage {
        public boolean saved = false;

        public Spy() {
            super(Path.of("build", "spy.txt"));
        }

        @Override
        public void save(EntryList list) {
            saved = true;
        }
    }
}
