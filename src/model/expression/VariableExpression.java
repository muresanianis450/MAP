package model.expression;
import model.ADT.Heap.IHeap;
import model.value.Value;
import model.ADT.Map.IMap;
import exceptions.MyException;
import model.type.Type;

public record VariableExpression(String variableName) implements Expression {

    @Override
    public Value evaluate(IMap<String, Value> symTable, IHeap heap)throws MyException{
        if(!symTable.isDefined(variableName)){
            throw new RuntimeException("Variable not defined<Variable_Execution_evaluate>");
        }
        return symTable.getValue(variableName);
    }
    @Override
    public Expression deepCopy(){
        return new VariableExpression(this.variableName);
    }

    @Override
    public Type typeCheck(IMap<String, Type> typeEnv) throws MyException {
        if (!typeEnv.isDefined(variableName)) {
            throw new MyException("Variable " + variableName + " is not defined in the type environment");
        }
        return typeEnv.getType(variableName);
    }


}
