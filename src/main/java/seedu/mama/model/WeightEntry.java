package seedu.mama.model;

import java.text.DecimalFormat;
/**
 * Represents a user weight entry.
 */
public class WeightEntry extends Entry {

    private static final DecimalFormat DECIMAL_FORMAT= new DecimalFormat("0.00");

    private final double weightInKG;

    /**
     * Constructs a new WeightEntry with the given weight value in kilograms.
     * @param weightInKG The weight in kg. Must be positive.
     */
    public WeightEntry(double weightInKG) {
        super("WEIGHT", formatWeight(weightInKG));
        this.weightInKG = weightInKG;
    }

    /**
     * Formats a weight double value to a string with 2 decimal places and "kg" suffix.
     *
     * @param weight weight in kg
     * @return formatted weight string, e.g., "65.50kg"
     */
    private static String formatWeight(double weight) {
        return DECIMAL_FORMAT.format(weight) + "kg";
    }

    public double getWeight() {
        return this.weightInKG;
    }

    /**
     * Converts the entry to a string suitable for storage.
     *
     * @return String in format "WEIGHT|weightValue"
     */
    @Override
    public String toStorageString() {
        return "WEIGHT|" + this.weightInKG;
    }

    /**
     * Creates a WeightEntry object from a storage string line.
     * If the stored weight is invalid, defaults to 0.0kg and prints an error message.
     * @param line Storage line
     * @return WeightEntry object
     */
    public static WeightEntry fromStorage(String line) {
        String[] parts = line.split("\\|", 2);
        String weightString = parts.length > 1 ? parts[1] : "0.0";

        try {
            double storedWeight = Double.parseDouble(weightString);
            return new WeightEntry(storedWeight);
        } catch (NumberFormatException e) {
            System.err.println("Invalid weight: " + weightString);
            return new  WeightEntry(0.0);
        }
    }
}
