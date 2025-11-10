package model.expression;


import exceptions.MyException;
import model.ADT.FileTable.IFileTable;
import model.ADT.Map.IMap;
import model.type.BooleanType;
import model.type.IntegerType;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.Value;

import java.net.Inet4Address;

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
    public Value evaluate(IMap symTable) throws MyException {
        Value v1 = left.evaluate(symTable);
        Value v2 = right.evaluate(symTable);

        if(!(v1.getType() instanceof  IntegerType )||
                !(v2.getType() instanceof IntegerType) )
        {
            throw new MyException("Relational operands must be integeres!");

        }
        int n1 = ((IntegerValue) v1).getValue();
        int n2 =  ((IntegerValue) v2).getValue();

        return switch(operator){
            case "<" -> new BooleanValue(n1 < n2);
            case ">" -> new BooleanValue(n1 > n2);
            case "==" -> new BooleanValue(n1 == n2);
            case "!=" -> new BooleanValue(n1 != n2);
            case "<=" -> new BooleanValue(n1 <= n2);
            case ">=" -> new BooleanValue(n1 >= n2);
            default -> throw new MyException("Invalid relational operand");
        };
    }

    @Override
    public String toString(){
        return left + " " +  operator + " " + right;
    }

}
