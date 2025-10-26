package model.expression;

import exceptions.MyException;
import model.state.SymbolTable;
import model.value.Value;

public interface Expression {
    Value evaluate(SymbolTable symbolTable) throws MyException;
}
