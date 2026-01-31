package model.statement;

import exceptions.MyException;
import model.ADT.LockTable.ILockTable;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;


public class LockStatement implements Statement {

    private final String var;


    public LockStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMap<String,Value> symTable = state.symbolTable();
        ILockTable lockTable = state.lockTable();

        if(!symTable.isDefined(var))
            throw new MyException("Variable " + var + " is not defined");

        Value v = symTable.lookup(var);

        if(!(v instanceof IntegerValue intVal))
            throw new MyException("Variable " + var + " is not a number");

        int foundIndex = intVal.getValue();


        //atomic check + update (host language locking)

        synchronized (lockTable) {
            if(!lockTable.isDefined(foundIndex))
                throw new MyException("lock: index" + foundIndex + " is not defined");

            int current = lockTable.get(foundIndex);
            if(current == -1){
                lockTable.update(foundIndex,state.id());

            }else{
                //someone else has it -> retry later
                state.executionStack().push(this);
            }
        }
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new LockStatement(var);
    }

    @Override
    public String toString() {
        return "lock(" + var + ")";
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        if (!typeEnv.isDefined(var))
            throw new MyException("lock: variable " + var + " not declared");
        if (!typeEnv.lookup(var).equals(new IntegerType()))
            throw new MyException("lock: " + var + " must be int");
        return typeEnv;
    }

}
