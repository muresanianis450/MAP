package model.ADT.Stack;

import model.statement.Statement;

import java.util.LinkedList;
import java.util.List;

public class StackExecutionStack implements IStack {

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = stack.size() - 1; i >= 0; i--) {
            sb.append(stack.get(i));
            if (i > 0) sb.append(" | ");
        }
        return sb.toString();
    }
}
