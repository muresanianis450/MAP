package model.statement;
import model.state.ProgramState;
import exceptions.MyException;
public interface Statement {
    ProgramState execute(ProgramState state) throws MyException;
}
