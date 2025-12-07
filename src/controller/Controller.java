package controller;

import model.state.ProgramState;
import repository.IRepository;
import exceptions.MyException;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/*EXPLICATIONS:
removeCompletedPrg:
    -inPrgList.stream() : converts the List<ProgramState> into a stream, which allows
                          functional-style operations like filter, map , forEach
    -.filter(ProgramState::isNotCompleted)
                        : filter keeps only elements that satisfy a predicate
                          This is equivalent to: .filter( p -> p.isNotCompleted())
    -.collect(Collectors.toList()) : after filtering, collect gathers the remaining elements back into a List
                                   Collectors.toList() is a standard way to convert a stream back to a list

oneStepForAllPrg
    -prgList.stream() : same thing
    -.map(...)         : map transforms each element of the stream into something else.
                        Here: each ProgramState p is transformed into a Callable<ProgramState>
                        The lambda () -> p.oneStep() is the body of the callable. It will execute onStep of the program when called
                        (Callable<ProgramState>) is just casting it explicitly
    -.collect(Collectors.toList()) : converts the stream of Callables back into a list, ready to be executed concurrently.
* */
public class Controller {

    private IRepository repo;
    private ExecutorService executor;

    public Controller(IRepository r) { this.repo = r; }

    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    // OLD NAME: executeOneStep
    // NEW REQUIRED NAME: oneStepForAllPrg
    public void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException {

        // log each PrgState BEFORE execution
        prgList.forEach(prg -> {
            try { repo.logPrgStateExec(prg); }
            catch (MyException e) { throw new RuntimeException(e); }
        });

        // Prepare callables
        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (() -> p.oneStep()))
                .collect(Collectors.toList());

        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try { return future.get(); }
                    catch (Exception e) { System.out.println(e.getMessage()); return null; }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        prgList.addAll(newPrgList);

        // log AFTER execution
        prgList.forEach(prg -> {
            try { repo.logPrgStateExec(prg); }
            catch (MyException e) { throw new RuntimeException(e); }
        });

        repo.setProgramList(prgList);
    }

    public void allStep() throws InterruptedException {

        executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgList = removeCompletedPrg(repo.getProgramList());

        while (prgList.size() > 0) {

            oneStepForAllPrg(prgList);

            prgList = removeCompletedPrg(repo.getProgramList());
        }

        executor.shutdownNow();

        repo.setProgramList(prgList);
    }
    public IRepository getRepository() {
        return repo;
    }
}
