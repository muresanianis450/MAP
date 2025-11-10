package model.state;

import exceptions.MyException;
import model.ADT.FileTable.IFileTable;
import model.ADT.List.IList;
import model.ADT.Map.IMap;
import model.ADT.Stack.IStack;
import model.statement.Statement;
import model.value.Value;

public class ProgramState {
    private final IStack<Statement> executionStack;
    private final IMap<String, Value> symbolTable;
    private final IList<Value> out;
    private final IFileTable fileTable;

    public ProgramState(IStack<Statement> executionStack,
                        IMap<String, Value> symbolTable,
                        IList<Value> out,
                        IFileTable fileTable,
                        Statement originalProgram) {

        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;

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


    public ProgramState oneStep() throws MyException {
        if (executionStack.isEmpty()) {
            throw new MyException("ProgramState stack is empty!");
        }

        Statement currentStatement = executionStack.pop();
        return currentStatement.execute(this);
    }


    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    @Override
    public String toString() {
        return "------------------------------------------------------------\n" +
                "ExeStack:\n" + executionStack +
                "\nSymTable:\n" + symbolTable +
                "\nOut:\n" + out +
                "\nFileTable:\n" + fileTable +
                "\n------------------------------------------------------------\n";
    }
}
