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

###  1. Repository Logging
- Added `logPrgStateExec()` method to **Repository interface**.
- Repository logs the **ProgramState** to a text file in append mode.
- Log includes sections:
  - **ExeStack**
  - **SymTable**
  - **Out**
  - **FileTable**
- Each stack element is printed in **infix form** (left-root-right traversal).

### 2. File Operations
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
- Added `Command` abstract class with subclasses:
        -`ExitCommand` -> exit program
        -`RunExample` -> execute a program via Controller

### Example program for this changes
```java
int a;
int b;
print(a < b); //true
print(a >= b); //false
```
## Changes added on 26.11.2025🔨

###  1. Heap Management & Reference Types

- `RefType`:
  ```java
    Ref(int)
    Ref(bool)
    //Nested
    Ref(Ref(int))
  ```
- `RefValue`
```java
// RefValue(<heapAddr>,<locationType>);
new RefValue(1,IntType);
new RefValue(10,RefType(IntType));
```

###  2. Heap Table Implementation
- Internally stored as a **HashMap<Integer,Value>**
- Tracks the **next free address**

### 3. New Heap-Based Statements & Expressions

- `new(var,expr)`
  - Evaluates expr
  - Allocates a new heap cell
  - Updates var with **RefValue(newAddress,locationType)**;


```java
Heap:     {1 → 20, 2 → (1,int)}
SymTable: {v → (1,int), a → (2,Ref int)}
Out:      {(1,int), (2,Ref int)}

```

####  `rh(expr)` - Read from heap
- Evaluates expr -> must be RefValue
- Reads the value stored at the address
- Updates eval() signature : **Value eval(symTable,heap)**
####  `wH(var,expr) `- Write to heap
- var exists and has RefType
- The address inside var is in the heap
- Expression type matches locationType
- Replaces heap content at that address

```java
new(v,20); print(rH(v));
wH(v,30); print(rH(v)+5);
```

### 4. Garbage Collector(Safe Version)
- Implemented a garbage collector that automatically removes unreachable heap entries.
- This ensures the heap contains only values that are **still accessible** either directly form the SymbolTable or indirectly through chains of references stored inside the heap itself.

#### How it works?🔍

---

1. Collect Directly Referenced Addresses
- It starts from all reference values stored in the Symbol Table.
- For every RefValue found, its address is added to the toVisit queue.
```java
if (v instanceof RefValue ref)
    toVisit.add(ref.getAddress());
```

2. BFS Through the Heap(Indirect References)
- The collector then performs a Breadth-First Search through the heap:
  - Takes the next heap address from the queue
  - Marks it as visited
  - If the value stored at that address is another RefValue, its address is added back to the queue
```java
while (!toVisit.isEmpty()) {
    int addr = toVisit.poll();
    if (!visited.contains(addr) && heap.containsKey(addr)) {
        visited.add(addr);

        Value heapVal = heap.get(addr);
        if (heapVal instanceof RefValue refInHeap) {
            toVisit.add(refInHeap.getAddress());
        }
    }
}
```

3. Heap Filtering
- Once the set of all **reachable addresses** is computer, the collector creates a **new heap** that keeps only those entries
```java
return heap.entrySet().stream()
    .filter(e -> reachable.contains(e.getKey()))
    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
```

### 5. While Statement
- Added the language construct:
```java
while (expression) statement
```
- Expression must evaluate to BoolValue
- If **false** -> loop ends
- If **true** -> Pushes:
  - the loop body
  - the while statement(for next iteration)
```java
int v; v = 4;
while (v > 0)
    print(v);
    v = v - 1;
print(v);
```


---

## Changes added on 31.01.2026🔨

# 1. Switch Statement Added

### How it works? 🔍

```java
switch(exp)
(case exp1: stmt1)
(case exp2: stmt2)
(default: stmt3)
```

## Changes added on 05.02.2026

This update significantly extends the language with **advanced control flow**, **procedures**, and multiple **synchronization mechanisms** for concurrent execution.

---

# 1. Advanced Control Flow Constructs ⛓️

### Conditional Assignment 

Added support for:

```java
v = exp1 ? exp2 : exp3
```
#### How it works?🔍
```java
if (exp1)
    v = exp2;
else
    v = exp3;
```
Type Safety
- exp1 must be **bool**
- exp2, exp3 and v must have the **same type**
- Variable must already be declared

### Repeat-Until Statement
```java
repeat(stmt) until(condition)
```
#### How it works?🔍
Desugars into:
```java
stmt;
while(!condition){
    stmt;}
```
Type Safety:
- The condition must be **bool**
- Ensures the body runs at least once

### For Statement
```java
for ( v = exp1 ; v < exp2; v = exp3) stmt
```
#### How it works?🔍
Desugars into:
```java
int v;
v = exp1;
while( v < exp2){
    stmt;
    v = exp3;
        }
```
Type Safety:
- exp1, exp2, exp3 must be **int**
- v is locally declared **inside the for**
- Variable must not already exist in the current scope


### While Statement
```java
while(condition) stmt
```
Type Safety:
- Condition must be **bool**


# 2. Time-Control & Execution Delay ⌛🕛

### Sleep Statement
```java
sleep(n)
```
#### How it works?🔍
- If **n > 0** -> pushes **sleep(n-1)** to ExecutionStack
- If **n == 0** -> does nothing

### Wait Statement
```java
wait(n)
```
#### How it works?🔍
Desugars into:
```java
print(n);
wait(n-1);
```

# 3. Synchronization Mechanisms( Concurrency Support) ⛓️‍💥⛓️‍💥
The interpreter now includes multiple synchronization primitives implemented using atomic operations( Java **synchronized** blocks and **ReentrantLock**).

## 🔒 Lock Mechanism
```java
lock(var)
```
#### How it works?🔍
- **var** must store an integer index
- If lock is **free** (-1) -> current program acquires it
- If locked -> statement is pushed back on stack (retry later)

## ⛓️ Latch Mechanism

### CountDown Statement
```java
countDown(var)
```
- **var** must contain latch index
- If latch value > 0 -> **decremented atomically**
- Always **writes current ProgramStateID** into Out

### AwaitLatch Statement
```java
awaitLatch(var)
```
- If latch value == 0 -> **continues execution**
- Else -> **pushes itself back (waits)**

## 🚦 Semaphore Mechanism
### Acquire Statement
```java
acquire(var)
```
- **var** must contain **semaphore index**
- If available permits exist:
  - Adds current **ProgramState ID* to acquired list
- If no permits:
  - Pushes itself back on execution stack

### Release Statement
```java
release(var)
```
- **Removes** current ProgramState ID from **semaphore holder list**
- If not holding the semaphore -> does nothing

## 🚧 Barrier Mechanism
### New Barrier Statement
```java
newBarrier(var,exp)
```
- **exp** must evaluate to integer N
- Allocates barrier with:
  - Required threads = N
  - Waiting list = empty
- Stores generated barrier index into var

### Barrier Table Structure
Each entry:
```java
(index) -> (required, waitingList)
```
- **required** = number of threads needed
- **waitingList** = ProgramState IDs currently waiting

# 4. Procedure Support 🫂
### ProcTable:
- Maps procedure name -> (parameter list, body)
- Prevents duplicate procedure definitions
- Lookup throws exception if undefined
```java
procedureName -> (List<String> params, Statement body)
```
