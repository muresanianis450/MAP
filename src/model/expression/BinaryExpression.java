package model.expression;

import exceptions.MyException;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.type.BooleanType;
import model.type.IntegerType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.Value;

public record BinaryExpression(String operator, Expression left, Expression right)
        implements Expression {

    @Override
    public Value evaluate(IMap<String, Value> symTable, IHeap heap) throws MyException {
        Value leftTerm = left.evaluate(symTable, heap);
        Value rightTerm = right.evaluate(symTable, heap);

        return switch (operator) {
            //boolean logic
            case "&&", "||" -> evaluateLogical(leftTerm, rightTerm);

            //comparison
            case "<", ">", "<=", ">=" -> evaluateComparison(leftTerm, rightTerm);

            //equality
            case "==", "!=" -> evaluateEquality(leftTerm, rightTerm);

            default -> throw new MyException("Unknown binary operator: " + operator);
        };
    }

    private BooleanValue evaluateLogical(Value leftTerm, Value rightTerm) throws MyException {
        if( (leftTerm.getType() instanceof BooleanType)
                || ( rightTerm.getType() instanceof BooleanType)) // before it was != Type.BOOLEAN but now Type is no longer enum
            throw new MyException("Logical operators require boolean operands");

        boolean leftVal = ((BooleanValue) leftTerm).getValue();
        boolean rightVal = ((BooleanValue) rightTerm).getValue();

        return switch (operator) {
            case "&&" -> new BooleanValue(leftVal && rightVal);
            case "||" -> new BooleanValue(leftVal || rightVal);
            default -> throw new MyException("Invalid logical operator: " + operator);
        };
    }

    private BooleanValue evaluateComparison(Value leftTerm, Value rightTerm) throws MyException {
        if ((leftTerm.getType() instanceof IntegerType)
                || (rightTerm.getType() instanceof IntegerType))
            throw new MyException("Comparison operators require integer operands");

        int leftVal = ((IntegerValue) leftTerm).getValue();
        int rightVal = ((IntegerValue) rightTerm).getValue();

        return switch (operator) {
            case "<" -> new BooleanValue(leftVal < rightVal);
            case ">" -> new BooleanValue(leftVal > rightVal);
            case "<=" -> new BooleanValue(leftVal <= rightVal);
            case ">=" -> new BooleanValue(leftVal >= rightVal);
            default -> throw new MyException("Invalid comparison operator: " + operator);
        };
    }

    private BooleanValue evaluateEquality(Value leftTerm, Value rightTerm) throws MyException {
        if (leftTerm.getType() != rightTerm.getType())
            throw new MyException("Equality operators require operands of the same type");

        boolean result = switch (operator) {
            case "==" -> leftTerm.equals(rightTerm);
            case "!=" -> !leftTerm.equals(rightTerm);
            default -> throw new MyException("Invalid equality operator: " + operator);
        };

        return new BooleanValue(result);
    }
}
