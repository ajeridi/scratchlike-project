# 🐢 Scratch Turtle Engine — JavaFX MVVM Interpreter

![Java](https://img.shields.io/badge/Java-21%20LTS%20%2F%2025-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-4285F4?style=for-the-badge&logo=java&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-success?style=for-the-badge)
![Patterns](https://img.shields.io/badge/Design%20Patterns-Command%20%7C%20Composite-blueviolet?style=for-the-badge)
![Modeling](https://img.shields.io/badge/Design-PlantUML-orange?style=for-the-badge)

An extensible visual programming environment and execution engine inspired by **Scratch** and **Logo Turtle Graphics**, built with modern **Java** and **JavaFX**.

This project was engineered for the **"Software Analysis & Design Project"** (*Projet d'Analyse et de Conception Logicielle*) course within the Bachelor in Applied Computer Science program at **EPFC (Brussels)**. It demonstrates a formal software engineering methodology where domain modeling and UML design systematically guided the entire implementation phase.

---

## 📽️ Live Demonstration

Real-time automated execution of an algorithmic geometric vortex script (`examples/demo_spirale.scr`): synchronized step-by-step instruction tracking, reactive vector canvas rendering, and dynamic symbol table evaluation.

<p align="center">
  <img src="docs/assets/demo-spiral.gif" alt="Scratch Engine Execution Demo" width="850">
</p>

---

## 🎯 Engineering Objectives & Pedagogy

Rather than simply assembling a graphical UI, this project focuses on robust software engineering fundamentals:
1. **Domain-Driven Understanding:** Formalizing the semantics of imperative execution (sequences, loops, variables, Cartesian geometry) before writing code.
2. **Prior UML Architectural Design:** Specifying object relationships, polymorphic contracts, and state management via formal PlantUML diagrams prior to implementation.
3. **Strict MVVM Decoupling:** Isolating the core domain execution model from JavaFX UI controls through reactive bidirectional data binding (`ObservableList`, `DoubleProperty`, `BooleanBinding`).
4. **Compile-Time Semantic Analysis:** Implementing a static verification pass to detect unbalanced control structures, scope conflicts, and runtime illegal states before program execution.

---

## 📐 1. Analysis & UML Design Phase

In accordance with software engineering best practices, design preceded code implementation through two complementary views:

### A. Domain Model (Conceptual Business View)
* Captures high-level business concepts: an executable `Program` aggregates an ordered list of `Action` commands running against an `ExecutionState` that manages a vector `Cursor`, geometric lines, and a `VariableTable`.

<p align="center">
  <img src="docs/uml/diagramme_classes.svg" alt="UML Domain Model" width="850">
</p>

* Source file: [`docs/uml/diagramme_classes.puml`](docs/uml/diagramme_classes.puml)
---

### B. Detailed Software Class Diagram (Implementation View)
* Specifies method signatures, inheritance structures (`Target_Action`, `Turn`, `Pen`, `Repeat`, `End_Repeat`), JavaFX observable property types, and loose coupling via ViewModel abstraction layers.

<details>
<summary><b>🔍 Click to expand the full Detailed Software Class Diagram</b></summary>

<br>

<p align="center">
  <a href="docs/uml/diagramme_classe_logiciel.png" target="_blank">
    <img src="docs/uml/diagramme_classe_logiciel.svg" alt="Detailed Software Class Diagram" width="100%">
  </a>
  <br>
  <em>(Click on the image or open the source file to inspect in full resolution)</em>
</p>

</details>

* Source file: [`docs/uml/diagramme_classe_logiciel.puml`](docs/uml/diagramme_classe_logiciel.puml)
---

## 🏗️ 2. Software Architecture & Implementation

```text
src/main/java/scratch/
├── model/                     # Independent domain core & execution runtime
│   ├── actions/               # Command Pattern: concrete executable instructions
│   ├── ActionFactory.java     # Script serialization & deserialization (.scr)
│   ├── ExecutionState.java    # Stack-based runtime (Call stack & program counter)
│   ├── Program.java           # Static semantic analyzer / pre-compiler
│   ├── Scene.java             # Vector geometry model & turtle cursor state
│   └── Variable_Table.java    # Dynamic symbol table store
├── viewmodel/                 # Presentation logic & reactive state bindings
│   ├── MainViewModel.java     # Central mediator orchestrating sub-viewmodels
│   ├── ProgramViewModel.java  # Script hierarchy, selection, and mutation bindings
│   ├── ExecutionViewModel.java# Timeline ticker, speed controls, and execution signals
│   ├── SceneViewModel.java    # Vector canvas rendering state
│   └── InformationViewModel.java # Telemetry and symbol table synchronization
└── view/                      # JavaFX presentation layer
    ├── MainView.java          # Primary application layout & menu bar
    ├── SceneView.java         # Double-buffered vector canvas renderer
    ├── ProgramView.java       # Visual script editor with automatic indentation
    ├── ExecutionView.java     # Execution dashboard (Auto/Manual controls, slider)
    └── InformationView.java   # Real-time turtle telemetry and TableView of variables
```
---

### Core Architectural Highlights
* **Command Pattern:** Each instruction (`Move_Forward`, `Turn_Right`, `Repeat`, `Var_Assignment`, etc.) extends the abstract `Action` base class, encapsulating its own parameters, validation logic, and runtime behavior inside `execute(ExecutionState)`. Adding new operations requires zero modifications to the execution engine (Open/Closed Principle).
* **Call Stack Virtual Machine:** Nested loop resolution is governed by dedicated call stacks (`Stack<Integer>` for loop indices and iteration counters inside `ExecutionState`), supporting arbitrary loop nesting depths without recursion overhead.
* **Static Semantic Compiler (`Program.compileProgram()`):** Protects the runtime environment by validating script integrity before execution:
  * Strict declaration-before-use variable placement.
  * Structural balance of control blocks (`Repeat` vs. `End_Repeat`).
  * Safety invariants preventing loop counters from being modified within their nested scopes.
* **Dual Execution Modes:**
  * *Manual Mode:* Step-by-step interactive inspection and debugging.
  * *Automated Mode:* Continuous real-time execution governed by a JavaFX `Timeline`, dynamically synchronized with an execution speed slider.

---

## 👤 Author & Context

* **Student:** Aymen JERIDI — Bachelor in Applied Computer Science (Application Development) at **EPFC (Brussels)**
---

> **⚠️ Portfolio & Academic Notice:**  
> This project was developed strictly for academic demonstration and portfolio purposes as part of a software engineering coursework. It is **not** intended, designed, or maintained for production use.