package model.statement;
import exceptions.MyException;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.Type;

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

    @Override
    public IMap <String,Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {
        return second.typeCheck(first.typeCheck(typeEnv));
    }
}
