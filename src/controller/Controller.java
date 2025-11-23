package controller;
import GC.GarbageCollector;
import model.state.ProgramState;
import model.statement.Statement;
import repository.IRepository;
import exceptions.MyException;


public class Controller {

    private final IRepository repository;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    public void executeOneStep(ProgramState state) throws MyException{
        if(state.executionStack().isEmpty()){
            throw new MyException("Execution stack is empty!");
        }

        //Pop the top statement
        Statement current = state.executionStack().pop();

        //Execute it
        current.execute(state);

        //log the current step
        repository.logPrgStateExec();
    }

    public void executeAllSteps() {
        ProgramState program = repository.getCurrentProgram();
        try {
            repository.logPrgStateExec();
            while (!program.executionStack().isEmpty()) {
                executeOneStep(program);
                //repository.logPrgStateExec(); ->Accidentally duplicated it

                //Garbage Collector: clean up heap after each step
                program.heap().setContent(
                        GarbageCollector.safeGarbageCollector(
                                program.symbolTable().getContent().values(),
                                program.heap().getContent()
                        )
                );
            }
            System.out.println(program);
        } catch (MyException e) {
            System.out.println(e.getMessage());
        }
    }

    public IRepository getRepository() {
        return repository;
    }
}

