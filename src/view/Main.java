package view;

import controller.Controller;
import model.expression.*;
import model.state.*;
import model.statement.*;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import repository.Repository;
import exceptions.MyException;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Toy Language Interpreter ===");
        System.out.println("\nSimple Commands:");
        System.out.println("  int x           - declare integer variable");
        System.out.println("  bool flag       - declare boolean variable");
        System.out.println("  x = 5           - assign value");
        System.out.println("  x = -10         - assign negative value");
        System.out.println("  print x         - print variable");
        System.out.println("\nExpressions:");
        System.out.println("  Arithmetic: +, -, *, /");
        System.out.println("  Comparison: <, >, <=, >=, ==, !=");
        System.out.println("  Logical: &&, ||");
        System.out.println("\nControl Flow:");
        System.out.println("  if (x > 0) { print x } else { print 0 }");
        System.out.println("\nSpecial Commands:");
        System.out.println("  run    - execute the program");
        System.out.println("  clear  - start over");
        System.out.println("  exit   - quit");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        List<Statement> statements = new ArrayList<>();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            if (input.equals("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            if (input.equals("clear")) {
                statements.clear();
                System.out.println("Program cleared.");
                continue;
            }

            if (input.equals("run")) {
                runProgram(buildProgram(statements));
                continue;
            }

            try {
                Statement stmt = parseStatement(input);
                statements.add(stmt);
                System.out.println("✓ Added to program");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static Statement buildProgram(List<Statement> statements) {
        if (statements.isEmpty()) {
            return new NoOperationStatement();
        }
        Statement result = statements.get(0);
        for (int i = 1; i < statements.size(); i++) {
            result = new CompoundStatement(result, statements.get(i));
        }
        return result;
    }

    private static void runProgram(Statement program) {
        var stack = new StackExecutionStack();
        stack.push(program);
        var symbols = new MapSymbolTable();
        var out = new ListOut();
        var programState = new ProgramState(stack, symbols, out);
        var repository = new Repository();
        repository.addProgram(programState);
        var controller = new Controller(repository);

            controller.executeAllSteps();

        System.out.println("Output: " + out);
    }

    // ========== PARSING ==========

    private static Statement parseStatement(String input) {
        input = input.trim();

        // Variable declarations: "int x" or "bool flag"
        if (input.startsWith("int ")) {
            String varName = input.substring(4).trim();
            return new VariableDeclarationStatement(varName, Type.INTEGER);
        }

        if (input.startsWith("bool ")) {
            String varName = input.substring(5).trim();
            return new VariableDeclarationStatement(varName, Type.BOOLEAN);
        }

        // Print statement: "print x"
        if (input.startsWith("print ")) {
            String exprStr = input.substring(6).trim();
            Expression expr = parseExpression(exprStr);
            return new PrintStatement(expr);
        }

        // If statement: "if (condition) { then-part } else { else-part }"
        if (input.startsWith("if ")) {
            return parseIfStatement(input);
        }

        // Assignment: "x = 5"
        int equalsPos = input.indexOf('=');
        if (equalsPos > 0) {
            String varName = input.substring(0, equalsPos).trim();
            String exprStr = input.substring(equalsPos + 1).trim();
            Expression expr = parseExpression(exprStr);
            return new AssignmentStatement(varName, expr);
        }

        throw new RuntimeException("Unknown statement. Try: 'int x', 'x = 5', 'print x', or 'if (...) {...} else {...}'");
    }

    private static Statement parseIfStatement(String input) {
        // Find the condition in parentheses
        int condStart = input.indexOf('(');
        int condEnd = findMatchingParen(input, condStart);

        if (condStart == -1 || condEnd == -1) {
            throw new RuntimeException("If statement needs condition in parentheses: if (condition) {...}");
        }

        String condStr = input.substring(condStart + 1, condEnd).trim();
        Expression condition = parseExpression(condStr);

        String remaining = input.substring(condEnd + 1).trim();

        // Find the then block in braces
        int thenStart = remaining.indexOf('{');
        int thenEnd = findMatchingBrace(remaining, thenStart);

        if (thenStart == -1 || thenEnd == -1) {
            throw new RuntimeException("If statement needs then block in braces: if (...) { statements }");
        }

        String thenStr = remaining.substring(thenStart + 1, thenEnd).trim();
        Statement thenStmt = parseBlock(thenStr);

        remaining = remaining.substring(thenEnd + 1).trim();

        // Find the else block
        if (!remaining.startsWith("else")) {
            throw new RuntimeException("If statement needs else block: if (...) {...} else {...}");
        }

        remaining = remaining.substring(4).trim();
        int elseStart = remaining.indexOf('{');
        int elseEnd = findMatchingBrace(remaining, elseStart);

        if (elseStart == -1 || elseEnd == -1) {
            throw new RuntimeException("Else block needs braces: else { statements }");
        }

        String elseStr = remaining.substring(elseStart + 1, elseEnd).trim();
        Statement elseStmt = parseBlock(elseStr);

        return new IfStatement(condition, thenStmt, elseStmt);
    }

    private static Statement parseBlock(String block) {
        if (block.isEmpty()) {
            return new NoOperationStatement();
        }

        // Split by semicolon at top level
        List<String> statementStrings = splitByChar(block, ';');
        List<Statement> statements = new ArrayList<>();

        for (String stmtStr : statementStrings) {
            stmtStr = stmtStr.trim();
            if (!stmtStr.isEmpty()) {
                statements.add(parseStatement(stmtStr));
            }
        }

        if (statements.isEmpty()) {
            return new NoOperationStatement();
        }

        Statement result = statements.get(0);
        for (int i = 1; i < statements.size(); i++) {
            result = new CompoundStatement(result, statements.get(i));
        }
        return result;
    }

    private static Expression parseExpression(String input) {
        input = input.trim();

        // Handle logical OR: ||
        int orPos = findOperatorAtTopLevel(input, "||");
        if (orPos != -1) {
            String left = input.substring(0, orPos).trim();
            String right = input.substring(orPos + 2).trim();
            return new BinaryOperatorExpression("||", parseExpression(left), parseExpression(right));
        }

        // Handle logical AND: &&
        int andPos = findOperatorAtTopLevel(input, "&&");
        if (andPos != -1) {
            String left = input.substring(0, andPos).trim();
            String right = input.substring(andPos + 2).trim();
            return new BinaryOperatorExpression("&&", parseExpression(left), parseExpression(right));
        }

        // Handle comparison operators (lower precedence than arithmetic)
        String[] compOps = {"==", "!=", "<=", ">=", "<", ">"};
        for (String op : compOps) {
            int pos = findOperatorAtTopLevel(input, op);
            if (pos != -1) {
                String left = input.substring(0, pos).trim();
                String right = input.substring(pos + op.length()).trim();
                return new BinaryOperatorExpression(op, parseExpression(left), parseExpression(right));
            }
        }

        // Handle addition and subtraction (lower precedence than multiplication)
        int addPos = findOperatorAtTopLevel(input, "+");
        if (addPos != -1) {
            String left = input.substring(0, addPos).trim();
            String right = input.substring(addPos + 1).trim();
            return new BinaryOperatorExpression("+", parseExpression(left), parseExpression(right));
        }

        // For subtraction, skip if it's at the start (negative number)
        int subPos = findOperatorAtTopLevel(input, "-");
        if (subPos > 0) {  // Changed from != -1 to > 0
            String left = input.substring(0, subPos).trim();
            String right = input.substring(subPos + 1).trim();
            return new BinaryOperatorExpression("-", parseExpression(left), parseExpression(right));
        }

        // Handle multiplication and division (higher precedence)
        int mulPos = findOperatorAtTopLevel(input, "*");
        if (mulPos != -1) {
            String left = input.substring(0, mulPos).trim();
            String right = input.substring(mulPos + 1).trim();
            return new BinaryOperatorExpression("*", parseExpression(left), parseExpression(right));
        }

        int divPos = findOperatorAtTopLevel(input, "/");
        if (divPos != -1) {
            String left = input.substring(0, divPos).trim();
            String right = input.substring(divPos + 1).trim();
            return new BinaryOperatorExpression("/", parseExpression(left), parseExpression(right));
        }

        // Handle parentheses
        if (input.startsWith("(") && input.endsWith(")")) {
            return parseExpression(input.substring(1, input.length() - 1));
        }

        // Handle literals and variables
        return parseLiteral(input);
    }

    private static Expression parseLiteral(String input) {
        input = input.trim();

        // Boolean literals
        if (input.equals("true")) {
            return new ConstantExpression(new BooleanValue(true));
        }
        if (input.equals("false")) {
            return new ConstantExpression(new BooleanValue(false));
        }

        // Integer literals
        if (isInteger(input)) {
            return new ConstantExpression(new IntegerValue(Integer.parseInt(input)));
        }

        // Variable
        if (isValidIdentifier(input)) {
            return new VariableExpression(input);
        }

        throw new RuntimeException("Invalid expression: " + input);
    }

    // ========== HELPER METHODS ==========

    private static int findMatchingParen(String str, int openPos) {
        if (openPos == -1 || openPos >= str.length() || str.charAt(openPos) != '(') {
            return -1;
        }

        int depth = 0;
        for (int i = openPos; i < str.length(); i++) {
            if (str.charAt(i) == '(') depth++;
            else if (str.charAt(i) == ')') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    private static int findMatchingBrace(String str, int openPos) {
        if (openPos == -1 || openPos >= str.length() || str.charAt(openPos) != '{') {
            return -1;
        }

        int depth = 0;
        for (int i = openPos; i < str.length(); i++) {
            if (str.charAt(i) == '{') depth++;
            else if (str.charAt(i) == '}') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    private static int findOperatorAtTopLevel(String str, String operator) {
        int depth = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(' || c == '{') depth++;
            else if (c == ')' || c == '}') depth--;
            else if (depth == 0 && str.startsWith(operator, i)) {
                // For single-char operators like -, make sure it's not part of a number
                if (operator.equals("-") && i == 0) {
                    continue; // Skip negative sign at start
                }
                // Make sure we don't match single < or > when looking for <= or >=
                if (operator.length() == 1 && i + 1 < str.length()) {
                    char next = str.charAt(i + 1);
                    if ((c == '<' || c == '>' || c == '=' || c == '!') && next == '=') {
                        continue; // Skip, this is part of a two-char operator
                    }
                }
                return i;
            }
        }
        return -1;
    }

    private static List<String> splitByChar(String str, char delimiter) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(' || c == '{') depth++;
            else if (c == ')' || c == '}') depth--;

            if (c == delimiter && depth == 0) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            result.add(current.toString());
        }

        return result;
    }

    private static boolean isInteger(String str) {
        if (str.isEmpty()) return false;
        int start = 0;
        if (str.charAt(0) == '-') {
            if (str.length() == 1) return false;
            start = 1;
        }
        for (int i = start; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }

    private static boolean isValidIdentifier(String str) {
        if (str.isEmpty()) return false;
        if (!Character.isLetter(str.charAt(0)) && str.charAt(0) != '_') return false;
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') return false;
        }
        return true;
    }
}

