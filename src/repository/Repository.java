package repository;

import exceptions.MyException;
import model.state.ProgramState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {

    private final List<ProgramState> programStates = new ArrayList<>();
    private final String logFilePath;

    public Repository(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    @Override
    public void addProgram(ProgramState programState) {
        programStates.add(programState);
    }

    @Override
    public ProgramState getCurrentProgram() {
        return programStates.get(0);
    }

    @Override
    public void logPrgStateExec() throws MyException {
        ProgramState state = getCurrentProgram();
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {

            logFile.println("------------------------------------------------------------");
            logFile.println("ExeStack:");
            logFile.println(state.executionStack());  // will rely on stack's custom toString()

            logFile.println("SymTable:");
            logFile.println(state.symbolTable());

            logFile.println("Out:");
            logFile.println(state.out());

            logFile.println("FileTable:");
            logFile.println("(not implemented yet)");

            logFile.println("------------------------------------------------------------\n");

        } catch (IOException e) {
            throw new MyException("Error writing to log file: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return programStates.toString();
    }
}
