package model.ADT.Stack;
import model.statement.Statement;

// TODO Make generic ADT
public interface IStack {
    void push(Statement statement);

    Statement pop();

    Boolean isEmpty();
}
