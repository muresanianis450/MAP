package model.ADT.Stack;
import model.statement.Statement;

import java.util.List;


public interface IStack <T>{
    void push(T item);

    T pop();

    Boolean isEmpty();

    T peek();           // New
    List<T> getReversed(); // new helper for logging + cloning
}
