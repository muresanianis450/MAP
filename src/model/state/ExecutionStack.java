package model.state;
import model.statement.Statement;
public interface ExecutionStack {
    void push(Statement statement);

    Statement pop();

    Boolean isEmpty();
}
