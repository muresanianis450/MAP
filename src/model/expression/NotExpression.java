package model.expression;

import exceptions.MyException;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.type.BooleanType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.Value;

public class NotExpression implements Expression {
    private final Expression exp;

    public NotExpression(Expression exp) {
        this.exp = exp;
    }

    @Override
    public Value evaluate(IMap<String, Value> symTable, IHeap heap) throws MyException {
        Value v = exp.evaluate(symTable, heap);
        if (!(v instanceof BooleanValue b)) {
            throw new MyException("NOT: expression is not boolean");
        }
        return new BooleanValue(!b.getValue());
    }

    @Override
    public Type typeCheck(IMap<String, Type> typeEnv) throws MyException {
        Type t = exp.typeCheck(typeEnv);
        if (!t.equals(new BooleanType())) {
            throw new MyException("NOT: expression is not boolean");
        }
        return new BooleanType();
    }

    @Override
    public Expression deepCopy() {
        return new NotExpression(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "!(" + exp + ")";
    }
}
