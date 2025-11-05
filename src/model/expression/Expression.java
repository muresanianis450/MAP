package model.expression;

import exceptions.MyException;
import model.ADT.Map.IMap;
import model.value.Value;

public interface Expression {
    Value evaluate(IMap symbolTable) throws MyException;
}
