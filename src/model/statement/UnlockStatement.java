package model.statement;

import exceptions.MyException;
import model.ADT.LockTable.ILockTable;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class UnlockStatement implements Statement {
    private final String var;

    public UnlockStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMap<String, Value> symTable = state.symbolTable();
        ILockTable lockTable = state.lockTable();

        if (!symTable.isDefined(var))
            throw new MyException("unlock: variable " + var + " not defined");

        Value v = symTable.lookup(var);
        if (!(v instanceof IntegerValue intVal))
            throw new MyException("unlock: variable " + var + " must be int");

        int foundIndex = intVal.getValue();

        synchronized (lockTable) {
            if (!lockTable.isDefined(foundIndex)) {
                // spec: do nothing
                return null;
            }

            int current = lockTable.get(foundIndex);
            if (current == state.id()) {
                lockTable.update(foundIndex, -1);
            }
        }

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new UnlockStatement(var);
    }

    @Override
    public String toString() {
        return "unlock(" + var + ")";
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        if (!typeEnv.isDefined(var))
            throw new MyException("unlock: variable " + var + " not declared");
        if (!typeEnv.lookup(var).equals(new IntegerType()))
            throw new MyException("unlock: " + var + " must be int");
        return typeEnv;
    }
}
