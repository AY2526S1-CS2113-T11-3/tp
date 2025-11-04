# **Jewel Jace Lim — Project Portfolio Page**

## **Project: MAMA**

**MAMA** is a Command Line Interface (CLI) health-tracking application designed for mothers to log and monitor essential
aspects of their daily wellbeing — including meals, workouts, milk production, weight, and body measurements.  
My main contributions focused on developing the **workout** related features which include **adding a workout entry**,
**setting a weekly workout goal**, **the initial timestamp handling for workout** (this was later refactored by my
teammate), and **viewing a weekly workout goal**. I also contributed to the **User Guide (UG)**
and **Developer Guide (DG)** and helped with updating **tests** in other features where necessary.

---

## **Summary of Contributions**

### **Code Contributed**

[**RepoSense Code Dashboard
**](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=jeweljace&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

---

### **Enhancements Implemented**

#### **1. New Feature: AddWorkoutCommand (Workout logging)**

* **What it does:**  
  Logs a workout with a type/description (e.g., yoga), duration in minutes (positive integer), and a feel rating (1–5).
  Each workout is timestamped, saved persistently, and the user gets instant feedback on weekly-goal progress (remaining
  minutes or goal reached).

* **Justification:**  
  Postpartum users can track both the quantity (minutes) and quality (feel) of workouts, enabling better recovery
  insights and adherence to weekly goals. The feel metric supports trend analysis (e.g., “lower feel after long runs”)
  and helps users pace their return to fitness.

* **Highlights:**
    * Implemented AddWorkoutCommand with fromInput(...) and execute(...), and extended WorkoutEntry to include feel (
      1–5) while preserving timestamps.
    * Integrated with the model and persistence: EntryList.add(...) + Storage.save(list) on each successful add.
    * Goal feedback: uses DateTimeUtil.weekStartMonday(...) and WorkoutGoalQueries to compute minutes this week vs. the
      current week’s goal, and tailors the confirmation message accordingly.
    * Robust parsing & validation:
        * Requires exactly one /dur and one /feel
        * duration > 0, feel ∈ [1..5]
        * Rejects extra tokens after numbers
        * Accepts compact markers (workout run/dur45/feel3) as well as spaced form
    * Developer quality: added JavaDoc and logging to workout-related classes for easier debugging and maintainability.
    * Tests updated/added:
        * AddWorkoutCommandTest (happy paths, multiple adds)
        * AddWorkoutCommandExceptionTest (invalid/edge cases incl. missing/duplicate segments, non-numeric/invalid feel)
        * Adjusted related tests (e.g., deletion/goal tests) to account for the new feel field that was added in v2.

---

#### **2. New Feature: SetWorkoutGoalCommand (Weekly workout goals)**

* **What it does:**  
  Sets a weekly workout goal in minutes (positive integer). Creates a timestamped WorkoutGoalEntry for the current week
  and persists it. The latest goal set within the same week is treated as the active goal used for progress feedback.

* **Justification:**  
  Weekly goals give users a clear target and enable meaningful progress tracking. Other features (e.g., logging
  workouts) can compute minutes completed vs. goal and motivate users with immediate, contextual feedback.

* **Highlights:**
    * Implemented SetWorkoutGoalCommand (fromInput(...), execute(...)) and integrated it with EntryList + Storage.save(
      list).
    * Added WorkoutGoalEntry (extends TimestampedEntry) to represent goals with consistent dd/MM/yy HH:mm timestamps.
    * Week logic: leveraged DateTimeUtil.weekStartMonday(...) and WorkoutGoalQueries.currentWeekGoal(...) so the latest
      goal set in [Mon..Sun) applies.
    * Validation: workout goal <minutes> format; minutes must be a positive integer; clear error messages for
      missing/non-numeric/invalid input.
    * Interop: AddWorkoutCommand uses the active weekly goal to display remaining minutes or goal reached.
    * Developer quality: added JavaDoc and logging around goal setting and queries to aid debugging and maintainability.
    * Tests:
        * SetWorkoutGoalCommandTest: valid execution and parsing, missing/non-numeric/non-positive cases.
        * Integration checks: workout add without goal shows reminder; with goal shows remaining minutes.

---

#### **3. New Feature: ViewWorkoutGoalCommand**

* **What it does:**  
  Shows the current week’s workout goal (if set), the total minutes completed this week, the remaining minutes (or “goal
  reached”), and a numbered list of this week’s workout entries with timestamps. Handles the “no goal set” case
  gracefully.

* **Justification:**  
  Gives users a quick, actionable snapshot of progress toward their weekly target—useful for pacing recovery, planning
  the next session, and reinforcing motivation without digging through logs.

* **Highlights:**
    * Implemented ViewWorkoutGoalCommand#execute(...) as a read-only command (does not mutate the model).
    * Week window computed via DateTimeUtil.weekStartMonday(LocalDateTime.now()).
    * Uses helpers to determine status:
        * WorkoutGoalQueries.currentWeekGoal(...) to fetch the latest goal set this week.
        * Iterates EntryList and filters by DateTimeUtil.inSameWeek(...) to sum minutes and list workouts (via
          WorkoutEntry.getDuration(), WorkoutEntry.timestampString()).
    * Clear, user-friendly output:
        * If no goal: prompts how to set one and lists workouts done this week (or “none yet”).
        * If goal exists: prints target, completed minutes, and remaining minutes (or congratulates when reached).
    * Developer quality: added JavaDoc and logging for easier debugging and consistent instrumentation with other
      workout commands.
    * Tests (where applicable): scenarios for no goal, goal set, and list formatting to ensure accurate totals and
      messages.

---

### **Contributions to the User Guide (UG)**

* Updated the **User Guide content** covering all *workout related features* (e.g., add workout log, set workout goal,
  and view workout goal)
* Added new sections for the **AddWorkoutCommand**, **SetWorkoutGoalCommand**, and **ViewWorkoutGoalCommand** features,
  including detailed usage examples.
* Ensured all command descriptions followed a consistent format (command syntax, examples, and notes).

---

### **Contributions to the Developer Guide (DG)**

* Added detailed explanation of **DeleteCommand** implementation:
    * Described step-by-step deletion process with index validation, removal, and data persistence.
    * Explained how it supports both full (`list`) and filtered (`list /t TYPE`) views.
    * Included a sequence diagram showing parsing, validation, and saving to storage.
    * Discussed design alternatives such as delete-by-index vs delete-by-keyword and their trade-offs.
      Scope & ownership
* Scope & ownership:
    * Authored Section 3.5 Add Workout (overview, command format, workflow, error handling, design considerations).
    * Wrote companion DG entries for SetWorkoutGoalCommand and ViewWorkoutGoalCommand to explain how goals integrate
      with workout logging and feedback.
    * Authored Section 3.10 Set and View Workout
* Diagrams (PlantUML):
    * Sequence (stepwise, simple, color-coded; square participants):
        * docs/diagrams/AddWorkoutUIDiagram.puml — UI captures raw input.
        * docs/diagrams/AddWorkoutParserDiagram.puml — Parser constructs AddWorkoutCommand (with clean constructor
          activation).
        * docs/diagrams/AddWorkoutCommandExecute.puml — execute(...) path with opt blocks (validation precondition +
          optional save) and return arrows.
        * docs/diagrams/AddWorkoutUseCase.puml — end-to-end flow (User→UI/Mama→Parser→Command→Model→Storage).
        * docs/diagrams/SetWorkoutGoal_Sequence.puml - Sequence diagram for set workout
        * docs/diagrams/ViewWorkoutGoal_Sequence.puml - Sequence diagram for view workout
* Content quality & consistency:
    * Standardized command grammar in DG (supports both spaced and compact markers; outputs normalized form in
      examples).
    * Documented error conditions succinctly (missing/duplicate segments, non-numeric/invalid ranges, extraneous
      tokens).
    * Kept diagrams intentionally minimal per DG guidelines (omitted low-value internals; used notes/legends instead of
      dense call stacks).
* Code–doc alignment:
    * Added/updated Javadoc and logging across workout-related classes to match the DG:
        * AddWorkoutCommand, SetWorkoutGoalCommand, ViewWorkoutGoalCommand
        * WorkoutEntry, WorkoutGoalEntry, WorkoutGoalQueries
        * Ensured DG examples mirror actual behavior (weekly goal window, latest-goal wins, persisted after add).
* Testing references:
* Linked DG behavior to test coverage: parsing/validation paths, execute happy paths, goal feedback, and persistence.

---

### **Contributions to Team-Based Tasks**

* Actively participated in **team discussions and milestone planning**, ensuring development progress remained aligned
  with project goals.
* Performed **code reviews** for teammates’ features, giving constructive feedback on structure, coding style, and
  adherence to SRP and assertion best practices.
* Refactored team testing and ensured cross-feature compatibility after addition of new features.
* Supported team integration during merges and Gradle build verifications.

---

### **Review / Mentoring Contributions**

* Reviewed PRs
* Helped teammates update their Tests to adhere to feature updates.
* Provided feedback on code readability and possible future feature updates.

---

