package model.statement;

import exceptions.MyException;
import model.ADT.BarrierTable.BarrierEntry;
import model.ADT.BarrierTable.IBarrierTable;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

import java.util.ArrayList;
import java.util.List;

public class AwaitStatement implements Statement{


    private final String var;

    public AwaitStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        // foundIndex = lookup(symTable, var)
        if (!state.symbolTable().isDefined(var))
            throw new MyException("await: var not defined: " + var);

        Value v = state.symbolTable().lookup(var);
        if (!(v instanceof IntegerValue intVal))
            throw new MyException("await: var is not int: " + var);

        int foundIndex = intVal.getValue();

        IBarrierTable bt = state.barrierTable();
        if (!bt.isDefined(foundIndex))
            throw new MyException("await: barrier index not in table: " + foundIndex);

        // retrieve entry (N1, L1)
        BarrierEntry entry = bt.lookup(foundIndex);
        int n1 = entry.getRequired();
        List<Integer> l1 = entry.getWaiting();
        int nl = l1.size();

        if (n1 > nl) {
            int id = state.id();
            if (l1.contains(id)) {
                // push back await(var)
                state.executionStack().push(this);
            } else {
                // add id to list, update atomically, push back await
                List<Integer> newList = new ArrayList<>(l1);
                newList.add(id);
                bt.update(foundIndex, new BarrierEntry(n1, newList));
                state.executionStack().push(this);
            }
        }
        // else do nothing

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new AwaitStatement(var);
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        if (!typeEnv.isDefined(var))
            throw new MyException("await: var not declared");

        Type varType = typeEnv.lookup(var);
        if (!varType.equals(new IntegerType()))
            throw new MyException("await: var is not int");

        return typeEnv;
    }

    @Override
    public String toString() {
        return "await(" + var + ")";
    }

}
