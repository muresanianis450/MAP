package model.statement;
import model.expression.Expression;
import model.state.ProgramState;
import exceptions.MyException;

public class PrintStatement implements Statement {
    private final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;

    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        var value = expression.evaluate(state.symbolTable());
        state.out().add(value);
        return state;
    }

    @Override
    public String toString(){
        return "print(" + expression + ")";

    }

}
