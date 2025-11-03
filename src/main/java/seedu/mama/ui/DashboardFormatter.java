package seedu.mama.ui;

import seedu.mama.model.DashboardSummary;
import seedu.mama.model.WorkoutGoalEntry;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Formats the data from a DashboardSummary into a user-friendly string.
 */
public class DashboardFormatter {
    private static final Logger LOGGER = Logger.getLogger(DashboardFormatter.class.getName());
    /**
     * Formats the entire dashboard.
     * @param summary The data object containing dashboard statistics.
     * @return A formatted string representing the dashboard.
     */
    public String format(DashboardSummary summary) {
        // Assertion for non-null input
        assert summary != null : "DashboardSummary cannot be null";

        LOGGER.log(Level.INFO, "Formatting dashboard data...");

        StringBuilder sb = new StringBuilder();
        sb.append("--- Your Daily Health Dashboard ---\n\n");

        formatDietSection(sb, summary);
        sb.append("\n");
        formatMilkSection(sb, summary);
        sb.append("\n");
        formatFitnessSection(sb, summary);

        String formattedString = sb.toString().trim();
        LOGGER.log(Level.INFO, "Dashboard formatted successfully.");
        return formattedString;
    }

    private void formatDietSection(StringBuilder sb, DashboardSummary summary) {
        sb.append(">> DIET (Today)\n");
        sb.append(String.format("   - Calories Consumed: %d kcal\n", summary.getCaloriesToday()));

        Integer goal = summary.getCalorieGoal();
        if (goal != null) {
            int remaining = goal - summary.getCaloriesToday();
            sb.append(String.format("   - Calorie Goal:      %d kcal\n", goal));
            sb.append(String.format("   - Remaining:         %d kcal\n", Math.max(0, remaining)));
        } else {
            sb.append("   - No calorie goal set. Use 'calorie goal <calories>' to set one.\n");
        }
    }

    private void formatMilkSection(StringBuilder sb, DashboardSummary summary) {
        sb.append(">> MILK (Today)\n");
        sb.append(String.format("   - Total Pumped: %d ml\n", summary.getMilkToday()));
    }

    private void formatFitnessSection(StringBuilder sb, DashboardSummary summary) {
        sb.append(">> FITNESS (This Week)\n");
        sb.append(String.format("   - Workout Minutes: %d mins\n", summary.getWorkoutMinutesThisWeek()));

        WorkoutGoalEntry goal = summary.getWorkoutGoal();
        if (goal != null) {
            int remaining = goal.getMinutesPerWeek() - summary.getWorkoutMinutesThisWeek();
            sb.append(String.format("   - Weekly Goal:     %d mins\n", goal.getMinutesPerWeek()));
            sb.append(String.format("   - Remaining:         %d mins\n", Math.max(0, remaining)));
        } else {
            sb.append("   - No weekly workout goal set. Use 'workout goal <minutes>'.\n");
        }
    }
}
