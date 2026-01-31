package model.expression;


import exceptions.MyException;
import model.ADT.FileTable.IFileTable;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.type.BooleanType;
import model.type.IntegerType;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.Value;

import java.net.Inet4Address;
import model.type.Type;

public class RelationalExpression implements Expression {
    private final Expression left;
    private final Expression right;
    private final String operator; // < > == != <= >=

    public RelationalExpression(Expression left, Expression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;

    }

    @Override
    public Value evaluate(IMap<String, Value> symTable, IHeap heap) throws MyException {
        Value v1 = left.evaluate(symTable, heap);
        Value v2 = right.evaluate(symTable, heap);

        // == and != should work for any types (as long as Value.equals is correct)
        if (operator.equals("==")) {
            return new BooleanValue(v1.equals(v2));
        }
        if (operator.equals("!=")) {
            return new BooleanValue(!v1.equals(v2));
        }

        // the other relational ops require integers
        if (!(v1.getType() instanceof IntegerType) || !(v2.getType() instanceof IntegerType)) {
            throw new MyException("Relational operands must be integers!");
        }

        int n1 = ((IntegerValue) v1).getValue();
        int n2 = ((IntegerValue) v2).getValue();

        return switch (operator) {
            case "<" -> new BooleanValue(n1 < n2);
            case ">" -> new BooleanValue(n1 > n2);
            case "<=" -> new BooleanValue(n1 <= n2);
            case ">=" -> new BooleanValue(n1 >= n2);
            default -> throw new MyException("Invalid relational operator: " + operator);
        };
    }


    @Override
    public String toString(){
        return left + " " +  operator + " " + right;
    }

    @Override
    public Expression deepCopy() {
        return new RelationalExpression(left.deepCopy(), right.deepCopy(), operator);
    }

    @Override
    public Type typeCheck(IMap<String, Type> typeEnv) throws MyException {
        Type leftType = left.typeCheck(typeEnv);
        Type rightType = right.typeCheck(typeEnv);

        switch (operator) {
            case "<", ">", "<=", ">=" -> {
                if (!(leftType instanceof IntegerType))
                    throw new MyException("Left operand of " + operator + " is not an integer.");
                if (!(rightType instanceof IntegerType))
                    throw new MyException("Right operand of " + operator + " is not an integer.");
                return new BooleanType(); // relational expressions always return boolean
            }
            case "==", "!=" -> {
                if (!leftType.equals(rightType))
                    throw new MyException("Operands of " + operator + " must have the same type.");
                return new BooleanType();
            }
            default -> throw new MyException("Unknown relational operator: " + operator);
        }
    }

}
