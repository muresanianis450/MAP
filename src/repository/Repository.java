package repository;
import model.ADT.BarrierTable.BarrierEntry;
import model.value.Value;
import exceptions.MyException;
import model.state.ProgramState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.ADT.LockTable.ILockTable;
import model.ADT.BarrierTable.BarrierTable;
public class Repository implements IRepository {

    private List<ProgramState> programStates;
    private final String logFilePath;

    public Repository(String logFilePath) {
        this.logFilePath = logFilePath;
        this.programStates = new ArrayList<>();
    }



    @Override
    public void addProgram(ProgramState programState) {
        //only 1 initial program is added, forks create more later
        programStates.add(programState);
    }

    /*@Override
    public ProgramState getCurrentProgram() {
        return programStates.get(0);
    }*/

    @Override
    public void logPrgStateExec(ProgramState state) throws MyException {
        //ProgramState state = getCurrentProgram();
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {

            logFile.println("------------------------------------------------------------");
            logFile.println("Program ID: "+ state.id());

            logFile.println("ExeStack:");
            logFile.println(state.executionStack());  // will rely on stack's custom toString()

           // logFile.println("SymTable:");
           // logFile.println(state.symbolTable());

            logFile.println("SymTableStack:");
            logFile.println(state.symTableStack());

            logFile.println("ProcTable:");
            logFile.println(state.procTable());

            logFile.println("Out:");
            logFile.println(state.out());

            logFile.println("FileTable:");
            logFile.println(state.fileTable());

            logFile.println("Heap:");
            Map<Integer,Value> heapContent = state.heap().getContent();
            for(Map.Entry<Integer, Value> entry : heapContent.entrySet()) {
                logFile.println(entry.getKey() + " -> " + entry.getValue().toString() + "\n");
            }

            logFile.println("LockTable:");
            Map<Integer, Integer> lockContent = state.lockTable().getContent();
            for (Map.Entry<Integer, Integer> entry : lockContent.entrySet()) {
                logFile.println(entry.getKey() + " -> " + entry.getValue());
            }

            logFile.println("BarrierTabel:");
            Map<Integer, BarrierEntry> barrierContent = state.barrierTable().getContent();
            for(var entry : barrierContent.entrySet()){
                logFile.println(entry.getKey() + " -> "  + entry.getValue());
            }

            logFile.println("------------------------------------------------------------\n");

        } catch (IOException e) {
            throw new MyException("Error writing to log file: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return programStates.toString();
    }

    @Override
    public List<ProgramState> getProgramList(){
        return programStates;
    }

    @Override
    public void setProgramList(List<ProgramState> newProgramList){
        this.programStates = newProgramList;
    }
}
