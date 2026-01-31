package controller;

import model.state.ProgramState;
import repository.IRepository;
import exceptions.MyException;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class GUIController {

    private IRepository repo;
    private ExecutorService executor;

    public GUIController(IRepository repo) {
        this.repo = repo;
        this.executor = Executors.newCachedThreadPool();
    }

    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }


    public void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException, MyException {

        // keep only runnable programs for stepping
        List<ProgramState> runnable = removeCompletedPrg(prgList);

        if (runnable.isEmpty()) {
            // no more steps possible
            throw new MyException("Program finished!");
        }



        List<Callable<ProgramState>> callList = runnable.stream()
                .map(p -> (Callable<ProgramState>) p::oneStep)
                .collect(Collectors.toList());

        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try { return future.get(); }
                    catch (Exception e) { return null; }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        // add forked programs to the ORIGINAL list
        prgList.addAll(newPrgList);

        // LOG once per GUI click (BEFORE step is enough; AFTER is optional)
        for (ProgramState prg : runnable) {
            repo.logPrgStateExec(prg);
        }

        // DO NOT remove completed programs here for GUI display purposes
        // (otherwise you lose the final output/state)

        // Optional: log AFTER (but it doubles file size)
        // for (ProgramState prg : prgList) repo.logPrgStateExec(prg);

        repo.setProgramList(prgList);
    }


    public void shutdownExecutor() {
        executor.shutdownNow();
    }

    public IRepository getRepository() {
        return repo;
    }
}
