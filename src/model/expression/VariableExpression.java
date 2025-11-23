package model.expression;
import model.ADT.Heap.IHeap;
import model.value.Value;
import model.ADT.Map.IMap;
import exceptions.MyException;

public record VariableExpression(String variableName) implements Expression {

    @Override
    public Value evaluate(IMap<String, Value> symTable, IHeap heap)throws MyException{
        if(!symTable.isDefined(variableName)){
            throw new RuntimeException("Variable not defined<Variable_Execution_evaluate>");
        }
        return symTable.getValue(variableName);
    }


}
