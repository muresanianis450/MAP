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
import model.type.*;
import model.value.*;
import repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class Interpreter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Supplier<Statement>> examples = new HashMap<>();
        Map<String, String> descriptions = new HashMap<>();

        examples.put("1", () -> new CompoundStatement(
                new VariableDeclarationStatement("v", new IntegerType()),
                new CompoundStatement(
                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                        new PrintStatement(new VariableExpression("v"))
                )
        ));
        descriptions.put("1", "int v; v = 2; print(v)");

        examples.put("2", () -> new CompoundStatement(
                new VariableDeclarationStatement("a", new IntegerType()),
                new CompoundStatement(
                        new VariableDeclarationStatement("b", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("a",
                                        new BinaryExpression("+",
                                                new ConstantExpression(new IntegerValue(2)),
                                                new BinaryExpression("*",
                                                        new ConstantExpression(new IntegerValue(3)),
                                                        new ConstantExpression(new IntegerValue(5))
                                                )
                                        )
                                ),
                                new CompoundStatement(
                                        new AssignmentStatement("b",
                                                new BinaryExpression("+",
                                                        new VariableExpression("a"),
                                                        new ConstantExpression(new IntegerValue(1))
                                                )
                                        ),
                                        new PrintStatement(new VariableExpression("b"))
                                )
                        )
                )
        ));
        descriptions.put("2", "int a; int b; a = 2 + 3*5; b = a + 1; print(b)");

        examples.put("3", () -> new CompoundStatement(
                new VariableDeclarationStatement("a", new BooleanType()),
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
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
        ));
        descriptions.put("3", "bool a; int v; a = true; if (a) then v=2 else v=3; print(v)");

        examples.put("4", () -> new CompoundStatement(
                new VariableDeclarationStatement("x", new IntegerType()),
                new CompoundStatement(
                        new OpenReadFile(new ConstantExpression(new StringValue("src/numbers.txt"))),
                        new CompoundStatement(
                                new ReadFile(new ConstantExpression(new StringValue("src/numbers.txt")), "x"),
                                new CompoundStatement(
                                        new PrintStatement(new VariableExpression("x")),
                                        new CompoundStatement(
                                                new ReadFile(new ConstantExpression(new StringValue("src/numbers.txt")), "x"),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("x")),
                                                        new CloseReadFile(new ConstantExpression(new StringValue("src/numbers.txt")))
                                                )
                                        )
                                )
                        )
                )
        ));
        descriptions.put("4", "int x; openReadFile('numbers.txt'); readAllFile('numbers.txt', x)");

        examples.put("5", () -> new CompoundStatement(
                new VariableDeclarationStatement("s", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("s", new ConstantExpression(new StringValue("Hello, World!"))),
                        new PrintStatement(new VariableExpression("s"))
                )
        ));
        descriptions.put("5", "string s; s = \"Hello, World!\"; print(s)");

        // Example 6: Relational Expression
        examples.put("6", () -> new CompoundStatement(
                new VariableDeclarationStatement("x", new IntegerType()),
                new CompoundStatement(
                        new AssignmentStatement("x", new ConstantExpression(new IntegerValue(7))),
                        new PrintStatement(new RelationalExpression(
                                new VariableExpression("x"),
                                new ConstantExpression(new IntegerValue(5)),
                                ">"
                        ))
                )
        ));
        descriptions.put("6", "int x; x = 7; print(x > 5)");


        // --- Select mode ---
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

            // --- Select program ---
            System.out.println("\nSelect program to run:");
            examples.keySet().forEach(k -> System.out.println(k + ") " + descriptions.get(k)));
            System.out.print("> ");
            String programOption = scanner.nextLine().trim();

            if (!examples.containsKey(programOption)) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            Statement program = examples.get(programOption).get(); // fresh instance
            String logFilePath;
            System.out.print("Enter log file path (e.g., log.txt): ");
            logFilePath = scanner.nextLine().trim();

            ProgramState programState = new ProgramState(
                    new StackExecutionStack(),
                    new SymbolTable(),
                    new ListOut(),
                    new FileTable(),
                    program
            );
            programState.executionStack().push(program);

            Repository repo = new Repository(logFilePath);
            repo.addProgram(programState);
            Controller controller = new Controller(repo);

            if (mainOption.equals("1")) {
                System.out.print("Display Program State after each step? [y/n]: ");
                boolean displayState = scanner.nextLine().trim().equalsIgnoreCase("y");
                try {
                    repo.logPrgStateExec(); // initial log

                    while (!programState.executionStack().isEmpty()) {
                        controller.executeOneStep(programState);

                        if (displayState) {
                            System.out.println("ExeStack=" + programState.executionStack());
                            System.out.println("SymTable=" + programState.symbolTable());
                            System.out.println("Out=" + programState.out());
                            System.out.println("FileTable=" + programState.fileTable().getContent());
                            System.out.println();
                        }
                    }

                    System.out.println("Final Output: " + programState.out());
                } catch (MyException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (mainOption.equals("2")) {
                try {
                    controller.executeAllSteps();
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
