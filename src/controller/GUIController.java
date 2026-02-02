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
    private int rrIndex = 0;

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


 //ROUND-ROBIN IMPLEMENTATION ( one step for ONE program per click ( for semaphores and GUI)) 
        // keep only runnable
        List<ProgramState> runnable = removeCompletedPrg(prgList);
        if (runnable.isEmpty()) {
            throw new MyException("Program finished!");
        }

        // keep repo list in sync with runnable (optional but keeps GUI clean)
        repo.setProgramList(runnable);

        // round-robin: pick ONE program state to execute this click
        if (rrIndex >= runnable.size()) rrIndex = 0;

        ProgramState current = runnable.get(rrIndex);

        // log BEFORE step (step-by-step requirement)
        repo.logPrgStateExec(current);

        // execute one step
        ProgramState forked = current.oneStep();

        // if fork happened, add new state
        if (forked != null) {
            runnable.add(forked);
        }

        // advance round-robin index for next click
        rrIndex++;
        if (rrIndex >= runnable.size()) rrIndex = 0;

        repo.setProgramList(runnable);

     /*   // keep only runnable programs for stepping
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

        repo.setProgramList(prgList); */
    }


    public void shutdownExecutor() {
        executor.shutdownNow();
    }

    public IRepository getRepository() {
        return repo;
    }
}
