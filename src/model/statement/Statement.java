package model.statement;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import exceptions.MyException;
import model.type.Type;
public interface Statement {
    ProgramState execute(ProgramState state) throws MyException;
    Statement deepCopy();

    IMap <String,Type> typeCheck(IMap<String,Type> typeEnv) throws MyException;

}
