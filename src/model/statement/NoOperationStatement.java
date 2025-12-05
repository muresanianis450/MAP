package model.statement;
import model.state.ProgramState;
//used when nothing left to execute
public class NoOperationStatement implements Statement{

    @Override
    public ProgramState execute(ProgramState state) {
        return null; // does nothing
    }
    @Override
    public String toString() {
        return "no, operation";
    }
    @Override
    public Statement deepCopy() {
        return new NoOperationStatement();
    }
}
