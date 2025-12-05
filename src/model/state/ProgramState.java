package model.state;

import exceptions.MyException;
import model.ADT.FileTable.IFileTable;
import model.ADT.List.IList;
import model.ADT.Map.IMap;
import model.ADT.Heap.IHeap;
import model.ADT.Stack.IStack;
import model.statement.Statement;
import model.type.IntegerType;
import model.value.IntegerValue;
import model.value.Value;

public class ProgramState {

    private static int lastId = 0;

    private final IStack<Statement> executionStack;
    private final IMap<String, Value> symbolTable;
    private final IHeap heap;
    private final IList<Value> out;
    private final IFileTable fileTable;
    private final int id;

    public ProgramState(IStack<Statement> executionStack,
                        IMap<String, Value> symbolTable,
                        IList<Value> out,
                        IFileTable fileTable,
                        IHeap heap,
                        Statement originalProgram
                        ) {

        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.heap = heap;
        this.out = out;
        this.fileTable = fileTable;
        this.id = newId(); //assign unique ID
    }

    public static synchronized int newId() {
        lastId++;
        return lastId;

    }

    public IStack<Statement> executionStack() {
        return executionStack;
    }

    public IMap<String, Value> symbolTable() {
        return symbolTable;
    }

    public IList<Value> out() {
        return out;
    }

    public IFileTable fileTable() {
        return fileTable;
    }

    public IHeap heap() {
        return heap;
    }

    public int id() {
        return id;
    }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    public ProgramState oneStep() throws MyException {
        if (executionStack.isEmpty()) {
            throw new MyException("Execution stack is empty!");
        }
        Statement current = executionStack.pop();
        return current.execute(this);
    }


    @Override
    public String toString() {
        return "------------------------------------------------------------\n" +
                "ID: " + id + "\n" +
                "ExeStack:\n" + executionStack +
                "\nSymTable:\n" + symbolTable +
                "\nOut:\n" + out +
                "\nFileTable:\n" + fileTable +
                "\nHeap:\n" + heap +
                "\n------------------------------------------------------------\n";
    }
}
