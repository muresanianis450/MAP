package view;

import controller.Controller;
import exceptions.MyException;
import model.ADT.FileTable.FileTable;
import model.ADT.List.ListOut;
import model.ADT.Map.SymbolTable;
import model.ADT.Stack.StackExecutionStack;
import model.expression.*;
import model.state.ProgramState;
import model.statement.*;
import model.type.BooleanType;
import model.type.IntegerType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import repository.Repository;
import model.value.StringValue;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // --- Example programs ---
        Statement ex1 = new CompoundStatement(
                new VariableDeclarationStatement("v", new IntegerType()),
                new CompoundStatement(
                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                        new PrintStatement(new VariableExpression("v"))
                )
        );

        Statement ex2 = new CompoundStatement(
                new VariableDeclarationStatement("a", new IntegerType()),
                new CompoundStatement(
                        new VariableDeclarationStatement("b", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("a",
                                        new BinaryExpression("+",
                                                new ConstantExpression(new IntegerValue(2)),
                                                new BinaryExpression("*",
                                                        new ConstantExpression(new IntegerValue(3)),
                                                        new ConstantExpression(new IntegerValue(5)))
                                        )
                                ),
                                new CompoundStatement(
                                        new AssignmentStatement("b",
                                                new BinaryExpression("+",
                                                        new VariableExpression("a"),
                                                        new ConstantExpression(new IntegerValue(1)))
                                        ),
                                        new PrintStatement(new VariableExpression("b"))
                                )
                        )
                )
        );

        Statement ex3 = new CompoundStatement(
                new VariableDeclarationStatement("a", new BooleanType()),
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new BooleanType()),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ConstantExpression(new BooleanValue(true))),
                                new CompoundStatement(
                                        new IfStatement(
                                                new VariableExpression("a"),
                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(3)))
                                        ),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        );
        // int x; openReadFile("numbers.txt"); readFile("numbers.txt", x); print(x);

        Statement fileProgramAll = new CompoundStatement(
                new VariableDeclarationStatement("x", new IntegerType()),
                new CompoundStatement(
                        new OpenReadFile(new ConstantExpression(new StringValue("src/numbers.txt"))),
                        new ReadAllFileAutomatically("src/numbers.txt", "x")
                )
        );

        // --- Read log file path ---
        System.out.print("Enter log file path: ");
        String logFilePath = scanner.nextLine().trim();

        // --- Main loop ---
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

            System.out.println("\nSelect program to run:");
            System.out.println("1) int v; v = 2; print(v)");
            System.out.println("2) int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)");
            System.out.println("3) bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)");
            System.out.println("4) int x; openReadFile(\"src/numbers.txt\"); readFile(\"src/numbers.txt\", x); print(x)");
            System.out.print("> ");
            String programOption = scanner.nextLine().trim();

            Statement program = switch (programOption) {
                case "1" -> ex1;
                case "2" -> ex2;
                case "3" -> ex3;
                case "4" -> fileProgramAll; // <-- new option for file operations
                default -> null;
            };

            if (program == null) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            // Prepare shared components
            var stack = new StackExecutionStack();
            stack.push(program);
            var symbols = new SymbolTable();
            var out = new ListOut();
            var fileTable = new FileTable();
            var programState = new ProgramState(stack, symbols, out,fileTable);

            var repository = new Repository(logFilePath);
            repository.addProgram(programState);
            var controller = new Controller(repository);

            if (mainOption.equals("1")) {
                // --- Interactive execution ---
                System.out.print("Do you want to display the Program State at each step? [y/n]: ");
                boolean displayState = scanner.nextLine().trim().equalsIgnoreCase("y");

                System.out.println("\nRunning program " + programOption + " (Assignment 2)...\n");

                try {
                    repository.logPrgStateExec(); // log initial state

                    while (!programState.executionStack().isEmpty()) {
                        controller.executeOneStep(programState);
                        repository.logPrgStateExec();

                        if (displayState) {
                            System.out.println("ExeStack=" + programState.executionStack());
                            System.out.println("SymbolTable=" + programState.symbolTable());
                            System.out.println("Output=" + programState.out());
                            System.out.println("File Table =");

                            if (programState.fileTable().getContent().isEmpty()) {
                                System.out.println("  <empty>");
                            } else {
                                programState.fileTable().getContent().keySet()
                                        .forEach(k -> System.out.println("  - " + k));
                            }


                            System.out.println();
                        }
                    }

                    System.out.println("Final Output: " + programState.out() + "\n");

                } catch (MyException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else if (mainOption.equals("2")) {
                // --- Full execution with logging ---
                System.out.println("\nRunning program " + programOption + " (Assignment 3)...\n");

                controller.executeAllSteps(); // will log after each step
                System.out.println("Execution finished. Check the log file at: " + logFilePath);

            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }
}
