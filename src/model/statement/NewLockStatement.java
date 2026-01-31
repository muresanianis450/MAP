package model.statement;


import exceptions.MyException;
import model.ADT.LockTable.ILockTable;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;


public class NewLockStatement implements Statement {

    private final String var;

    public NewLockStatement(String var) {
        this.var = var;
    }
    @Override
    public Statement deepCopy() {
        return new NewLockStatement(var);
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMap<String,Value> symTable = state.symbolTable();
        ILockTable lockTable = state.lockTable();

        //allocate new lock location in a thread-safe way

        int newFreeLocation = lockTable.allocate(); //already synchronized

        //store index in var (add or update)

        if(symTable.isDefined(var)){
            symTable.update(var,new IntegerValue(newFreeLocation));
        }
        else {
            symTable.add(var,new IntegerValue(newFreeLocation));
        }
        return null;
    }

    @Override
    public IMap<String,Type> typeCheck(IMap<String,Type> typeEnv) throws MyException {
        //var should be int

        if (typeEnv.isDefined(var)){
            Type t = typeEnv.lookup(var);
            if(!t.equals(new IntegerType()))
                throw new MyException("Type "+var+" must be int");
        }else{
            //if undeclared, we treat it as int after newLock
            typeEnv.add(var,new IntegerType());
        }
        return typeEnv;
    }


}
