package repository;
import exceptions.MyException;
import model.state.ProgramState;

import java.util.List;

public interface IRepository {
    void addProgram(ProgramState p ) ;
   // ProgramState getCurrentProgram(); -> no longer needed for Concurrent ToyLanguage

    void logPrgStateExec(ProgramState state) throws MyException;


    List<ProgramState> getProgramList();

    void setProgramList(List<ProgramState> newProgramList);
}
