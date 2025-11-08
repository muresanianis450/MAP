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

public class OpenReadFile implements Statement {
    private final Expression expression;

    public OpenReadFile(Expression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value value = expression.evaluate(state.symbolTable());
        if (!(value.getType() instanceof StringValue stringValue)) {
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

        return state;
    }

    @Override
    public String toString() {
        return "OpenReadFile(" + expression + ")";
    }
}
