package model.expression;
import model.value.Value;
import model.ADT.Map.IMap;
import exceptions.MyException;

public record VariableExpression(String variableName) implements Expression {

    @Override
    public Value evaluate(IMap symbolTable)throws MyException{
        if(!symbolTable.isDefined(variableName)){
            throw new RuntimeException("Variable not defined<Variable_Execution_evaluate>");
        }
        return symbolTable.getValue(variableName);
    }


}
