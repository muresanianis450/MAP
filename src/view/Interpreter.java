    package view;

    import controller.Controller;
    import exceptions.MyException;
    import model.ADT.FileTable.FileTable;
    import model.ADT.Heap.Heap;
    import model.ADT.Heap.Heap;
    import model.ADT.List.ListOut;
    import model.ADT.LockTable.LockTable;
    import model.ADT.Map.SymbolTable;
    import model.ADT.Stack.StackExecutionStack;
    import model.expression.*;
    import model.state.ProgramState;
    import model.statement.*;
    import model.type.*;
    import model.value.*;
    import repository.Repository;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    import java.util.Scanner;
    import java.util.function.Supplier;

    public class Interpreter {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);


            // Select mode
            while (true) {
                System.out.println("\n==============================");
                System.out.println("Select mode:");
                System.out.println("1) Interactive execution (Assignment 2)");
                System.out.println("2) Full execution with log (Assignment 3)");
                System.out.println("0) Exit");
                System.out.println("==============================");
                System.out.print("> ");
                String mainOption = scanner.nextLine().trim();

                if (mainOption.equals("0")) {
                    System.out.println("Exiting...");
                    break;
                }

                // Get all programs from ExamplePrograms
                List<ExamplePrograms.ProgramExample> programs = ExamplePrograms.getPrograms();

                //Display programs with descriptions
                System.out.println("\nSelect program to run:");
                for (int i = 0; i < programs.size(); i++) {
                    System.out.println((i + 1) + ") " + programs.get(i).getDescription());
                }
                System.out.print("> ");

                int programIndex;
                try {
                    programIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    if (programIndex < 0 || programIndex >= programs.size()) {
                        System.out.println("Invalid choice. Try again.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number.");
                    continue;
                }


                Statement program = programs.get(programIndex).getProgram();


                System.out.println("\nRunning program: " + programs.get(programIndex).getDescription() + "\n");

                System.out.print("Enter log file path (e.g., log.txt): ");
                String logFilePath = scanner.nextLine().trim();

                //Initialize ProgramState
                ProgramState programState = new ProgramState(
                        new StackExecutionStack(),
                        new SymbolTable(),
                        new ListOut(),
                        new FileTable(),
                        new Heap(),
                        new LockTable(),
                        program
                );

                programState.executionStack().push(program);

                Repository repo = new Repository(logFilePath);
                repo.addProgram(programState);
                Controller controller = new Controller(repo);

                // --- Run program ---
                if (mainOption.equals("1")) {
                    System.out.print("Display Program State after each step? [y/n]: ");
                    boolean displayState = scanner.nextLine().trim().equalsIgnoreCase("y");
                    try {
                        repo.logPrgStateExec(programState); // initial log

                        while (!programState.executionStack().isEmpty()) {
                            controller.oneStepForAllPrg(repo.getProgramList());

                            if (displayState) {
                                System.out.println("ExeStack=" + programState.executionStack());
                                System.out.println("SymTable=" + programState.symbolTable());
                                System.out.println("Out=" + programState.out());
                                System.out.println("FileTable=" + programState.fileTable().getContent());
                                System.out.println();
                            }
                        }

                        System.out.println("Final Output: " + programState.out());
                    } catch (MyException | InterruptedException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                } else if (mainOption.equals("2")) {
                    try {
                        controller.allStep();
                        System.out.println("Execution finished. Check the log file at: " + logFilePath);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid option. Try again.");
                }
            }
        }
    }
