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

    // ✅ REQUIRED NAME
    public void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException {

        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });

        List<Callable<ProgramState>> callList = prgList.stream()
                .map(p -> (Callable<ProgramState>) p::oneStep)
                .collect(Collectors.toList());

        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try { return future.get(); }
                    catch (Exception e) { return null; }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        prgList.addAll(newPrgList);

        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });

        repo.setProgramList(prgList);
    }

    public void shutdownExecutor() {
        executor.shutdownNow();
    }

    public IRepository getRepository() {
        return repo;
    }
}
