package model.expression;
import model.value.Value;
import model.state.SymbolTable;

public record VariableExpression(String variableName) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable){
        if(!symbolTable.isDefined(variableName)){
            throw new RuntimeException("Variable not defined<Variable_Execution_evaluate>");
        }
        return symbolTable.getValue(variableName);
    }


}
