package repository;
import model.state.ProgramState;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private final List<ProgramState> programStates = new ArrayList<>();

    public void addProgram(ProgramState programState){
        programStates.add(programState);
    }

    public List<ProgramState> getProgramStates(){
        return programStates;

    }


    public ProgramState getCurrentProgram(){
        return programStates.get(0);
    }

    @Override
    public String toString(){
        return programStates.toString();
    }

}
