package model.expression;

import exceptions.MyException;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public record ArithmeticExpression(String operator, Expression left, Expression right)
    implements Expression{

    @Override
    public Value evaluate(IMap<String, Value> symTable, IHeap heap) throws MyException {
        Value leftTerm = left.evaluate(symTable,heap);
        Value rightTerm = right.evaluate(symTable,heap);

        if(!(leftTerm.getType() instanceof IntegerType )
          || !(rightTerm.getType() instanceof IntegerType))
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
    public Expression deepCopy(){
        return new ArithmeticExpression(this.operator, this.left, this.right);
    }
    @Override
    public Type typeCheck(IMap<String, Type> typeEnv) throws MyException {
        // Type-check left and right expressions
        Type leftType = left.typeCheck(typeEnv);
        Type rightType = right.typeCheck(typeEnv);

        // Both must be integers
        if (!(leftType instanceof IntegerType))
            throw new MyException("Left operand of " + operator + " is not an integer");

        if (!(rightType instanceof IntegerType))
            throw new MyException("Right operand of " + operator + " is not an integer");

        // Arithmetic expressions always return integer
        return new IntegerType();
    }


}
