# 🧠 Toy Language Interpreter (Java)

A small **Toy Language Interpreter** built in **Java**, designed to simulate the inner workings of a simple programming language.  
It executes statements step-by-step while showing the evolution of the **Execution Stack**, **Symbol Table**, and **Output List** — giving a clear view of how programs run internally.

---

## Changes added on 26.10.2025🔨

### Language Components
- **Variables and Types** (`int`, `bool`)
- **Expressions**
  - Constant and Variable expressions
  - Arithmetic operations: `+`, `-`, `*`, `/`
  - Logical and relational operations *(optional for extension)*
- **Statements**
  - `VariableDeclarationStatement` — declares a variable
  - `AssignmentStatement` — assigns an expression value to a variable
  - `PrintStatement` — outputs a value
  - `IfStatement` — conditional execution
  - `CompoundStatement` — sequential composition
  - `NoOperationStatement` — represents a no-op

---

### Execution Model
Each program runs as a **Program State**, containing:
- **Execution Stack** — pending statements
- **Symbol Table** — current variable bindings
- **Output List** — program results so far

The interpreter executes programs **step-by-step**, showing internal state transitions like:


--- 
## Changes added on 11.10.2025🔨

#### 1. Repository Logging
- Added `logPrgStateExec()` method to **Repository interface**.
- Repository logs the **ProgramState** to a text file in append mode.
- Log includes sections:
  - **ExeStack**
  - **SymTable**
  - **Out**
  - **FileTable**
- Each stack element is printed in **infix form** (left-root-right traversal).

#### 2. File Operations
- Implemented **FileTable** (filename → BufferedReader).
- Supports only text files with positive integers, one per line.
- Statements added:
  - `openRFile(exp)` — opens a file if not already opened
  - `readFile(exp, var_name)` — reads integer from file into variable
  - `closeRFile(exp)` — closes the file and removes it from FileTable
- Tested with:
```java
string varf;
varf = "test.in";
openRFile(varf);
int varc;
readFile(varf, varc); print(varc);
readFile(varf, varc); print(varc);
closeRFile(varf);
```
### 3. Type System
- Implemented **defaultValues()** in all types
- Variables declared using **VariableDeclareStatement** -> now initialized with **defaultValue()**
- Added **StringType** and **StringValue** classes

### 4. Relational Expressions
- Supports '<' '>' '==' '!=' '<=' '>='.

### 5. View (MVC Text Menu)
- Added **Command** abstract class with subclasses:
        -**ExitCommand** -> exit program
        -**RunExample** -> execute a program via Controller

### Example program for this changes
```java
int a;
int b;
print(a < b); //true
print(a >= b); //false
```
