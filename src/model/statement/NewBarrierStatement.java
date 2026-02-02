package model.statement;

import exceptions.MyException;
import model.ADT.BarrierTable.IBarrierTable;
import model.ADT.Map.IMap;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class NewBarrierStatement implements Statement{
    private final String var;
    private final Expression exp;

    public NewBarrierStatement(String var, Expression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        Value v = exp.evaluate(state.symbolTable(),state.heap());
        if(!(v instanceof IntegerValue intVal))
            throw new MyException("newBarrier: exp is not int");

        int nr = intVal.getValue();

        //var must exist and be int
        if(!state.symbolTable().isDefined(var))
            throw new MyException("newBarrier: var is not defined: " + var);

        Value current = state.symbolTable().lookup(var);

        if(!current.getType().equals(new IntegerType()))
            throw new MyException("newBarrier: variable not int: " + var);

        IBarrierTable bt = state.barrierTable();
        int index = bt.allocate(nr); // atomic allocate

        state.symbolTable().update(var,new IntegerValue(index));
        return null;
    }

    @Override
    public Statement deepCopy(){
        return new NewBarrierStatement(var, exp.deepCopy());
    }

    @Override
    public IMap<String,Type> typeCheck (IMap<String,Type> typeEnv) throws MyException{
        if(!typeEnv.isDefined(var))
            throw new MyException("newBarrier: variable not declared: " + var);

        Type varType = typeEnv.lookup(var);
        Type expType = exp.typeCheck(typeEnv);

        if(!varType.equals(new IntegerType()))
            throw new MyException("newBarrier: variable not int: " + var);
        if(!expType.equals(new IntegerType()))
            throw new  MyException("newBarrier: expression not int: " + exp);
        return typeEnv;
    }
    @Override
    public String toString(){
        return "newBarrier(" + var + ", " + exp + ")";
    }

}
