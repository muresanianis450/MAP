package model.statement;
import model.state.ProgramState;

//Represents two statements executed in sequence
public class CompoundStatement implements Statement{
    private final Statement first;
    private final Statement second;
    public CompoundStatement(Statement first, Statement second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        state.executionStack().push(second);
        state.executionStack().push(first);
        return null;
    }

    @Override
    public String toString(){
        return "(" + first + "; " + second + ")";
    }

    @Override
    public Statement deepCopy(){
        return new CompoundStatement(first.deepCopy(), second.deepCopy());
    }
}
