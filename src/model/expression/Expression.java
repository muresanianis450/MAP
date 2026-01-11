package model.expression;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.type.Type;
import model.value.Value;
import model.ADT.Heap.IHeap;

public interface Expression {
    Value evaluate(IMap<String,Value> symTable, IHeap heap) throws MyException;

    Type typeCheck(IMap<String,Type> typeEnv) throws MyException;
    Expression deepCopy();

}
