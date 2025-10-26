package model.statement;

import model.expression.Expression;
import model.state.ProgramState;
import model.value.Value;
import exceptions.MyException;


//Assigns an evaluated expression to a variable
public class AssignmentStatement implements Statement {
    private final String variableName;
    private final Expression expression;

    public AssignmentStatement(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        Value value = expression.evaluate(state.symbolTable());
        state.symbolTable().update(variableName, value);
        return state;
    }

    @Override
    public String toString(){
        return variableName + "= " + expression;
    }
}
