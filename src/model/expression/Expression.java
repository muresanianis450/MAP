package model.expression;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.value.Value;
import model.ADT.Heap.IHeap;

public interface Expression {
    Value evaluate(IMap<String,Value> symTable, IHeap heap) throws MyException;

    Expression deepCopy();
}
