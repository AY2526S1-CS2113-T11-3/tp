package seedu.mama.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static java.lang.Integer.parseInt;

public class EntryList {
    private final List<Entry> items = new ArrayList<>();

    /**
     * Cached "last shown" view (what the user currently sees).
     */
    private List<Entry> shown = new ArrayList<>();
    /**
     * Current filter; null means "show all".
     */
    private Predicate<Entry> currentFilter = null;

    public EntryList() {
        recomputeShown();
    }

    public void add(Entry e) {
        items.add(e);
        recomputeShown(); // keep shown view in sync
    }

    public Entry deleteByIndex(int zeroBased) {
        Entry removed = items.get(zeroBased);
        if (removed instanceof MilkEntry) {
            String volStr = ((MilkEntry) removed).getMilk();
            String numberVol = volStr.replace("ml", "");
            int volume = parseInt(numberVol);
            MilkEntry.minusTotalMilkVol(volume);
        }
        Entry out = items.remove(zeroBased);
        recomputeShown(); // keep shown view in sync
        return out;
    }

    /**
     * Delete by index in the current shown view (what the user sees).
     */
    public Entry deleteByShownIndex(int zeroBasedShown) {
        if (zeroBasedShown < 0 || zeroBasedShown >= shown.size()) {
            throw new IndexOutOfBoundsException("Shown index " +
                    zeroBasedShown +
                    " out of range (size=" + shown.size() + ")");
        }
        Entry target = shown.get(zeroBasedShown);
        int realIndex = indexOf(target);
        if (realIndex < 0) {
            throw new IndexOutOfBoundsException("Shown entry not found in backing list");
        }
        return deleteByIndex(realIndex);
    }


    public int size() {
        return items.size();
    }

    public Entry get(int i) {
        return items.get(i);
    }

    public List<Entry> asList() {
        return new ArrayList<>(items);
    }

    // ====== Shown (filtered) view API ======

    /**
     * Number of entries in current shown view.
     */
    public int shownSize() {
        return shown.size();
    }

    /**
     * Entry at index in current shown view (0-based).
     */
    public Entry getShown(int i) {
        return shown.get(i);
    }

    /**
     * Unmodifiable snapshot of current shown view.
     */
    public List<Entry> getShownSnapshot() {
        return Collections.unmodifiableList(shown);
    }

    /**
     * Persist the filter (null => show all) and rebuild shown view.
     */
    public void setFilter(Predicate<Entry> predicate) {
        this.currentFilter = predicate;
        recomputeShown();
    }

    /**
     * Clear any filter and show all.
     */
    public void clearFilter() {
        this.currentFilter = null;
        recomputeShown();
    }

    /**
     * Rebuild the shown view from the backing list and currentFilter.
     */
    private void recomputeShown() {
        List<Entry> next = new ArrayList<>();
        for (Entry e : items) {
            if (currentFilter == null || currentFilter.test(e)) {
                next.add(e);
            }
        }
        this.shown = next;
    }

    /**
     * Locate an entry in the backing list by equals().
     */
    private int indexOf(Entry target) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }
}
