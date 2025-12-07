package view;

import controller.Controller;
import exceptions.MyException;
import model.state.ProgramState;

import java.util.Scanner;

public class RunExample extends Command {
    private final Controller controller;

    public RunExample(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nRun mode options:");
        System.out.println("1) Interactive execution (step-by-step)");
        System.out.println("2) Full execution with logging");
        System.out.print("> ");
        String option = scanner.nextLine().trim();

        try {
            if (option.equals("1")) {
                // Interactive mode
                System.out.println("\n--- Interactive Execution ---");

                while (true) {
                    var programs = controller.getRepository().getProgramList();

                    if (programs.isEmpty()) {
                        System.out.println("Program finished successfully.");
                        break;
                    }

                    // print ALL current program states
                    for (ProgramState p : programs) {
                        System.out.println("\n=== PROGRAM " + p.getId() + " ===");
                        System.out.println(p);
                        controller.getRepository().logPrgStateExec(p);
                    }

                    System.out.println("Press ENTER to execute next step...");
                    scanner.nextLine();

                    controller.oneStepForAllPrg(controller.getRepository().getProgramList());
                }
            }
            else if (option.equals("2")) {
                // Full execution mode
                System.out.println("\n--- Full Execution ---");
                controller.oneStepForAllPrg(controller.getRepository().getProgramList());
            }
            else {
                System.out.println("Invalid option.\n");
            }
        } catch (MyException e) {
            System.out.println("Execution error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
