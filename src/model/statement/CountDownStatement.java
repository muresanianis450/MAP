package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class CountDownStatement implements Statement {

    private final String var;

    public CountDownStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        // 1) var must exist in SymTable
        if (!state.symbolTable().isDefined(var)) {
            throw new MyException("countDown: variable not defined: " + var);
        }

        Value v = state.symbolTable().lookup(var);

        // 2) var must be int
        if (!(v instanceof IntegerValue intVal)) {
            throw new MyException("countDown: variable is not int: " + var);
        }

        int foundIndex = intVal.getValue();

        // 3) foundIndex must exist in latch table
        if (!state.latchTable().isDefined(foundIndex)) {
            throw new MyException("countDown: latch index not found: " + foundIndex);
        }

        // 4) atomic lookup + update is handled inside latchTable methods (synchronized/lock)
        int current = state.latchTable().lookup(foundIndex);

        if (current > 0) {
            state.latchTable().update(foundIndex, current - 1);
        }

        // 5) always write program id into Out
        state.out().add(new IntegerValue(state.id()));

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new CountDownStatement(var);
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        if (!typeEnv.isDefined(var)) {
            throw new MyException("countDown: variable not declared: " + var);
        }

        Type t = typeEnv.lookup(var);
        if (!t.equals(new IntegerType())) {
            throw new MyException("countDown: variable is not int: " + var);
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "countDown(" + var + ")";
    }
}
