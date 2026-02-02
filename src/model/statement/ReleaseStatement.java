package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.ADT.SemaphoreTable.ISemaphoreTable;
import model.ADT.SemaphoreTable.SemaphoreEntry;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ReleaseStatement implements Statement {
    private final String var;

    public ReleaseStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMap<String, Value> symTable = state.symbolTable();
        ISemaphoreTable semTable = state.semaphoreTable();

        if (!symTable.isDefined(var)) {
            throw new MyException("release: variable " + var + " not defined");
        }
        Value v = symTable.lookup(var);
        if (!v.getType().equals(new IntegerType())) {
            throw new MyException("release: variable " + var + " is not int");
        }
        int foundIndex = ((IntegerValue) v).getValue();

        if (!semTable.isDefined(foundIndex)) {
            throw new MyException("release: index " + foundIndex + " not found in SemaphoreTable");
        }

        synchronized (semTable) {
            SemaphoreEntry entry = semTable.get(foundIndex);
            List<Integer> list1 = entry.getAcquiredBy();

            if (list1.contains(state.id())) {
                List<Integer> newList = new ArrayList<>(list1);
                newList.remove((Integer) state.id());
                semTable.update(foundIndex, entry.withAcquiredBy(newList));
            }
            // else do nothing
        }

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new ReleaseStatement(var);
    }

    @Override
    public String toString() {
        return "release(" + var + ")";
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        if (!typeEnv.isDefined(var)) {
            throw new MyException("release: variable not declared: " + var);
        }
        Type t = typeEnv.lookup(var);
        if (!t.equals(new IntegerType())) {
            throw new MyException("release: var is not int");
        }
        return typeEnv;
    }
}
