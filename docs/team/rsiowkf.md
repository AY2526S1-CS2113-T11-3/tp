# **Ryan Siow — Project Portfolio Page**

## **Project: MAMA**

**MAMA** is a Command Line Interface (CLI) health-tracking application designed to support mothers in monitoring their postpartum health through features such as meal logging, workout tracking, milk production, and body weight management.  
My primary contributions focused on implementing the **core data entry commands and models for milk production and weight tracking**, ensuring reliability, data integrity, and robust test coverage.

---

## **Summary of Contributions**

### **Code Contributed**

[**RepoSense Code Dashboard**](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=rsiowkf&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-09-19T00%3A00%3A00&filteredFileName=)

---

### **Enhancements Implemented**

#### **1. New Feature: AddMilkCommand**
* **What it does:**  
  Records a milk-pumping session with a specified volume in milliliters (e.g., `milk 80`). Automatically updates and displays the total amount of breast milk pumped so far.

* **Justification:**  
  Enables mothers to track their breastfeeding output accurately over time — an essential health indicator for both mother and child.

* **Highlights:**
    * Implemented `AddMilkCommand` following the Command design pattern for modular extensibility.
    * Added defensive input validation (rejects negative or non-numeric inputs).
    * Incorporated **logging (`java.util.logging`)** and **assertions** for maintainability and debugging.
    * Implemented aggregation logic (`MilkEntry.addTotalMilkVol()`) to maintain a running total of milk produced.
    * Designed `MilkEntry` as a subclass of `TimestampedEntry` to record time-specific entries.
    * Implemented persistence support through `toStorageString()` and `fromStorage()` for file saving and loading.
    * Created comprehensive **JUnit tests** (`AddMilkCommandTest`) ensuring command correctness, timestamp validity, and accumulation logic.

---

#### **2. New Feature: AddWeightCommand**
* **What it does:**  
  Records the user’s weight in kilograms (e.g., `weight 80`).

* **Justification:**  
  Regular weight monitoring supports postpartum health tracking and complements other recorded metrics like workouts and calorie intake.

* **Highlights:**
    * Implemented `AddWeightCommand` with full input validation and error handling.
    * Introduced `WeightEntry` model class adhering to SRP, representing a clean abstraction for weight data.
    * Ensured clean integration with the storage and entry listing subsystems.
    * Developed **JUnit tests** (`AddAddWeightCommandTest`) to verify multiple entry addition, correct string formatting, and data persistence consistency.

---

#### **3. Enhancement: Model Layer Design Improvements**
* **What it does:**  
  Refined `MilkEntry` and `WeightEntry` classes to improve cohesion and extendibility.

* **Justification:**  
  The previous structure lacked consistency between entry types. Establishing a standard format for data parsing, storage, and retrieval enhances maintainability.

* **Highlights:**
    * Introduced standardized serialization (`toStorageString()`) and deserialization (`fromStorage()`) methods across entry types.
    * Centralized timestamp management in `TimestampedEntry`, promoting uniformity.
    * Added normalization helpers to handle flexible user inputs (e.g., auto-appending “ml” to milk entries).

---

### **Contributions to the Developer Guide (DG)**

* Added detailed explanation of **AddMilkCommand** design and implementation:
    * Command parsing and validation flow.
    * Interaction between `Command`, `EntryList`, and `Storage`.
    * Sequence diagram for `AddMilkCommand` execution (showing creation, model update, and storage save).
* Documented the **MilkEntry aggregation logic**, explaining how cumulative milk totals are maintained across sessions.
* Highlighted adherence to **Single Responsibility Principle (SRP)** and **Open/Closed Principle (OCP)** in command structure and model hierarchy.

---

### **Contributions to Team-Based Tasks**

* Participated actively in team meetings and milestone planning.
* Conducted code reviews for teammates’ features and provided feedback on coding style, assertions, and SLAP adherence.
* Assisted in setting up Gradle test configurations and resolving initial test failures related to file formatting and end-of-line handling.

---

### **Review / Mentoring Contributions**

* **Reviewed PR [#43](https://github.com/AY2526S1-CS2113-T11-3/tp/pull/43)** Reviewed teammates’ pull requests and suggested adding edge case tests (e.g. invalid meal inputs) to strengthen input validation and improve test coverage.

---
