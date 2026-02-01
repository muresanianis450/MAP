package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;


public class SleepStatement implements Statement {

    private final int number;

    public SleepStatement(int number) {
        this.number = number;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        //pop already happened in oneStep()

        if(number > 0 ){
            state.executionStack().push(new SleepStatement(number -1));
        }
        return null;
    }

    @Override
    public Statement deepCopy(){
        return new SleepStatement(number);
    }

    @Override
    public IMap<String,Type> typeCheck(IMap<String,Type> typeEnv) throws MyException {
        //sleep(number) -> number is a int
        if(number < 0 ){
            throw new MyException("SleepStatement number is negative");
        }
        return typeEnv;
    }

    @Override
    public String toString(){
        return "sleep(" + number + ")";
    }

}
