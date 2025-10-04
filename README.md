# Nand2Tetris Full Course Solution

This repository contains a complete, from-scratch solution to the [Nand2Tetris](https://www.nand2tetris.org/) course, implemented in Java and organized as Gradle projects. It covers all major components, from hardware description to assembler, virtual machine, and compiler.

---

## Project Structure

- **hardware/**  
  Solutions for hardware components (Chapters 1–5), written in HDL.

- **Assembler/**  
  Converts Hack assembly language to binary machine code.  
  **Usage:**  
  ```sh
  gradle run --args="path/to/file.asm"
  ```

- **VMtranslator/**  
  Translates VM code to Hack machine code.  
  **Usage:**  
  ```sh
  gradle run --args="path/to/fileOrDirectory"
  ```

- **Compiler/**  
  Compiles Jack source code (Hack high-level language) to VM code.  
  **Usage:**  
  (From the `Compiler` directory)  
  ```sh
  gradle test
  ```

---

## Getting Started

### Prerequisites

- Java (JDK 8 or higher)
- [Gradle](https://gradle.org/) (or use the included Gradle wrapper)

### Building the Projects

Navigate to the desired project directory (`Assembler`, `VMtranslator`, or `Compiler`) and use:

```sh
gradle build
```
or, if using the Gradle wrapper:
```sh
./gradlew build
```

### Running

See the **Project Structure** section above for details on running each component.

#### Example: Running the Assembler

```sh
cd Assembler
gradle run --args="path/to/Prog.asm"
```

#### Example: Running the VM Translator

```sh
cd VMtranslator
gradle run --args="path/to/FolderOrFile"
```

#### Example: Running Compiler Tests

```sh
cd Compiler
gradle test
```

---

## Credits

- Course: [Nand2Tetris](https://www.nand2tetris.org/)
- Solutions by [Aquib-Nawaz](https://github.com/Aquib-Nawaz)

---

## License

This project is for educational purposes. Please respect the Nand2Tetris academic integrity policy.
