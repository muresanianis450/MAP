package model.state;

import exceptions.MyException;
import model.ADT.FileTable.IFileTable;
import model.ADT.List.IList;
import model.ADT.Map.IMap;
import model.ADT.Heap.IHeap;
import model.ADT.ProcTable.IProcTable;
import model.ADT.Stack.IStack;
import model.statement.Statement;
import model.type.IntegerType;
import model.value.IntegerValue;
import model.value.Value;
import model.ADT.LockTable.ILockTable;
import model.ADT.BarrierTable.IBarrierTable;
public class ProgramState {

    private static int lastId = 0;

    private final IStack<Statement> executionStack;
 // private final IMap<String, Value> symbolTable;
    private final IHeap heap;
    private final IList<Value> out;
    private final IFileTable fileTable;
    private final int id;



    private final ILockTable lockTable;
    private final IStack<IMap<String,Value>> symTableStack;
    private final IProcTable procTable;
    private final IBarrierTable barrierTable;
    public ProgramState(IStack<Statement> executionStack,
                        IStack<IMap<String, Value>>symbolTableStack,
                        IList<Value> out,
                        IFileTable fileTable,
                        IHeap heap,
                        ILockTable lockTable,
                        IProcTable procTable,
                        IBarrierTable barrierTable,
                        Statement originalProgram
                        ) {

        this.executionStack = executionStack;
        this.heap = heap;
        this.out = out;
        this.fileTable = fileTable;
        this.id = newId(); //assign unique ID

        this.lockTable = lockTable;
        this.symTableStack = symbolTableStack;
        this.procTable = procTable;
        this.barrierTable =  barrierTable;
    }

    public static synchronized int newId() {
        lastId++;
        return lastId;

    }

    public IStack<Statement> executionStack() {
        return executionStack;
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

    public ILockTable lockTable() {
        return lockTable;
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
                //"\nSymTable:\n" + symbolTable +
                "\nSymTableStack:\n" + symTableStack +
                "\nOut:\n" + out +
                "\nFileTable:\n" + fileTable +
                "\nHeap:\n" + heap +
                "\nProcTable:\n" + procTable +
                "\nBarrierTable:\n" + barrierTable +
                "\nLockTable:\n" + lockTable +
                "\n------------------------------------------------------------\n";
    }

    public String getId() {
          return Integer.toString(id);
    }

    public IMap<String, Value> symbolTable() {
        //return symbolTable;
        return symTableStack.peek();
    }
    public IStack<IMap<String,Value>> symTableStack(){
        return symTableStack;
    }

    public void pushSymTable(IMap<String,Value> newTable) {
        symTableStack.push(newTable);
    }

    public void popSymTable() throws MyException {
        if(symTableStack.isEmpty()){
            throw new MyException("SymTable stack is empty!");
        }
        symTableStack.pop();
    }

    public IProcTable procTable() {return procTable;}

    public IBarrierTable barrierTable() {return barrierTable;}
}
