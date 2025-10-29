# User Guide

---

## Introduction

**Mama** is a Command Line Interface (CLI) health-tracking application designed for mothers to log meals, workouts, milk
pumping sessions, and body measurements.  
It helps users monitor nutrition, exercise, and postpartum recovery — all stored **locally** for privacy.

Mama is lightweight, works entirely **offline**, and runs on any system with Java 17 or later.

---

## Quick Start

1. **Ensure Java 17 or above is installed.**  
   You can verify by running:

```java -version```

2. **Download** the latest `mama.jar` file from your GitHub release page.

3. Open a terminal in the folder containing `mama.jar`.

4. Run the program:

```java -jar mama.jar```

5. You should see a welcome message and command prompt.

6. Type a command and press **Enter** to start logging your activities.

---

## Features

This section explains each command in Mama.  
Features marked with *Coming soon* are not yet implemented in this version.

---

### 1. Viewing Entries — `list`

Displays all entries stored in the system.  
You can also filter entries by their type.

**Format**

> list
>
> list /t TYPE


**Valid TYPE values:**  
`meal`, `workout`, `milk`, `measure`, `weight`, `workout_goal`

**Examples**

```list```
```list /t meal```
```list /t workout```

**Notes**

- The index displayed beside each entry is **1-based**.
- Use that index when deleting an entry.

---

### 2. Deleting an Entry — `delete`

Removes an entry by its index as shown in the `list` or `list /t TYPE` command.

**Format**
> delete INDEX


**Examples**
```delete 2```
```delete 5```

**Notes**

- Use the `list` or `list /t TYPE` command to find the correct index first.

---

### 3. Logging a Meal — `meal`

Adds a meal entry with its calorie value.

**Format**
> meal MEAL_NAME /cal CALORIES


**Examples**
```meal breakfast /cal 500```
```meal chicken rice /cal 650```

**Notes**

- `CALORIES` must be a positive integer.

---

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
- The latest goal you set within the week applies for that week. Setting a new goal later in the same week replaces the earlier one for that week only.
- Goals do not backfill past weeks and do not carry forward automatically, set a new goal next week if you want one.
- You can view past workout goals using ```list``` or ```list /t workout_goal```.

---

### 6. Logging Milk Volume — `milk`

Records a milk-pumping session in millilitres (ml).

**Format**
> milk VOLUME


**Examples**
```milk 150```
```milk 80```

**Notes**

- `VOLUME` must be a non-negative integer.
- Each entry records the date and time automatically.

---

### 7. Logging Weight — `weight`

Adds a body-weight entry in kilograms.

**Format**
> weight VALUE


**Examples**
```weight 70```
```weight 63```

**Notes**

- The value must be a whole number.

---

### 8. Logging Body Measurements — `measure`

Records body measurements such as waist, hips, or chest.  
You can include any combination of available fields.

**Format**
> measure waist/WAIST hips/HIPS [chest/CHEST] [thigh/THIGH] [arm/ARM]


**Examples**
```measure waist/78 hips/92```
```measure waist/78 hips/92 chest/96 thigh/55 arm/30```

**Notes**

- Each field must be a positive integer (in cm).
- The order of fields does not matter.

---

### 9. Calorie Goal — `goal`

Sets or displays your daily calorie goal.

**Formats**
> goal CALORIES → sets a new goal
>
> goal → views current goal


**Examples**
```goal 1800```
```goal```

**Notes**

- Calories are summed from logged meals.

---
### 10. Viewing Health Dashboard — `dashboard`

Displays a consolidated overview of your health data, including today's diet and milk output, and this week's fitness progress.

**Format**

> dashboard → view dashboard

Example ```dashboard```

**Notes**

- The dashboard automatically aggregates data based on the current date and week (starting from Monday).

- It will display calorie and workout goals, along with remaining amounts, if you have set them using the goal <calories> and workout goal <minutes> commands respectively.

- If goals are not set, reminder messages will be shown instead.

### 10. Exiting the Program — `bye`

Ends the session and saves all data automatically.

**Format**
> bye


**Example**
```bye```

**Notes**

- Your data is saved automatically before exiting, so no need to save manually.

---

## Data File

Mama automatically creates and updates a text file named `mama.txt` in the same folder as `mama.jar`.  
Each entry is stored on a separate line, using the `|` character as a separator.

**Example Content**
> MEAL|breakfast|500
>
> WORKOUT|yoga|45|28/10/25 02:33
>
> MILK|120ml|28/10/25 02:32
>
> MEASURE|70|98|90|55|30|28/10/25 02:53

**Notes**

- Do **not** edit `mama.txt` manually unless necessary.
- Deleting the file will reset all data.

---

## FAQ

**Q:** How do I filter only specific entries?  
**A:** Use `list /t TYPE`. Example: `list /t meal`.

**Q:** Why do I get an invalid index error?  
**A:** The index number must exist in the list shown by `list`.

**Q:** Does Mama require internet access?  
**A:** No. Everything is stored locally for privacy.

---

## Command Summary

| Command             | Format                                                                | Example                    |
|---------------------|-----------------------------------------------------------------------|----------------------------|
| **List**            | `list` or `list /t TYPE`                                              | `list /t meal`             |
| **Delete**          | `delete INDEX`                                                        | `delete 2`                 |
| **Add Meal**        | `meal MEAL_NAME /cal CALORIES`                                        | `meal breakfast /cal 500`  |
| **Add Workout**     | `workout TYPE /dur DURATION`                                          | `workout yoga /dur 30`     |
| **Workout Goal**    | `workout goal [MINUTES]`                                              | `workout goal 150`         |
| **Add Milk**        | `milk VOLUME`                                                         | `milk 150`                 |
| **Add Weight**      | `weight VALUE`                                                        | `weight 70`                |
| **Add Measurement** | `measure waist/WAIST hips/HIPS [chest/CHEST] [thigh/THIGH] [arm/ARM]` | `measure waist/78 hips/92` |
| **Calorie Goal**    | `goal [CALORIES]`                                                     | `goal 1800`                |
| **Exit**            | `bye`                                                                 | `bye`                      |

