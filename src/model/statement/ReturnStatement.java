package model.statement;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.state.ProgramState;
import model.type.Type;

//FOR PROCEDURES : LEAVED THE PROCEDURE AND RESTORE THE CALLER SCOPE
//pops the top SymTable from SymTableStack
//meaning I exit the procedure's scope
// and the caller's symTable becomes current again
public class ReturnStatement implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        state.popSymTable();
        return null;
    }

    @Override public Statement deepCopy(){return new ReturnStatement();}

    @Override
    public IMap<String,Type> typeCheck(IMap<String, Type> typeEnv) {
        return typeEnv;
    }

    @Override
    public String toString(){
        return "return";
    }


}
