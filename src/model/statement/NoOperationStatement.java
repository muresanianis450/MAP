package model.statement;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.Type;

//used when nothing left to execute
public class NoOperationStatement implements Statement{

    @Override
    public ProgramState execute(ProgramState state) {
        return null; //TODO Maybe should return null ??
    }
    @Override
    public String toString() {
        return "no, operation";
    }
    @Override
    public Statement deepCopy() {
        return new NoOperationStatement();
    }

    @Override
    public IMap <String,model.type.Type> typeCheck(IMap<String, Type> typeEnv) {
        return typeEnv;
    }
}
