package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;


public class NewLatchStatement implements Statement {

    private final String var;
    private final Expression expr;

    public NewLatchStatement(String var, Expression expr) {
        this.var = var;
        this.expr = expr;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value v = expr.evaluate(state.symbolTable(), state.heap());

        if(!(v instanceof IntegerValue intVal))
            throw new MyException("NewLatch: expression is not an integer");

        int num1 = intVal.getValue();

        if(!state.symbolTable().isDefined(var))
            throw new MyException("NewLatch: variable not defined");


        Value cur = state.symbolTable().lookup(var);
        if(!cur.getType().equals(new IntegerType()))
            throw new MyException("NewLatch: variable is not of integer type");


        int index = state.latchTable().allocate(num1); // attomic allocate
        state.symbolTable().update(var, new IntegerValue(index));

        return null;
    }
    @Override
    public Statement deepCopy(){
        return new NewLatchStatement(var, expr.deepCopy());
    }

    @Override
    public IMap<String,Type> typeCheck(IMap<String,Type> typeEnv) throws MyException {
        if(!typeEnv.isDefined(var))
            throw new MyException("NewLatch: variable not declared");

        Type t = typeEnv.lookup(var);
        if(!t.equals(new IntegerType()))
            throw new MyException("NewLatch: variable is not of integer type");

        return typeEnv;
    }

    @Override
    public String toString(){
        return "countDown(" + var + ")";
    }

}
