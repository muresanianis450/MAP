package model.ADT.Stack;
import model.statement.Statement;


public interface IStack <T>{
    void push(T item);

    T pop();

    Boolean isEmpty();
}
