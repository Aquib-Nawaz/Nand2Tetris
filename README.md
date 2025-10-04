# Nand2Tetris Full Course Solution

This repository contains a comprehensive, Java-based solution to the [Nand2Tetris](https://www.nand2tetris.org/) course (The Elements of Computing Systems), which guides learners through building a modern computer system from first principles, starting with basic logic gates and culminating in a working computer with a compiler and operating system.

## About Nand2Tetris

Nand2Tetris is a project-based course that takes you on a journey through all the major layers of computer systems. You begin by constructing simple hardware components in HDL, progress to building a CPU and a virtual machine, and finally develop a compiler for a high-level programming language (Jack).

The course is structured as a series of projects:
- **Chapters 1–5:** Digital logic, building up from basic gates (NAND) to the ALU and computer architecture, implemented in HDL.
- **Chapters 6–7:** Writing an assembler to translate Hack assembly to binary machine code.
- **Chapters 7–8:** Building a virtual machine translator that converts stack-based VM code into Hack assembly.
- **Chapters 9–11:** Developing a compiler that translates the Jack language into VM commands.

## Repository Structure

- **hardware/**  
  HDL solutions for Chapters 1–5, including logic gates, ALU, memory, and CPU design.
- **Assembler/**  
  Java implementation of the Hack assembler (Chapter 6), converting `.asm` to `.hack` binary files.
- **VMtranslator/**  
  Java implementation of the VM-to-Hack translator (Chapters 7–8), handling both arithmetic and memory access commands.
- **Compiler/**  
  Java implementation of the Jack compiler (Chapters 9–11), translating `.jack` high-level code to VM commands.

## How to Use

This repository uses Gradle for building and running Java code.  
Requirements: **Java 8+**, **Gradle** (or use included Gradle wrapper).

- **Assembler:** Converts Hack assembly to binary machine code.
  - `gradle run --args="path/to/file.asm"` (from `Assembler/` directory)
- **VMtranslator:** Converts VM code (file or directory) to Hack assembly.
  - `gradle run --args="path/to/fileOrFolder"` (from `VMtranslator/` directory)
- **Compiler:** Compiles Jack code to VM code.
  - `gradle test` (from `Compiler/` directory)

## Learning Outcomes

- Understand digital logic and hardware design by building real components in HDL.
- Gain insights into assembler design and machine code translation.
- Learn about stack-based virtual machines and their implementation.
- Develop a complete compiler for a custom high-level language.

## Credits

- Based on the [Nand2Tetris](https://www.nand2tetris.org/) course by Noam Nisan and Shimon Schocken.
- Solutions by [Aquib-Nawaz](https://github.com/Aquib-Nawaz).

## License

For educational use only. Please respect the Nand2Tetris academic honesty policy.
