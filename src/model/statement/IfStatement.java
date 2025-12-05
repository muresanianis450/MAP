package model.statement;
import model.expression.Expression;
import model.state.ProgramState;
import model.value.BooleanValue;
import model.value.Value;
import exceptions.MyException;


//executes one of two statements depending on the condition
public class IfStatement implements Statement{
    private final Expression condition;
    private final Statement thenStatement;
    private final Statement elseStatement;

    public IfStatement(Expression condition, Statement thenStatement, Statement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;

    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        Value conditionValue = condition.evaluate(state.symbolTable(),state.heap());
        if(!(conditionValue instanceof BooleanValue booleanValue)){
            throw new UnsupportedOperationException("If statement is not a boolean: " + conditionValue) ;
        }

        if(booleanValue.getValue()){
            state.executionStack().push(thenStatement);
        }else{
            state.executionStack().push(elseStatement);
        }

        //return state;
        return null;     }

    @Override
    public String toString(){
        return "if(" + condition + ")"+"then(" + thenStatement +") else( " + elseStatement;

    }

    @Override
    public Statement deepCopy(){
        return new IfStatement(condition.deepCopy(), thenStatement.deepCopy(), elseStatement);
    }
}
