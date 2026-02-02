package model.expression;

import exceptions.MyException;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.type.IntegerType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class MulExpression implements Expression {
    private final Expression exp1;
    private final Expression exp2;

    public MulExpression(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public Value evaluate(IMap<String, Value> symTable, IHeap heap) throws MyException {
        Value v1 = exp1.evaluate(symTable, heap);
        if (!v1.getType().equals(new IntegerType())) {
            throw new MyException("MUL: first operand is not int");
        }

        Value v2 = exp2.evaluate(symTable, heap);
        if (!v2.getType().equals(new IntegerType())) {
            throw new MyException("MUL: second operand is not int");
        }

        int n1 = ((IntegerValue) v1).getValue();
        int n2 = ((IntegerValue) v2).getValue();

        int result = (n1 * n2) - (n1 + n2);
        return new IntegerValue(result);
    }

    @Override
    public Type typeCheck(IMap<String, Type> typeEnv) throws MyException {
        Type t1 = exp1.typeCheck(typeEnv);
        Type t2 = exp2.typeCheck(typeEnv);

        if (!t1.equals(new IntegerType())) {
            throw new MyException("MUL: first operand not int");
        }
        if (!t2.equals(new IntegerType())) {
            throw new MyException("MUL: second operand not int");
        }
        return new IntegerType();
    }

    @Override
    public Expression deepCopy() {
        return new MulExpression(exp1.deepCopy(), exp2.deepCopy());
    }

    @Override
    public String toString() {
        return "MUL(" + exp1 + ", " + exp2 + ")";
    }
}
