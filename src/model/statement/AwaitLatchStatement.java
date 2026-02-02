package model.statement;

import exceptions.MyException;
import model.ADT.LatchTable.ILatchTable;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class AwaitLatchStatement implements Statement {

    private final String var;

    public AwaitLatchStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        // foundIndex = lookup(symTable, var)
        if (!state.symbolTable().isDefined(var))
            throw new MyException("awaitLatch: var not defined: " + var);

        Value v = state.symbolTable().lookup(var);
        if (!(v instanceof IntegerValue intVal))
            throw new MyException("awaitLatch: var is not int: " + var);

        int foundIndex = intVal.getValue();

        ILatchTable lt = state.latchTable();
        if (!lt.isDefined(foundIndex))
            throw new MyException("awaitLatch: latch index not in table: " + foundIndex);

        int latchValue = lt.lookup(foundIndex);

        // if latchValue == 0 -> do nothing
        // else push back awaitLatch(var)
        if (latchValue != 0) {
            state.executionStack().push(this);
        }

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new AwaitLatchStatement(var);
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        if (!typeEnv.isDefined(var))
            throw new MyException("awaitLatch: var not declared");

        Type varType = typeEnv.lookup(var);
        if (!varType.equals(new IntegerType()))
            throw new MyException("awaitLatch: var is not int");

        return typeEnv;
    }

    @Override
    public String toString() {
        return "awaitLatch(" + var + ")";
    }
}
