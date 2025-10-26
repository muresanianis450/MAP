package model.state;

import model.statement.Statement;

import java.util.LinkedList;
import java.util.List;

public class StackExecutionStack implements ExecutionStack {

    private final List<Statement> stack = new LinkedList<>();

    @Override
    public void push(Statement statement){
        stack.addFirst(statement);
    }

    @Override
    public Boolean isEmpty(){
        return stack.isEmpty();
    }
    @Override
    public Statement pop(){
        return stack.removeFirst();
    }
}
