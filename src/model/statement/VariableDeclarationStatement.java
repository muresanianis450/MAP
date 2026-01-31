package model.statement;
import model.state.ProgramState;
import model.type.Type;
import model.ADT.Map.IMap;
import model.type.Type;
import exceptions.MyException;


//Declares a variable in the symbol table
public class VariableDeclarationStatement implements Statement{
    private final String variableName;
    private final Type type;

    public VariableDeclarationStatement(String variableName, Type type){
        this.variableName = variableName;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state){
        if(state.symbolTable().isDefined(variableName)){
            throw new RuntimeException("Variable already declared: " + variableName);
        }
        //state.symbolTable().declareVariable(variableName, type); BEFORE THE CHANGE
        state.symbolTable().add(variableName, type.defaultValue());
        //return state;
        return null ;     }

    @Override
    public String toString(){
        return type +" "+variableName;
    }

    @Override
    public Statement deepCopy() {
        return new VariableDeclarationStatement(variableName, type);
    }

    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {

        if (typeEnv.isDefined(variableName))
            throw new MyException("Variable already declared: " + variableName);

        typeEnv.add(variableName, type);
        return typeEnv;
    }

}


