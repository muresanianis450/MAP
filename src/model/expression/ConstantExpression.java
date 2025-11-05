package model.expression;
import model.ADT.Map.IMap;
import model.value.Value;

public record ConstantExpression(Value value) implements Expression {

    @Override
    public Value evaluate(IMap symbolTable) {
        return value;
    }
}
