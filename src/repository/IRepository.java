package repository;
import exceptions.MyException;
import model.state.ProgramState;
public interface IRepository {
    void addProgram(ProgramState p ) ;
    ProgramState getCurrentProgram();

    void logPrgStateExec() throws MyException;


}
