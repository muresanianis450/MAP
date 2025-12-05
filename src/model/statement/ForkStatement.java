package model.statement;

import model.ADT.Map.IMap;
import model.ADT.Stack.IStack;
import model.ADT.Stack.StackExecutionStack;
import model.state.ProgramState;
import model.value.Value;
import exceptions.MyException;

public class ForkStatement implements Statement {
    private final Statement forkedStatement;

    public ForkStatement(Statement forkedStatement) {
        this.forkedStatement = forkedStatement;
    }

    @Override
    public ProgramState execute(ProgramState parentState) throws MyException {
        // 1. Create a new execution stack for the forked thread
        IStack<Statement> newStack = new StackExecutionStack<>();
        newStack.push(forkedStatement.deepCopy());

        // 2. Clone the parent symbol table (so variables are scoped)
        IMap<String, Value> newSymbolTable = parentState.symbolTable().deepCopy();

        // 3. Heap, Out, FileTable are shared
        return new ProgramState(
                newStack,
                newSymbolTable,
                parentState.out(),        // Shared output
                parentState.fileTable(),  // Shared file table
                parentState.heap(),       // Shared heap
                forkedStatement           // Original program pushed on stack
        );

    }

    @Override
    public Statement deepCopy() {
        return new ForkStatement(forkedStatement.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + forkedStatement + ")";
    }
}
