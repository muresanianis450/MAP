package controller;
import model.state.ProgramState;
import model.statement.Statement;
import repository.Repository;
import exceptions.MyException;


public class Controller {

    private final Repository repository;

    public Controller(Repository repository) {
        this.repository = repository;
    }

    public void executeOneStep(ProgramState state) throws MyException{
        if(state.executionStack().isEmpty()){
            throw new MyException("Execution stack is empty!");
        }
        Statement current = state.executionStack().pop();
        current.execute(state);
    }

    public void executeAllSteps() {
        ProgramState program = repository.getCurrentProgram();
        try {
            while (!program.executionStack().isEmpty()) {
                executeOneStep(program);
            }
        } catch (MyException e) {
            System.out.println(e.getMessage());
        }
    }
}

