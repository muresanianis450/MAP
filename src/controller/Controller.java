package controller;

import model.state.ProgramState;
import repository.IRepository;
import exceptions.MyException;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
