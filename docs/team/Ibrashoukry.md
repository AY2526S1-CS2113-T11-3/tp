# **Ibrahim Shoukry — Project Portfolio Page**

## **Project: MAMA**

**MAMA** is a Command Line Interface (CLI) health-tracking application designed to support mothers in monitoring their postpartum health through features such as meal logging, workout tracking, milk production, and body weight management.  
My primary contributions focused on implementing the **core data entry commands and models for milk production and weight tracking**, ensuring reliability, data integrity, and robust test coverage.

---

## **Summary of Contributions**

### **Code Contributed**

[**RepoSense Code Dashboard**](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=ibrashoukry&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=undefined&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=undefined)

---

### **Enhancements Implemented**

#### **1. New Feature: AddMealCommand**
* **What it does:**  
  Records a meal with a specified calories in kcal (e.g., `meal /cal 500`). Automatically updates and displays the total amount of calories recorded so far.

* **Justification:**  
  Enables mothers to track their calorie intake to keep a healthy diet for recovery and to make sure that the mother is getting enough calories to produce a healthy milk output.

* **Highlights:**
    * Implemented `AddMealCommand` using the **Command design pattern**, promoting modularity and easy extensibility for future features.
    * Added **robust input parsing and validation** in `fromInput()` to handle malformed commands, missing parameters, and reject negative or non-numeric values.
    * Integrated **optional macro tracking** (`/protein`, `/carbs`, `/fat`) to enhance nutritional detail while maintaining flexibility for minimal input.
    * Designed `MealEntry` as a dedicated subclass of `Entry`, encapsulating calorie and macronutrient data with clean string representations for both **display (`toListLine()`)** and **storage (`toStorageString()`)**.
    * Implemented **persistence support** via `toStorageString()` and `fromStorage()` ensuring data consistency across sessions.
    * Added **total calorie aggregation** logic in `execute()` to compute cumulative intake and provide feedback relative to user goals loaded from `Storage`.
    * Ensured clear, user-friendly command feedback with contextual goal tracking messages (“over” or “remaining” kcal).
    * Structured for **unit testing** of parsing, validation, storage, and goal feedback functionalities to guarantee reliability and correctness.


---

#### **2. New Feature: SetCalorieGoalCommand**
* **What it does:**  
  Allows the user to set and view a personalized daily calorie goal (e.g., `calorie goal 2000`), and track progress based on logged meal entries.

* **Justification:**  
  Setting a calorie goal helps users monitor and regulate their energy intake effectively. This feature complements other health-tracking entries (e.g., meals, workouts, and weight) by providing a clear daily target for nutrition management.

* **Highlights:**
    * Implemented `SetCalorieGoalCommand` to encapsulate goal-setting logic following the **Command design pattern**.
    * Added full **input validation and error handling**, rejecting negative or unrealistically high calorie goals.
    * Developed `CalorieGoalQueries` utility to provide reusable static methods for **viewing and setting calorie goals**.
    * Integrated **goal progress computation**—summing all recorded meal calories to show current progress toward the user’s goal.
    * Ensured robust interaction with the `Storage` subsystem to persist user goals between sessions.
    * Structured for **unit testing** of goal parsing, persistence, validation, and progress feedback to guarantee correctness and reliability.


---

#### **3. Enhancement: Model Layer Design Improvements**
* **What it does:**  
  Refined `CalorieGoalEntry` class and standardized model structures to improve cohesion and extendibility across entry types.

* **Justification:**  
  The model layer now follows a consistent design for data representation, parsing, and storage, improving maintainability and enabling seamless integration with future entry types.

* **Highlights:**
    * Introduced `CalorieGoalEntry` to represent user goal data in a unified format consistent with other `Entry` subclasses.
    * Implemented standardized serialization (`toStorageString()`) and deserialization (`fromStorage()`) for reliable persistence.
    * Reinforced abstraction principles by ensuring each entry type encapsulates its own domain logic and storage representation cleanly.
    * Improved model-level consistency to support future expansion (e.g., hydration or sleep tracking entries).


---

### **Contributions to the Developer Guide (DG)**

* Added comprehensive documentation for **AddMealCommand**, detailing its purpose, structure, and workflow.
    * Explained command parsing via `Parser` and parameter extraction logic (e.g., handling `/cal`, `/protein`, `/carbs`, `/fat`).
    * Illustrated validation flow for mandatory and optional parameters with appropriate error handling using `CommandException`.
    * Described interactions between `AddMealCommand`, `MealEntry`, `EntryList`, and `Storage` during execution.
* Included multiple **sequence and process diagrams** (e.g., parsing, validation, persistence) to visualize control flow and data propagation.
* Documented the **MealEntry model design**, showing how it encapsulates macronutrient data and supports persistence through `toStorageString()` and `fromStorage()`.
* Added comprehensive documentation for **SetCalorieGoalCommand**, detailing its purpose, structure, and workflow.
  * Explained command parsing via `Parser` and how it determines which function that follows (setting a goal or viewing a goal) by sending it to `CalorieGoalQueries`.
  * Illustrated validation flow for mandatory and optional parameters with appropriate error handling using `CommandException`.
  * Described interactions between `SetCalorieGoalCommand`, `CalorieGoalEntry`, `CalorieGoalQueries`, and `Storage` during execution.
* Provided detailed **design considerations** comparing alternative input methods and validation strategies.
* Emphasized compliance with **SOLID principles** — particularly **SRP** (each class has a distinct responsibility) and **OCP** (commands can be easily extended with new features).
* Added a concise **summary section** outlining command syntax, example usage, and resultant system behavior.


---

### **Contributions to Team-Based Tasks**

* Actively participated in team meetings and planning
* Assisted with technical help when needed
* Helped teammates with reviewing and merging PRs

---

### **Review / Mentoring Contributions**

* Contributed with code review and commenting on PRs
* Provided feedback on others' features
* Helped with fixing gradle issues and other issues related to updating local code from upstream

---
