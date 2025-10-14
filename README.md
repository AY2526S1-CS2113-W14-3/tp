# FitChaser

FitChaser is a **Command Line Interface (CLI)** fitness tracker application written in **Java**.  
It allows users to create workouts, add exercises, record sets and repetitions, log durations, and manage their fitness data easily — all within a simple text-based interface.

---

## 📋 Table of Contents
0. [Features](#features)
1. [Usage Example](#usage-example)
2. [Command Summary](#command-summary)
3. [Getting Started](#getting-started)
4. [Project Structure](#project-structure)
5. [Coding Standards](#coding-standards)
6. [Acknowledgements](#acknowledgements)

---

## 🚀 Features

- **Workout Management**
    - Create, view, and delete workouts with date/time.
- **Exercise Tracking**
    - Add exercises, sets, and reps to each workout.
- **Progress Logging**
    - View workout durations and logs.
- **Weight Recording**
    - Record and view your weight history.
- **File Persistence**
    - Data is automatically loaded and saved through `FileHandler`.
- **Simple CLI**
    - All actions are performed through text commands starting with `/`.

---

## 🏋️ Usage Example
/create_workout n/swimming d/13/10/25 t/1500
/add_exercise n/lap r/9
/view_log
/exit

## Command Summary
/help                                - View all commands
/add_weight w/WEIGHT d/DATE          - Record your weight (e.g. /add_weight w/80.5 d/19/10/25)
/create_workout n/NAME d/DATE t/TIME - Create a new workout (e.g. /create_workout n/PushDay d/19/10/25 t/1900)
/add_exercise n/NAME r/REPS          - Add an exercise (e.g. /add_exercise n/Squat r/11)
/add_set r/REPS                      - Add a new set (e.g. /add_set r/9)
/end_workout d/DATE t/TIME           - End the current workout (e.g. /end_workout d/19/10/25 t/2030)
/view_log                            - View your workout history
/del_workout NAME                    - Delete a workout (e.g. /del_workout PushDay)
/exit                                - Save progress and exit the app

## ⚙️ Getting Started
## Setting up in Intellij

Prerequisites: JDK 16 (use the exact version), update Intellij to the most recent version.

0. **Ensure Intellij JDK 17 is defined as an SDK**, as described [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) -- this step is not needed if you have used JDK 17 in a previous Intellij project.
0. **Import the project _as a Gradle project_**, as described [here](https://se-education.org/guides/tutorials/intellijImportGradleProject.html).
0. **Verify the setup**: After the importing is complete, locate the `src/main/java/seedu/duke/Duke.java` file, right-click it, and choose `Run Duke.main()`. If the setup is correct, you should see something like the below:
   ```
   > Task :compileJava
   > Task :processResources NO-SOURCE
   > Task :classes
   
   > Task :FitChaser.main()
   +------------------------------------------------------+
    |                      FITCHASER                       |
    +------------------------------------------------------+
    
    Your virtual gym buddy's clocked in and ready to make you strong!
    Type /help to explore all available commands!
    Let's crush your fitness goals together!
    --------------------------------------------------
    Enter command >
   ```
   Type some word and press enter to let the execution proceed to the end.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.


### Prerequisites
- **Java 16 or above**
- A terminal or command prompt

FAQ

Q: Can I use underscores in workout or exercise names?
A: Yes. Underscores and Spacebars are supported and encouraged for multi-word names.

Q: What happens if I enter an invalid date/time?
A: FitChaser will prompt you with the correct format (dd/MM/yy HHmm).

Q: Where is my data stored?
A: The app creates and saves your workouts into a text file under data (save.txt).