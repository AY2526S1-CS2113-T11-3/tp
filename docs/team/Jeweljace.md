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
* Diagrams (PlantUML):
    * Sequence (stepwise, simple, color-coded; square participants):
        * docs/diagrams/AddWorkoutUIDiagram.puml — UI captures raw input.
        * docs/diagrams/AddWorkoutParserDiagram.puml — Parser constructs AddWorkoutCommand (with clean constructor
          activation).
        * docs/diagrams/AddWorkoutCommandExecute.puml — execute(...) path with opt blocks (validation precondition +
          optional save) and return arrows.
        * docs/diagrams/AddWorkoutUseCase.puml — end-to-end flow (User→UI/Mama→Parser→Command→Model→Storage).
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

## **Contributions to the User Guide (Extracts)**

### 4. Logging a Workout — `workout`

Adds a workout entry with its duration (in minutes) and a required “feel” rating (1–5) so you can track recovery.

**Format**
> workout TYPE /dur DURATION /feel RATING


**Examples**
```workout yoga /dur 30 /feel 5```
```workout run /dur 45 /feel 3```

Compact forms are also accepted: ```workout TYPE/durDURATION/feelRATING```

**Notes**

- `TYPE` can be any text (e.g., running, yoga, strength training).
- `DURATION` must be a positive integer.
- `FEEL` must be an integer from 1 to 5.

| Feel rating | Description                                                                  |
|-------------|------------------------------------------------------------------------------|
| **1**       | I feel terrible and/or I hated the workout activity 😱🙅‍♀️                  |
| **2**       | I do not feel good and/or I disliked the workout activity 😭👎               |
| **3**       | I feel normal and/or I neither like nor dislike the workout activity 😐🤷‍♀️ |
| **4**       | I feel good and/or I liked the workout activity 😊👍                         |
| **5**       | I feel amazing and/or I loved the workout activity 😍💕                      |

- Exactly one `/dur` and one `/feel` must be present.
- Input must be in this format order: `workout TYPE /dur DURATION /feel RATING` (avoid `workout TYPE /feel RATING /dur DURATION`).
- Do not add extra words after the numbers (e.g., avoid `mins`, `great`):
  use `... /dur 30 /feel 4`, not `... /dur 30 mins /feel 4 great`.
- Each workout is timestamped automatically and saved to storage.
---

### 5. Managing Workout Goals — `workout goal`

Sets or views your **weekly workout goal**.

**Formats**
> workout goal MINUTES → sets a new goal
>
> workout goal → views current goal


**Examples**
```workout goal 150```
```workout goal```

**Notes**

- The goal represents total minutes of workouts per week.
- A week runs from **Monday 00:00** to **Sunday 23:59**
- The latest goal you set within the week applies for that week and goals do not stack. Setting a new goal later in the same week replaces the earlier one for that week only.
- Goals do not backfill past weeks and do not carry forward automatically, set a new goal next week if you want one.
- You can view past workout goals using ```list``` or ```list /t workout_goal```.

---

## **Contributions to the Developer Guide (Extracts)**

### 3.5 Add Workout — Jewel Jace Lim

#### Overview

`AddWorkoutCommand` records a workout with a type/description, duration (in minutes), and a feel rating (from 1 to 5).  
It validates inputs, appends a `WorkoutEntry`, and persists via `Storage#save(list)`.

#### Implementation Details

**Step 1.** User enters `workout yoga /dur 30 /feel 4` and `Ui` captures input.
> ![AddWorkout_UI](images/AddWorkoutUIDiagram.png)

**Step 2.** `Parser` validates the segments and builds `AddWorkoutCommand(description="yoga", duration="30", feel="4")`.

**Checks enforced:**

- Exactly one /dur and one /feel
- DURATION is a positive integer
- FEEL is an integer in [1..5]
- No extra tokens after the numbers

> ![AddWorkout_Parsing](images/AddWorkoutParserDiagram.png)

**Step 3.** `AddWorkoutCommand#execute(...)`  updates the model and persists.
Precondition: input already validated.

**Flow:**

1. Create WorkoutEntry("yoga", 30, 4).
2. EntryList.add(entry).
3. Persist via Storage.save(list).
4. Compute weekly minutes vs goal and compose feedback.
5. Return CommandResult to UI.

> ![Workout_ValidationAndAppend](images/AddWorkoutCommandExecuteDiagram.png)

**Step 4.** `Ui` displays result (e.g.,
`Got it. I've logged this workout: [Workout] yoga (30 mins, feel 4/5) (dd/MM/yy HH:mm)...`).

> ![AddWorkout_Sequence](images/AddWorkoutUseCase.png)

#### Design Considerations

**Aspect: Duration representation**

| Alternative                          | Pros                                              | Cons                                 |
|--------------------------------------|---------------------------------------------------|--------------------------------------|
| **Strict integer minutes (current)** | Flexible, easy to type, simple to parse/aggregate | Users must omit units (mins)         |
| Accept suffix (e.g., 30min)          | More natural for some users                       | More parsing branches, error surface |

**Aspect: Segment whitespace**

| Alternative                                            | Pros                                               | Cons                                                                   |
|--------------------------------------------------------|----------------------------------------------------|------------------------------------------------------------------------|
| **Compact markers allowed (/dur30, /feel4) (current)** | Flexible, easy to type, allows for more user error | Input might look ugly                                                  |
| Require whitespace (/dur 30, /feel 4)                  | Looks neater                                       | Less forgiving—missing a space causes errors; slightly more keystrokes |

#### Summary

- **Command:** `workout TYPE /dur DURATION /feel FEEL`
- **Example:** `workout yoga /dur 30 /feel 4`
- **Effect:** Appends a `WorkoutEntry` and saves immediately.

---

### 3.10 Set and View Weekly Workout Goal - Jewel Jace Lim
#### Overview
The weekly `workout goal` feature lets users set a target (in minutes) and view their progress for the current calendar week (Mon 00:00 → Sun 23:59:59).
It uses the shared timestamp abstraction (TimestampedEntry#timestamp()) for week calculations and persists goals as normal `WORKOUT GOAL entries`.
Users can view their past `workout goals` by using the list feature.

#### Implementation Details

**Step 1.** User enters for example: `workout goal 150`

**Step 2.** Parser routing
The Parser inspects tokens after workout goal:
- If a positive integer is present → SetWorkoutGoalCommand(minutesPerWeek).
- Otherwise → ViewWorkoutGoalCommand (view current week’s goal & progress).

**Step 3.** Set workout goal command

`SetWorkoutGoalCommand#execute(list, storage):`
- Validates `minutesPerWeek` > 0.
- Appends a new `WorkoutGoalEntry(minutesPerWeek)` to `EntryList`.
- Calls `Storage#save(EntryList)` to persist the updated list.
- Returns `CommandResult("Weekly workout goal set to X minutes.")`.

> ![SetWorkoutGoal_Sequence](images/SetWorkoutGoal_Sequence.png)

**Step 4.** View workout goal command

`ViewWorkoutGoalCommand#execute(list, storage):`
- Computes `weekStart = DateTimeUtil.weekStartMonday(now)` (uses TimestampedEntry#timestamp() consistently).
- Finds the latest `WorkoutGoalEntry` set for/on this week via `WorkoutGoalQueries#currentWeekGoal(list.asList(), weekStart)`.
- Iterates `EntryList` to collect `WorkoutEntry` instances in the same week (using `DateTimeUtil#inSameWeek(ts, weekStart))`, sums `getDuration()`.
- If a goal exists: computes `remaining = max(0, goal.minutesPerWeek - minutesThisWeek)` and formats progress.
- If no goal: reminds the user to set a goal and lists any workouts done.

> ![ViewWorkoutGoal_Sequence](images/ViewWorkoutGoal_Sequence.png)

**Step 5. UI output**

Ui prints the CommandResult message for both set & view paths.

#### Design Considerations

**Aspect: Number of workout goals shown in list**

| Alternative                                          | Pros                                                    | Cons                                               |
|------------------------------------------------------|---------------------------------------------------------|----------------------------------------------------|
| **Allow previous workout goal to remain (current)**  | User can track past goals to keep track of her progress | Overtime, might have too many workout goal entries |

#### Summary
**Command:**
- `workout goal <MINUTES>` → sets weekly target and persists via Storage#save(EntryList).
- `workout goal` → views current week’s goal and progress.