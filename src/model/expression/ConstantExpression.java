package model.expression;
import model.ADT.Heap.IHeap;
import model.ADT.Map.IMap;
import model.value.Value;

public record ConstantExpression(Value value) implements Expression {

    @Override
    public Value evaluate(IMap<String, Value> symTable, IHeap heap) {
        return value;
    }

    @Override
    public Expression deepCopy() {
        return new ConstantExpression(value.deepCopy());
    }
}
