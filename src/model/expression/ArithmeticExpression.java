package model.expression;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public record ArithmeticExpression(String operator, Expression left, Expression right)
    implements Expression{

    @Override
    public Value evaluate(IMap symbolTable) throws MyException {
        Value leftTerm = left.evaluate(symbolTable);
        Value rightTerm = right.evaluate(symbolTable);

        if((leftTerm.getType() instanceof IntegerType )
          || (rightTerm.getType() instanceof IntegerType))
            throw new MyException("Arithmetic operation require interger operands");

        IntegerValue leftValue = (IntegerValue) leftTerm;
        IntegerValue rightValue = (IntegerValue) rightTerm;

        return switch(operator){
            case "+" -> new IntegerValue(leftValue.getValue() + rightValue.getValue());
            case "-" -> new IntegerValue(leftValue.getValue() - rightValue.getValue());
            case "*" -> new IntegerValue(leftValue.getValue() * rightValue.getValue());
            case "/" -> {
                if(rightValue.getValue() == 0)
                    throw new MyException ("Division by zero");

                yield new IntegerValue(leftValue.getValue() / rightValue.getValue());
            }
            default -> throw new MyException ("Invalid arithmetic operator: " + operator);


        };

    }
}
