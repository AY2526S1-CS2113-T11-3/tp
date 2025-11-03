# **Lee Yi Sheng \- Project Portfolio Page**

## **Project: MAMA**

**MAMA** is a Command Line Interface (CLI) health-tracking application designed specifically for mothers to easily log and monitor crucial aspects of their postpartum journey, including meals, workouts, milk production, weight, and body measurements. My contributions focused on enhancing data visibility and improving the application's overall structure for better maintainability.

## **Summary of Contributions**

### **Code Contributed \[[RepoSense Code Dashboard](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=lee-yisheng&breakdown=true&sort=groupTitle+dsc&sortWithin=title&since=2025-09-19T00:00:00&timeframe=commit&mergegroup&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName)\]**

### **Enhancements Implemented**

My primary contributions centered on enhancing data visibility and improving the application's architectural quality. I upgraded the **`list` command** to support type filtering (e.g., `list /t meal`), a critical usability feature for users managing a growing number of entries. This was achieved by implementing a dedicated `ListCommandParser` to adhere to the Single Responsibility Principle (SRP) and refactoring `ListCommand` to accept a `Predicate<Entry>`, which makes the filtering logic flexible and easily extensible for future criteria, such as filtering by date.

To provide users with immediate, actionable insights, I introduced the new **`dashboard` command**. This feature gives a consolidated, at-a-glance view of the user's health status, summarizing daily calorie intake, milk production, and weekly workout progress against their goals. The design is highly cohesive, separating concerns into three distinct components: `DashboardSummary` (Model) for data aggregation, `DashboardFormatter` (UI) for presentation, and `ViewDashboardCommand` (Control) as an orchestrator, making the feature robust and easy to modify.

Finally, I led a significant **refactoring effort to improve code cohesion and extensibility**. I introduced `EntryType` and `CommandType` enums to serve as single sources of truth, centralizing logic that was previously scattered across multiple classes. This change resolved violations of SRP and the Open/Closed Principle (OCP), making the system highly extensible; adding a new entry type now only requires updating the enum, and the relevant parsers and UI components adapt automatically.
### **Contributions to Team-Based Tasks**

* Participated actively in weekly team meetings, contributing to feature planning and task allocation, ensuring that everything is moving accordingly.
* Helped set up initial project structure (github, team repo, organization repo, forking workflow)
* Automated initial V2 user stories making with a shell script

### **Review/Mentoring Contributions**

* Paired with Yu Jie to refactor **CommandResult** refactoring
* Reviewed PR \# 47 (​​Add Weight) : Helped Ryan solve gradle test fail issue on formatting (add newline after end of file) [https://github.com/AY2526S1-CS2113-T11-3/tp/pull/47](https://github.com/AY2526S1-CS2113-T11-3/tp/pull/47)
* Reviewed PR \#56 (refactor, commandException) : Provided feedback on levels of abstraction (violating SRP and SLAP), suggested refactoring [https://github.com/AY2526S1-CS2113-T11-3/tp/pull/56](https://github.com/AY2526S1-CS2113-T11-3/tp/pull/56)

### **Contributions to the User Guide (UG)**

* Added the section explaining the new dashboard command, including format, example usage, and output interpretation.
* Enhanced the existing list command section to accurately describe the new filtering functionality (/t TYPE prefix) and updated the list of valid TYPE values.

### **Contributions to the Developer Guide (DG)**

* Added the design and implementation details for the List Command Filter feature (Section 3.1 in DeveloperGuide.md), including the rationale, flow of control, and sequence diagram link.  
  (Self-note: Ensure the sequence diagram for List Command is added/updated in the DG)
* Added the design and implementation details for the Dashboard Command feature, explaining the DashboardSummary, DashboardFormatter, and DashboardCommand components and their interaction (Section 3.8 in DeveloperGuide.md).
* Briefly documented the rationale and impact of the **EntryType Enum refactoring** within the relevant feature sections or in a general design principles section.
* Added the design and implementation details for the Help Command feature.
