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

public class Main {
    public static void main(String[] args) {
        try {
            // Example 1: int v; v = 2; print(v);
            Statement ex1 = new CompoundStatement(
                    new VariableDeclarationStatement("v", Type.INTEGER),
                    new CompoundStatement(
                            new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                            new PrintStatement(new VariableExpression("v"))
                    )
            );

            // Example 2: int a; a = 2 + 3 * 5; int b; b = a - 4 / 2 + 7; print(b);
            Statement ex2 = new CompoundStatement(
                    new VariableDeclarationStatement("a", Type.INTEGER),
                    new CompoundStatement(
                            new AssignmentStatement("a",
                                    new ArithmeticExpression("+",
                                            new ConstantExpression(new IntegerValue(2)),
                                            new ArithmeticExpression("*",
                                                    new ConstantExpression(new IntegerValue(3)),
                                                    new ConstantExpression(new IntegerValue(5)))
                                    )
                            ),
                            new CompoundStatement(
                                    new VariableDeclarationStatement("b", Type.INTEGER),
                                    new CompoundStatement(
                                            new AssignmentStatement("b",
                                                    new ArithmeticExpression("+",
                                                            new ArithmeticExpression("-",
                                                                    new VariableExpression("a"),
                                                                    new ArithmeticExpression("/",
                                                                            new ConstantExpression(new IntegerValue(4)),
                                                                            new ConstantExpression(new IntegerValue(2)))
                                                            ),
                                                            new ConstantExpression(new IntegerValue(7))
                                                    )
                                            ),
                                            new PrintStatement(new VariableExpression("b"))
                                    )
                            )
                    )
            );

            // Example 3: bool a; a = false; int v; if (a) then v = 2 else v = 3; print(v);
            Statement ex3 = new CompoundStatement(
                    new VariableDeclarationStatement("a", Type.BOOLEAN),
                    new CompoundStatement(
                            new AssignmentStatement("a", new ConstantExpression(new BooleanValue(false))),
                            new CompoundStatement(
                                    new VariableDeclarationStatement("v", Type.INTEGER),
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

            System.out.println("=== Example 1 ===");
            runProgram(ex1);

            System.out.println("\n=== Example 2 ===");
            runProgram(ex2);

            System.out.println("\n=== Example 3 ===");
            runProgram(ex3);

        } catch (MyException e) {
            System.out.println("Runtime Error: " + e.getMessage());
        }
    }

    private static void runProgram(Statement program) throws MyException {
        var stack = new StackExecutionStack();
        var symbols = new MapSymbolTable();
        var out = new ListOut();
        var programState = new ProgramState(stack, symbols, out);
        var repository = new Repository();
        repository.addProgram(programState);
        var controller = new Controller(repository);

        stack.push(program);
        controller.executeAllSteps();
        System.out.println("Output: " + out);
    }
}
