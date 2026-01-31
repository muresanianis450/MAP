package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.Type;

public class SkipStatement implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        // do nothing
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new SkipStatement();
    }

    @Override
    public String toString() {
        return "skip";
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) {
        return typeEnv;
    }
}
