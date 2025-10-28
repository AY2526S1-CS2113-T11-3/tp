package seedu.mama.model;

import seedu.mama.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Common base for entries that carry a creation timestamp.
 */
public abstract class TimestampedEntry extends Entry {
    private final LocalDateTime timestamp;

    protected TimestampedEntry(String type, String description) {
        super(type, description);
        this.timestamp = LocalDateTime.now();
    }

    protected TimestampedEntry(String type, String description, LocalDateTime when) {
        super(type, description);
        this.timestamp = Objects.requireNonNull(when, "timestamp");
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String timestampString() {
        return DateTimeUtil.format(timestamp);
    }

    protected String withTimestamp(String storagePrefix) {
        return storagePrefix + "|" + DateTimeUtil.format(timestamp);
    }
}
