package controller;
import GC.GarbageCollector;
import model.state.ProgramState;
import model.statement.Statement;
import repository.IRepository;
import exceptions.MyException;

import java.util.List;
import java.util.stream.Collectors;


public class Controller {

    private final IRepository repository;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

        public void executeOneStep(ProgramState state) throws MyException{
            if(state.executionStack().isEmpty()){
                throw new MyException("Execution stack is empty!"); //TODO LOGFILE DOES NOT SHOW MULTIPLE PROGRAM ID's
            }

            //Pop the top statement
            Statement current = state.executionStack().pop();
            ProgramState newPrg = current.execute(state);

            //LOG parent state after execution
            repository.logPrgStateExec(state);

            //If a fork occured
            if(newPrg != null){
                repository.addProgram(newPrg);

                //LOG the new thread too

                repository.logPrgStateExec(newPrg);
            }
        }

    //Remove completed programs

    public void oneStepForALlPrograms(List<ProgramState> programList) throws MyException{

        //run one step for each program
        List<ProgramState> newPrograms = programList.stream()
                .map(prg -> {
                    try{
                        return prg.oneStep();
                    }catch(MyException e){
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(p -> p!=null) //keep only forks
                .collect(Collectors.toList());

        programList.addAll(newPrograms);

        //log every program
        programList.forEach(prg ->{
            try{ repository.logPrgStateExec(prg);}
            catch(MyException e){ throw new RuntimeException(e);}
        });

        //update repo
        repository.setProgramList(programList);
    }

    private List<ProgramState> removeCompletedProgram(List<ProgramState> programList){
        return programList.stream()
                .filter(p -> !p.executionStack().isEmpty())
                .collect(Collectors.toList());
    }


    public void executeAllSteps() {

        List<ProgramState> programList = removeCompletedProgram(repository.getProgramList());

        try {
            while (!programList.isEmpty()) {

               oneStepForALlPrograms(programList);

                //Garbage Collector for ALL states
                for(ProgramState prg: programList){
                    prg.heap().setContent(
                            GarbageCollector.safeGarbageCollector(
                                    prg.symbolTable().getContent().values(),
                                    prg.heap().getContent()
                            )
                    );
                }

               programList = removeCompletedProgram(repository.getProgramList());

            }
        } catch (MyException e) {
            System.out.println(e.getMessage() + "HERE") ;
        }
    }

    public IRepository getRepository() {
        return repository;
    }
}

