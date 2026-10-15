# 🐢 Scratch Turtle Engine — JavaFX MVVM Interpreter

[![Java](https://img.shields.io/badge/Java-21%20LTS%20%2F%2025-orange?logo=openjdk)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-blue?logo=openjfx)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/Testing-JUnit%205-25A162?logo=junit5)](https://junit.org/junit5/)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Command%20Pattern-informational)](#software-architecture--design)

> **Execution engine and visual programming environment inspired by Logo and Scratch.**  
> Implemented in modern Java using a reactive **MVVM** architecture, featuring static semantic pre-compilation, a symbol table memory store, and an extensible step-by-step vector runtime governed by an internal call stack.

---


## 🎯 Project Overview

This desktop application provides a complete visual programming environment designed to compose, validate, and execute vector drawing algorithms (Turtle Graphics). Users build programs using an action palette, configure dynamic variables, nest loops, and observe step-by-step or automated execution rendered on a real-time vector canvas.

### Key Highlights
* **Extensible Action Palette:** Displacement, trigonometric rotation, pen state management, loop blocks, and runtime variable operations.
* **Static Semantic Verification (Integrated Compiler):** Validates program integrity prior to execution (unbound variable detection, loop closure balance, scope safety, and state conflict prevention).
* **Dual Execution Runtime:**
  * **Manual Mode:** Interactive step-by-step execution for debugging and state inspection.
  * **Automated Mode:** Real-time playback governed by a JavaFX `Timeline`, with dynamic execution speed controls via reactive bindings.
* **Call Stack & Symbol Table Management:** Dedicated stack-based memory management (`ExecutionState`) handling nested loops, iteration counters, and variable scopes.
* **Custom Script Persistence:** Custom serialization and deserialization engine for importing and exporting `.scr` program scripts.

---

## 🏗️ Software Architecture & Design

The application enforces a strict separation of concerns, decoupling the execution runtime, memory state, and graphical presentation layer.

```text
src/main/java/scratch/
├── model/                  # Domain model, state management & execution runtime
│   ├── actions/            # Command Pattern implementations
│   ├── ActionFactory.java  # Script serialization / deserialization factory
│   ├── ExecutionState.java # Virtual machine runtime (Call stack & program counter)
│   ├── Program.java        # Static semantic analyzer / Pre-compiler
│   ├── Scene.java          # Vector geometry model & cursor entity
│   └── Variable_Table.java # Symbol table (runtime variable store)
├── viewmodel/              # Presentation logic (JavaFX Properties & Bindings)
│   ├── MainViewModel.java
│   ├── ProgramViewModel.java
│   ├── ExecutionViewModel.java
│   └── SceneViewModel.java
└── view/                   # UI components (Canvas 2D rendering, ListView, Controls)
    ├── MainView.java
    ├── SceneView.java      # Real-time graphics renderer
    ├── ProgramView.java
    └── ExecutionView.java
```

### Design Patterns & Engineering Highlights
1. **Model-View-ViewModel (MVVM):** The presentation layer observes reactive properties (`ObservableList`, `DoubleProperty`, `BooleanBinding`) published by the ViewModels. UI event handlers never mutate the domain model directly.
2. **Command Pattern:** Every instruction (`Move_Forward`, `Turn_Left`, `Repeat`, `Var_Assignment`, etc.) implements a common abstract contract `Action`, encapsulating its own behavior in `execute(ExecutionState)`.
3. **Stack-Based Virtual Machine (Call Stack Engine):**
   * Loop execution and nesting are handled via a dedicated call stack (`Stack<Integer>` for instruction pointers and loop counters), preventing execution drift and supporting arbitrary nesting depth.
4. **Static Semantic Analysis (Pre-run Compiler):**
   * Before runtime instantiation, `compileProgram()` statically enforces language constraints:
     * Variable declaration preceding utilization and unique identifier rules.
     * Balanced control structures (`Repeat` / `End_Repeat` boundaries).
     * Mutation protection against modifying active loop counter variables within nested scopes.

---

## 💡 Engineering Decisions & Trade-Offs

* **Command Pattern vs Switch-Case Dispatcher:** Rather than routing execution through a monolithic procedural switch statement, each instruction encapsulates its own validation, execution, and string serialization logic. This makes adding new instructions frictionless without modifying the core execution loop.
* **Static Verification Prior to Execution:** Programs are compiled