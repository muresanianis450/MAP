package model.statement;

import model.ADT.Map.IMap;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.Type;
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
        Value value = expression.evaluate(state.symbolTable(),state.heap());
        state.symbolTable().update(variableName, value);
     //   return state;
        return null;
    }

    @Override
    public String toString(){
        return variableName + "= " + expression;
    }

    @Override
    public Statement deepCopy() {
        return new AssignmentStatement(variableName, expression.deepCopy());
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {

        if (!typeEnv.isDefined(variableName))
            throw new MyException("Assignment: variable not declared");

        Type varType = typeEnv.lookup(variableName);
        Type expType = expression.typeCheck(typeEnv);

        if (!varType.equals(expType))
            throw new MyException("Assignment: right hand side and left hand side have different types");

        return typeEnv;
    }
}
