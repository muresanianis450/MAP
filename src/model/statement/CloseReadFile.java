package model.statement;

import exceptions.MyException;
import model.ADT.FileTable.IFileTable;
import model.expression.Expression;
import model.state.ProgramState;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFile implements Statement {
    private final Expression expression;

    public CloseReadFile(Expression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        // Evaluate the expression
        Value value = expression.evaluate(state.symbolTable());
        if (!(value instanceof StringValue stringValue)) {
            throw new MyException("Expression must evaluate to a string value!");
        }

        IFileTable fileTable = state.fileTable();

        // Check if file is in FileTable
        if (!fileTable.isDefined(stringValue)) {
            throw new MyException("File is not opened: " + stringValue);
        }

        BufferedReader reader = fileTable.lookup(stringValue);
        try {
            reader.close();
        } catch (IOException e) {
            throw new MyException("Error closing file: " + e.getMessage());
        }

        // Remove entry from FileTable
        fileTable.remove(stringValue);

        return state;
    }

    @Override
    public String toString() {
        return "CloseReadFile(" + expression + ")";
    }
}
