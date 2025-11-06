package model.statement;

import exceptions.MyException;
import model.ADT.FileTable.IFileTable;
import model.expression.ConstantExpression;
import model.state.ProgramState;
import model.value.StringValue;

public class ReadAllFileAutomatically implements Statement {
    private final String fileName;
    private final String variable;

    public ReadAllFileAutomatically(String fileName, String variable){
        this.fileName = fileName;
        this.variable = variable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IFileTable fileTable = state.fileTable();
        StringValue fileKey = new StringValue(fileName);

        if (!fileTable.isDefined(fileKey)) {
            throw new MyException("File does not exist: " + fileName);
        }

        // Get the reader
        var reader = fileTable.lookup(fileKey);

        try {
            if (reader.ready()) {
                // Push self again for the next integer
                state.executionStack().push(this);
                // Push a ReadFile to read ONE integer now
                state.executionStack().push(
                        new ReadFile(new ConstantExpression(fileKey), variable)
                );
            }
        } catch (Exception e) {
            throw new MyException("Error reading all file: " + e.getMessage());
        }

        return state;
    }

    @Override
    public String toString() {
        return "ReadAllFileAutomatically(" + fileName + ", " + variable + ")";
    }
}
