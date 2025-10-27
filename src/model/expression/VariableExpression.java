package model.expression;
import model.value.Value;
import model.state.SymbolTable;
import model.exceptions.MyException;

public record VariableExpression(String variableName) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable)throws MyException{
        if(!symbolTable.isDefined(variableName)){
            throw new RuntimeException("Variable not defined<Variable_Execution_evaluate>");
        }
        return symbolTable.getValue(variableName);
    }


}
