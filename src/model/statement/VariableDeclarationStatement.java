package model.statement;
import model.state.ProgramState;
import model.type.Type;

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
        state.symbolTable().declareVariable(variableName, type.defaultValue());
        return state;
    }

    @Override
    public String toString(){
        return type +" "+variableName;
    }
}

