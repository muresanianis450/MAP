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

public class AcquireStatement implements Statement {
    private final String var;

    public AcquireStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMap<String, Value> symTable = state.symbolTable();
        ISemaphoreTable semTable = state.semaphoreTable();

        // foundIndex = lookup(symTable, var)
        if (!symTable.isDefined(var)) {
            throw new MyException("acquire: variable " + var + " not defined");
        }
        Value v = symTable.lookup(var);
        if (!v.getType().equals(new IntegerType())) {
            throw new MyException("acquire: variable " + var + " is not int");
        }

        int foundIndex = ((IntegerValue) v).getValue();

        // check if the semaphore table contains an entry for that index
        if (!semTable.isDefined(foundIndex)) {
            throw new MyException("acquire: index " + foundIndex + " not found in SemaphoreTable");
        }

        // atomic critical section for acquire logic
        synchronized (semTable) {

            SemaphoreEntry entry = semTable.get(foundIndex);
            int N1 = entry.getPermits(); //entry's permit count
            List<Integer> list1 = entry.getAcquiredBy(); //current holders list
            int NL = list1.size();

            if (N1 > NL) {
                if (list1.contains(state.id())) {
                    // if it is already in that list, do nothing
                } else {
                    //add it to the list
                    List<Integer> newList = new ArrayList<>(list1);
                    newList.add(state.id());
                    semTable.update(foundIndex, entry.withAcquiredBy(newList));
                }
            } else {
                // push back acquire(var)
                state.executionStack().push(this);
            }
        }

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new AcquireStatement(var);
    }

    @Override
    public String toString() {
        return "acquire(" + var + ")";
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        if (!typeEnv.isDefined(var)) {
            throw new MyException("acquire: variable not declared: " + var);
        }
        Type t = typeEnv.lookup(var);
        if (!t.equals(new IntegerType())) {
            throw new MyException("acquire: var is not int");
        }
        return typeEnv;
    }
}
