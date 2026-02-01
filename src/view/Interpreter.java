package view;

import controller.Controller;
import exceptions.MyException;
import model.ADT.FileTable.FileTable;
import model.ADT.Heap.Heap;
import model.ADT.List.ListOut;
import model.ADT.LockTable.LockTable;
import model.ADT.Map.IMap;
import model.ADT.Map.SymbolTable;
import model.ADT.Stack.StackExecutionStack;
import model.state.ProgramState;
import model.statement.Statement;
import repository.Repository;

import model.value.Value;

import java.util.List;
import java.util.Scanner;

// NEW imports for procedures
import model.ADT.ProcTable.IProcTable;
import model.ADT.ProcTable.ProcTable;

public class Interpreter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

            // Get all programs
            List<ExamplePrograms.ProgramExample> programs = ExamplePrograms.getPrograms();

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

            // -------------------------------
            // NEW: SymTable STACK (one global table initially)
            // -------------------------------
            StackExecutionStack<IMap<String, Value>> symTableStack = new StackExecutionStack<>();
            symTableStack.push(new SymbolTable<>()); // global scope

            // -------------------------------
            // NEW: ProcTable (shared global)
            // -------------------------------
            IProcTable procTable = new ProcTable();

            try{
                procTable.add("sum", List.of("a", "b"), ExamplePrograms.buildSumBody());
                procTable.add("product", List.of("a", "b"), ExamplePrograms.buildProductBody());
            }catch (MyException e) {
                System.out.println("ProcTable init error: " + e.getMessage());}

            // If you have a “procedure example program”, you can register procedures here.
            // Otherwise, procTable stays empty for old examples and they still work.

            // -------------------------------
            // NEW: ProgramState creation
            // -------------------------------
            StackExecutionStack<Statement> exeStack = new StackExecutionStack<>();
            exeStack.push(program); // push ONCE here

            ProgramState programState = new ProgramState(
                    exeStack,
                    symTableStack,
                    new ListOut<>(),
                    new FileTable(),
                    new Heap(),
                    new LockTable(),
                    procTable,
                    program
            );

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
                            System.out.println("SymTableStack=" + programState.symTableStack()); // NEW
                            System.out.println("Out=" + programState.out());
                            System.out.println("FileTable=" + programState.fileTable().getContent());
                            System.out.println("Heap=" + programState.heap().getContent());
                            System.out.println("ProcTable=" + programState.procTable()); // NEW
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
