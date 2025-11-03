package view;

import controller.Controller;
import exceptions.MyException;
import model.expression.*;
import model.state.*;
import model.statement.*;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import repository.Repository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask the user if they want to see each program state
        System.out.print("Do you want to display the Program State? [y/n]: ");
        boolean displayState = scanner.nextLine().trim().equalsIgnoreCase("y");

        // --- Example 1 ---
        Statement ex1 = new CompoundStatement(
                new VariableDeclarationStatement("v", Type.INTEGER),
                new CompoundStatement(
                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                        new PrintStatement(new VariableExpression("v"))
                )
        );

        // --- Example 2 ---
        Statement ex2 = new CompoundStatement(
                new VariableDeclarationStatement("a", Type.INTEGER),
                new CompoundStatement(
                        new VariableDeclarationStatement("b", Type.INTEGER),
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

        // --- Example 3 ---
        Statement ex3 = new CompoundStatement(
                new VariableDeclarationStatement("a", Type.BOOLEAN),
                new CompoundStatement(
                        new VariableDeclarationStatement("v", Type.INTEGER),
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

        // --- Interactive loop ---
        while (true) {
            System.out.println("\nEnter the number of the exercise you want to run:");
            System.out.println("1) int v; v = 2; print(v)");
            System.out.println("2) int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)");
            System.out.println("3) bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)");
            System.out.println("0) Exit");
            System.out.print("> ");

            String option = scanner.nextLine().trim();

            if (option.equals("0")) {
                System.out.println("Exiting...");
                break;
            }

            Statement program = switch (option) {
                case "1" -> ex1;
                case "2" -> ex2;
                case "3" -> ex3;
                default -> null;
            };

            if (program == null) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            var stack = new StackExecutionStack();
            stack.push(program);
            var symbols = new MapSymbolTable();
            var out = new ListOut();

            var programState = new ProgramState(stack, symbols, out);
            var repository = new Repository();
            repository.addProgram(programState);
            var controller = new Controller(repository);

            System.out.println("\nRunning program " + option + "...\n");

            try {
                while (!programState.executionStack().isEmpty()) {
                    if (displayState) {
                        System.out.println("ExeStack=" + programState.executionStack());
                        System.out.println("SymbolTable=" + programState.symbolTable());
                        System.out.println("Output=" + programState.out());
                        System.out.println();
                    }
                    controller.executeOneStep(programState);
                }

                System.out.println("ExeStack=" + programState.executionStack());
                System.out.println("SymbolTable=" + programState.symbolTable());
                System.out.println("Output=" + programState.out());
                System.out.println("\nFinal Output: " + programState.out() + "\n");

            } catch (MyException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
