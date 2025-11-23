package model.statement;

import exceptions.MyException;
import model.ADT.FileTable.IFileTable;
import model.ADT.Map.IMap;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.value.IntegerValue;
import model.value.StringValue;
import model.value.Value;
import model.type.Type;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements Statement {
    private final Expression expression;
    private final String variableName;

    public ReadFile(Expression expression, String variableName) {
        this.expression = expression;
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value value = expression.evaluate(state.symbolTable(),state.heap());
        if (!(value instanceof StringValue stringValue)) {
            throw new MyException("Expression must evaluate to a string value!");
        }

        IFileTable fileTable = state.fileTable();
        if (!fileTable.isDefined(stringValue)) {
            throw new MyException("File not opened: " + stringValue);
        }

        IMap<String, Value> symTable = state.symbolTable();
        if (!symTable.isDefined(variableName)) {
            throw new MyException("Variable " + variableName + " is not defined!");
        }

        if (!(symTable.getValue(variableName).getType() instanceof IntegerType)) {
            throw new MyException("Variable " + variableName + " must be of type int!");
        }

        BufferedReader reader = fileTable.lookup(stringValue);

        try {
            // Read only ONE line
            String line = reader.readLine();
            int number = (line == null || line.trim().isEmpty()) ? 0 : Integer.parseInt(line.trim());
            symTable.update(variableName, new IntegerValue(number));
            //state.out().add(new IntegerValue(number));
        } catch (IOException e) {
            throw new MyException("Error reading from file: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new MyException("File contains non-integer data!");
        }

        return state;
    }

    @Override
    public String toString() {
        return "ReadFile(" + expression + ", " + variableName + ")";
    }
}
