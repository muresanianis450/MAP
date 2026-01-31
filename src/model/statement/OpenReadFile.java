package model.statement;

import exceptions.MyException;
import model.ADT.FileTable.IFileTable;
import model.expression.Expression;
import model.state.ProgramState;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import model.ADT.Map.IMap;
import model.type.Type;
import model.type.StringType;
public class OpenReadFile implements Statement {
    private final Expression expression;

    public OpenReadFile(Expression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value value = expression.evaluate(state.symbolTable(),state.heap());
        if (!(value instanceof StringValue stringValue)) {
            throw new MyException("Expression must evaluate to a string value!");
        }

        IFileTable fileTable = state.fileTable();
        if (fileTable.isDefined(stringValue)) {
            throw new MyException("File is already opened: " + stringValue);
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(stringValue.getValue()));
            fileTable.add(stringValue, reader);
        } catch (IOException e) {
            throw new MyException("Error opening file: " + e.getMessage());
        }

        //return state;
        return null ;     }

    @Override
    public String toString() {
        return "OpenReadFile(" + expression + ")";
    }

    @Override
    public Statement deepCopy() {
        return new OpenReadFile(this.expression);
    }
    @Override
    public IMap<String, Type> typeCheck(IMap<String, Type> typeEnv) throws MyException {

        Type expType = expression.typeCheck(typeEnv);

        if (!expType.equals(new StringType()))
            throw new MyException("OpenReadFile: expression is not of type string");

        return typeEnv;
    }


}
