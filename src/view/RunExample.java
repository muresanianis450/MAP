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

        ProgramState program = controller.getRepository().getProgramList().get(0);

        try {
            if (option.equals("1")) {
                // Interactive mode: press ENTER between steps
                System.out.println("\n--- Interactive Execution ---");
                controller.getRepository().logPrgStateExec(program);
                while (!program.executionStack().isEmpty()) {
                    System.out.println(program);
                    System.out.println("Press ENTER to execute next step...");
                    scanner.nextLine();
                    controller.executeOneStep(program);
                }
                System.out.println("\nProgram finished successfully.");
                System.out.println(program);
            } else if (option.equals("2")) {
                // Full execution mode
                System.out.println("\n--- Full Execution ---");
                controller.executeAllSteps();
            } else {
                System.out.println("Invalid option.\n");
            }
        } catch (MyException e) {
            System.out.println("Execution error: " + e.getMessage());
        }
    }
}
