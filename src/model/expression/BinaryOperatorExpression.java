package model.expression;

import exceptions.MyException;
import model.state.SymbolTable;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.Value;


// TODO Implement ArithmeticExpression class
public record BinaryOperatorExpression
        (String operator, Expression left, Expression right)
        implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable)  throws MyException {
        var leftTerm = left.evaluate(symbolTable);
        var rightTerm = right.evaluate(symbolTable);

        switch (operator) {
            case "+", "-", "*", "/" -> {
                checkTypes(leftTerm, rightTerm, Type.INTEGER);
                var leftValue = (IntegerValue) leftTerm;
                var rightValue = (IntegerValue) rightTerm;
                return evaluateArithmeticExpression(leftValue, rightValue);
            }
            case "&&", "||" -> {
                checkTypes(leftTerm, rightTerm, Type.BOOLEAN);
                var leftValueB = (BooleanValue) leftTerm;
                var rightValueB = (BooleanValue) rightTerm;
                return evaluateBooleanExpression(leftValueB, rightValueB);
            }
            case "<", ">", "<=", ">=" -> {
                checkTypes(leftTerm, rightTerm, Type.INTEGER);
                var leftValue = (IntegerValue) leftTerm;
                var rightValue = (IntegerValue) rightTerm;
                return evaluateComparisonExpression(leftValue, rightValue);
            }
            case "==", "!=" -> {
                if (leftTerm.getType() != rightTerm.getType()) {
                    throw new MyException("Both operands must have the same type for " + operator);
                }
                return evaluateEqualityExpression(leftTerm, rightTerm);
            }
        }
        throw new MyException("Unknown operator " + operator);
    }

    private void checkTypes(Value leftTerm, Value rightTerm, Type type) throws  MyException {
        if (leftTerm.getType() != type || rightTerm.getType() != type) {
            throw new MyException("Wrong type for operator " + operator);
        }
    }

    private IntegerValue evaluateArithmeticExpression(IntegerValue leftValue, IntegerValue rightValue) throws MyException {
        return switch (operator) {
            case "+" -> new IntegerValue(leftValue.getValue() + rightValue.getValue());
            case "-" -> new IntegerValue(leftValue.getValue() - rightValue.getValue());
            case "*" -> new IntegerValue(leftValue.getValue() * rightValue.getValue());
            case "/" -> {
                if (rightValue.getValue() == 0) {
                    throw new MyException("Division by zero");
                }
                yield new IntegerValue(leftValue.getValue() / rightValue.getValue());
            }
            default -> throw new MyException("Unreachable code");
        };
    }

    private BooleanValue evaluateBooleanExpression(BooleanValue leftValue, BooleanValue rightValue) throws MyException {
        return switch (operator) {
            case "&&" -> new BooleanValue(leftValue.getValue() && rightValue.getValue());
            case "||" -> new BooleanValue(leftValue.getValue() || rightValue.getValue());
            default -> throw new MyException("Unreachable code");
        };
    }

    private BooleanValue evaluateComparisonExpression(IntegerValue leftValue, IntegerValue rightValue) throws MyException{
        return switch (operator) {
            case "<" -> new BooleanValue(leftValue.getValue() < rightValue.getValue());
            case ">" -> new BooleanValue(leftValue.getValue() > rightValue.getValue());
            case "<=" -> new BooleanValue(leftValue.getValue() <= rightValue.getValue());
            case ">=" -> new BooleanValue(leftValue.getValue() >= rightValue.getValue());
            default -> throw new MyException("Unreachable code");
        };
    }

    private BooleanValue evaluateEqualityExpression(Value leftTerm, Value rightTerm) throws MyException{
        boolean result = switch (operator) {
            case "==" -> {
                if (leftTerm.getType() == Type.INTEGER) {
                    yield ((IntegerValue) leftTerm).getValue() == ((IntegerValue) rightTerm).getValue();
                } else {
                    yield ((BooleanValue) leftTerm).getValue() == ((BooleanValue) rightTerm).getValue();
                }
            }
            case "!=" -> {
                if (leftTerm.getType() == Type.INTEGER) {
                    yield ((IntegerValue) leftTerm).getValue() != ((IntegerValue) rightTerm).getValue();
                } else {
                    yield ((BooleanValue) leftTerm).getValue() != ((BooleanValue) rightTerm).getValue();
                }
            }
            default -> throw new MyException("Unreachable code");
        };
        return new BooleanValue(result);
    }
}